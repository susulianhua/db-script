/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.cepcoreimpl;

import org.tinygroup.cepcore.*;
import org.tinygroup.cepcore.exception.ServiceNotFoundException;
import org.tinygroup.cepcore.util.CEPCoreUtil;
import org.tinygroup.cepcoreimpl.util.LocalThreadContextUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.event.BaseServiceInfo;
import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.event.ServiceRequest;
import org.tinygroup.event.central.Node;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 当服务执行远程调用时 如果有多个远程处理器可用，则根据配置的EventProcessorChoose choose进行调用
 * 如果未曾配置，则默认为chooser生成权重chooser进行处理
 *
 * @author chenjiao
 */
public class CEPCoreImpl extends AbstractCEPCoreImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(CEPCoreImpl.class);

    private Map<String, List<EventProcessor>> serviceIdMap = new HashMap<String, List<EventProcessor>>();
    /**
     * 存放所有的EventProcessor
     */
    private Map<String, EventProcessor> processorMap = new HashMap<String, EventProcessor>();
    /**
     * 为本地service建立的map，便于通过serviceId迅速找到service
     */
    private Map<String, ServiceInfo> localServiceMap = new HashMap<String, ServiceInfo>();
    /**
     * 为远程service建立的map，便于通过serviceId迅速找到service
     */
    private Map<String, ServiceInfo> remoteServiceMap = new HashMap<String, ServiceInfo>();
    /**
     * 本地service列表
     */
    private List<ServiceInfo> localServices = new ArrayList<ServiceInfo>();
    /**
     * 为eventProcessor每次注册时所拥有的service信息 key为eventProcessor的id
     */
    private Map<String, List<ServiceInfo>> eventProcessorServices = new HashMap<String, List<ServiceInfo>>();

    /**
     *
     */
    private Map<EventProcessor, List<String>> regexMap = new HashMap<EventProcessor, List<String>>();
    /**
     * 存放拥有正则表达式的EventProcessor，没有的不会放入此list
     */
    private List<EventProcessor> processorList = new ArrayList<EventProcessor>();

    private List<EventProcessorRegisterTrigger> triggers = new ArrayList<EventProcessorRegisterTrigger>();

    private CEPCoreProcessFilterChain chain;
    private CEPCoreProcessDealer dealer;

    private void dealAddEventProcessor(EventProcessor eventProcessor) {
        processorMap.put(eventProcessor.getId(), eventProcessor);
        eventProcessor.setCepCore(this);

        List<ServiceInfo> serviceList = eventProcessor.getServiceInfos();
        eventProcessorServices.put(eventProcessor.getId(), serviceList);
        if (serviceList != null && !serviceList.isEmpty()) {
            if (EventProcessor.TYPE_REMOTE != eventProcessor.getType()) {
                addLocalServiceInfo(serviceList, eventProcessor);
            } else {
                addRemoteServiceInfo(serviceList, eventProcessor);
            }
            addServiceInfos(eventProcessor, serviceList);
        }

        addRegex(eventProcessor);
    }

    private void addRemoteServiceInfo(List<ServiceInfo> serviceList, EventProcessor eventProcessor) {
        for (ServiceInfo service : serviceList) {
            String serviceId = service.getServiceId();
            BaseServiceInfo baseServiceInfo = new BaseServiceInfo(service);
            if (remoteServiceMap.containsKey(serviceId)) {
                ServiceInfo oldService = remoteServiceMap.get(serviceId);
                if (oldService.compareTo(baseServiceInfo) != 0) {
                    LOGGER.logMessage(LogLevel.ERROR, "RemoteService发生id重复");
                    logConflictServiceInfo(serviceId, eventProcessor);
                }
            } else {
                remoteServiceMap.put(serviceId, baseServiceInfo);
            }

        }
    }

    private void logConflictServiceInfo(String serviceId, EventProcessor eventProcessor) {
        LOGGER.logMessage(LogLevel.ERROR, "冲突serviceId:{},当前来源:{}", serviceId, eventProcessor.getId());
        List<EventProcessor> list = serviceIdMap.get(serviceId);
        if (list == null) {
            return;
        }
        for (EventProcessor e : list) {
            LOGGER.logMessage(LogLevel.ERROR, "已有来源:{}", e.getId());
        }
    }

    private void addLocalServiceInfo(List<ServiceInfo> serviceList, EventProcessor eventProcessor) {
        for (ServiceInfo service : serviceList) {
            String serviceId = service.getServiceId();
            BaseServiceInfo baseServiceInfo = new BaseServiceInfo(service);
            if (localServiceMap.containsKey(serviceId)) {
                ServiceInfo oldService = localServiceMap.get(serviceId);
                if (oldService.compareTo(baseServiceInfo) != 0) {
                    LOGGER.logMessage(LogLevel.ERROR, "LocalService发生id重复，重复serviceId：{}", serviceId);
                    logConflictServiceInfo(serviceId, eventProcessor);
                }
            } else {
                localServiceMap.put(serviceId, baseServiceInfo);
                localServices.add(baseServiceInfo);
            }
        }
    }

    private void addServiceInfos(EventProcessor eventProcessor, List<ServiceInfo> serviceList) {
        for (ServiceInfo service : serviceList) {
            String name = service.getServiceId();
            if (serviceIdMap.containsKey(name)) {
                List<EventProcessor> list = serviceIdMap.get(name);
                if (!list.contains(eventProcessor)) {
                    list.add(eventProcessor);
                }
            } else {
                List<EventProcessor> list = new ArrayList<EventProcessor>();
                serviceIdMap.put(name, list);
                list.add(eventProcessor);
            }
        }
    }

    private void addRegex(EventProcessor eventProcessor) {
        if (eventProcessor.getRegex() != null && !eventProcessor.getRegex().isEmpty()) {
            regexMap.put(eventProcessor, eventProcessor.getRegex());
            processorList.add(eventProcessor);
        }
    }

    public void registerEventProcessor(EventProcessor eventProcessor) {

        LOGGER.logMessage(LogLevel.INFO, "开始 注册EventProcessor:{}", eventProcessor.getId());
        changeVersion(eventProcessor);
        if (processorMap.containsKey(eventProcessor.getId())) {
            removeEventProcessorInfo(eventProcessor);
        }
        dealAddEventProcessor(eventProcessor);
        LOGGER.logMessage(LogLevel.INFO, "注册EventProcessor:{}完成", eventProcessor.getId());
    }

    private void removeEventProcessorInfo(EventProcessor processor) {
        String processorId = processor.getId();
        if (!eventProcessorServices.containsKey(processorId)) {
            return;
        }
        // 20160811 之前是直接用的processor的引用
        // 若是重复注册且processor和之前的processor服务不一样,导致删除不干净的情况发生
        EventProcessor toRemove = processorMap.remove(processorId);
        // 20160811 之前是get,导致未删除,现在改为remove
        List<ServiceInfo> serviceInfos = eventProcessorServices.remove(processorId);
        for (ServiceInfo service : serviceInfos) {
            removeServiceInfo(toRemove, service);
        }

        removeRegex(toRemove);
    }

    private void removeRegex(EventProcessor eventProcessor) {
        if (eventProcessor.getRegex() != null && !eventProcessor.getRegex().isEmpty()) {
            regexMap.remove(eventProcessor);
            processorList.remove(eventProcessor);
        }
    }

    /**
     * @param eventProcessor
     * @param service
     */
    private void removeServiceInfo(EventProcessor eventProcessor, ServiceInfo service) {
        String serviceId = service.getServiceId();
        if (serviceIdMap.containsKey(serviceId)) {
            // localServices.remove(service);//
            // 20150318调整代码localServices.remove(localServices。indexOf(service))，旧代码有可能是-1
            List<EventProcessor> list = serviceIdMap.get(serviceId);
            if (list.contains(eventProcessor)) {
                list.remove(eventProcessor);
                if (list.isEmpty()) {
                    serviceIdMap.remove(serviceId);
                    // 注销此段逻辑，之前list为空时，会导致checkRemoteOrLocal中localServices删除不掉
                    // 20160811
                    // localServiceMap.remove(serviceId);
                    // remoteServiceMap.remove(serviceId);
                }
                // 检查是否需要从远程服务列表中删除该服务
                checkRemoteOrLocal(list, serviceId);
            }

        } else {
            // do nothing
        }
    }

    /**
     * 如果所有处理服务器都是本地的，则将服务从远程Map中删除
     *
     * @param list
     * @param serviceId
     */
    private void checkRemoteOrLocal(List<EventProcessor> list, String serviceId) {
        boolean hasLocal = false;
        boolean hasRemote = false;
        for (EventProcessor processor : list) {
            if (hasLocal && hasRemote) {
                break;
            }
            if (!hasLocal && CEPCoreUtil.isLocal(processor)) {
                hasLocal = true;
            } else if (!hasRemote && processor.getType() == EventProcessor.TYPE_REMOTE) {
                hasRemote = true;
            }
        }
        if (!hasRemote) {
            remoteServiceMap.remove(serviceId);
        }
        if (!hasLocal) {
            ServiceInfo info = localServiceMap.remove(serviceId);
            if (info != null) {
                localServices.remove(info);
            }
        }

    }

    public void unregisterEventProcessor(EventProcessor eventProcessor) {

        LOGGER.logMessage(LogLevel.INFO, "开始 注销EventProcessor:{}", eventProcessor.getId());
        changeVersion(eventProcessor);
        removeEventProcessorInfo(eventProcessor);
        LOGGER.logMessage(LogLevel.INFO, "注销EventProcessor:{}完成", eventProcessor.getId());
    }

    // private void putThreadVariable(Event event) {
    // ServiceRequest request = event.getServiceRequest();
    // LoggerFactory.putThreadVariable(LoggerFactory.SERVICE_EVENTID,
    // event.getEventId());
    // LoggerFactory.putThreadVariable(LoggerFactory.SERVICE_SERVICEID,
    // request.getServiceId());
    // }
    //
    // private void clearThreadVariable() {
    // LoggerFactory.removeThreadVariable(LoggerFactory.SERVICE_EVENTID);
    // LoggerFactory.removeThreadVariable(LoggerFactory.SERVICE_SERVICEID);
    // }

    public void process(Event event) {
        ServiceRequest request = event.getServiceRequest();
        // // 前置Aop
        // getAopMananger().beforeHandle(event);
        String eventNodeName = event.getServiceRequest().getNodeName();
        LOGGER.logMessage(LogLevel.DEBUG, "请求指定的执行节点为:{0}", eventNodeName);

        EventProcessor eventProcessor = getEventProcessor(request, eventNodeName);
        getChain().getDealer(dealer, event, this, eventProcessor).process(event, this, eventProcessor);
    }


    private EventProcessor getEventProcessorByRegex(ServiceRequest serviceRequest) {
        String serviceId = serviceRequest.getServiceId();
        for (EventProcessor p : processorList) {
            List<String> regex = regexMap.get(p);
            if (p.isEnable() && checkRegex(regex, serviceId)) {
                return p;
            }
        }
        throw new RuntimeException("没有找到合适的服务处理器,服务id:" + serviceId);
    }

    private boolean checkRegex(List<String> regex, String serviceId) {
        for (String s : regex) {
            Pattern pattern = Pattern.compile(s);
            Matcher matcher = pattern.matcher(serviceId);
            boolean b = matcher.matches(); // 满足时，将返回true，否则返回false
            if (b) {
                return true;
            }
        }
        return false;
    }

    private EventProcessor getEventProcessor(ServiceRequest serviceRequest, String eventNodeName) {
        // 查找出所有包含该服务的EventProcessor
        String serviceId = serviceRequest.getServiceId();
        List<EventProcessor> allList = serviceIdMap.get(serviceId);
        if (allList == null) { // 如果取出来是空，先初始化便于处理
            allList = new ArrayList<EventProcessor>();
        }
        List<EventProcessor> list = new ArrayList<EventProcessor>();
        for (EventProcessor e : allList) {
            if (CEPCoreUtil.isTinySysService(serviceId)) {
                list.add(e);
            } else if (e.isEnable()) {
                list.add(e);
            } else {
                LOGGER.logMessage(LogLevel.WARN, "EventProcessor:{} enable为false", e.getId());
            }
        }
        // 如果指定了执行节点名，则根据执行节点查找处理器
        if (!StringUtil.isBlank(eventNodeName)) {
            return findEventProcessor(serviceRequest, eventNodeName, list);
        }

        if (list.isEmpty()) {
            return getEventProcessorByRegex(serviceRequest);
        }
        return getEventProcessor(serviceRequest, list);
    }

    private EventProcessor findEventProcessor(ServiceRequest serviceRequest, String eventNodeName,
                                              List<EventProcessor> list) {
        boolean isCurrentNode = false;
        // 如果指定了执行节点，则判断执行节点是否是当前节点
        String nodeName = getNodeName();
        if (!StringUtil.isBlank(eventNodeName) && !StringUtil.isBlank(nodeName)) {
            LOGGER.logMessage(LogLevel.INFO, "当前节点NodeName:{}", nodeName);
            if (Node.checkEquals(eventNodeName, nodeName)) {
                isCurrentNode = true;
                LOGGER.logMessage(LogLevel.INFO, "请求指定的执行节点即当前节点");
            }
        }
        String serviceId = serviceRequest.getServiceId();
        if (!isCurrentNode) {
            return notCurrentNode(eventNodeName, list, serviceId);
        }
        return isCurrentNode(eventNodeName, list, serviceId);
    }

    private EventProcessor isCurrentNode(String eventNodeName, List<EventProcessor> list, String serviceId) {
        // 如果是当前节点，则判断查找出来的EventProcessor是否是本地处理器，如果是，则返回
        for (EventProcessor e : list) {
            if (CEPCoreUtil.isLocal(e)) {
                return e;
            }
        }
        throw new RuntimeException("当前服务器上不存在请求" + serviceId + "对应的事件处理器");
    }

    private EventProcessor notCurrentNode(String eventNodeName, List<EventProcessor> list, String serviceId) {
        // 如果不是当前节点，则根据节点名查找到指定节点
        // 首先判断该节点是否是存在于已查找到的包含该服务的list之中，如果包含则返回
        // 如果不包含，则读取该节点的正则表达式信息，如果匹配则返回
        for (EventProcessor e : list) {
            if (e.canProcess(eventNodeName)) {
                if (!e.isEnable() && !CEPCoreUtil.isTinySysService(serviceId)) {
                    LOGGER.logMessage(LogLevel.WARN, "EventProcessor:{} enable为false", e.getId());
                    continue;
                }
                // 如果包含该服务的EventProcessor列表中存在该处理器，则返回
                if (list.contains(e)) {
                    return e;
                }
                if (processorList.contains(e)) {
                    List<String> regex = regexMap.get(e);
                    if (checkRegex(regex, serviceId)) {
                        return e;
                    }
                }
                throw new RuntimeException("节点" + eventNodeName + "对应的事件处理器上不存在服务:" + serviceId);
            }
        }
        throw new RuntimeException("当前服务器上不存在节点:" + eventNodeName + "对应的事件处理器");
    }

    private EventProcessor getEventProcessor(ServiceRequest serviceRequest, List<EventProcessor> list) {
        if (list.size() == 1) {
            return list.get(0);
        }

        // 如果有本地的 则直接返回本地的EventProcessor
        for (EventProcessor e : list) {
            if (CEPCoreUtil.isLocal(e)) {
                return e;
            }
        }
        // 如果全是远程EventProcessor,那么需要根据负载均衡机制计算
        return getEventProcessorChoose().choose(list);
    }

    public List<ServiceInfo> getServiceInfos() {
        return localServices;
    }

    private ServiceInfo getLocalServiceInfo(String serviceId) {
        return localServiceMap.get(serviceId);
    }

    private ServiceInfo getRemoteServiceInfo(String serviceId) {
        return remoteServiceMap.get(serviceId);
    }

    public ServiceInfo getServiceInfo(String serviceId) {
        ServiceInfo info = getLocalServiceInfo(serviceId);
        if (info == null) {
            info = getRemoteServiceInfo(serviceId);
        }
        if (info == null) {
            throw new ServiceNotFoundException(serviceId);
        }
        return info;

    }

    public void addEventProcessorRegisterTrigger(EventProcessorRegisterTrigger trigger) {
        triggers.add(trigger);
    }

    public void refreshEventProcessors() {
        for (EventProcessor processor : getEventProcessors()) {
            if (!processor.isRead()) {
                registerEventProcessor(processor);
                processor.setRead(true);
            }
        }
        // if (operator != null && operator instanceof ArOperator) {
        // ((ArOperator) operator).reReg();
        // operator.getClass().getMethod("reReg");
        // }
        if (getOperator() != null) {
            try {
                Method m = getOperator().getClass().getMethod("reReg");
                if (m != null) {
                    m.invoke(getOperator());
                }
            } catch (IllegalArgumentException e) {
                LOGGER.errorMessage(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                LOGGER.errorMessage(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                LOGGER.errorMessage(e.getMessage(), e);
            } catch (SecurityException e) {
                LOGGER.errorMessage(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                LOGGER.errorMessage(e.getMessage(), e);
            }
        }
    }

    public List<EventProcessor> getEventProcessors() {
        List<EventProcessor> processors = new ArrayList<EventProcessor>();
        for (EventProcessor processor : processorMap.values()) {
            processors.add(processor);
        }
        return processors;
    }

    public CEPCoreProcessFilterChain getChain() {
        return chain;
    }

    public void setChain(CEPCoreProcessFilterChain chain) {
        this.chain = chain;
    }

    void setFilters(List<CEPCoreProcessFilter> filters) {
        // 因为每次插入都是放到List首位
        for (int i = filters.size() - 1; i >= 0; i--) {
            CEPCoreProcessFilter filter = filters.get(i);
            chain.insertFilterToBegin(filter);
        }
    }

    public CEPCoreProcessDealer getDealer() {
        return dealer;
    }

    public void setDealer(CEPCoreProcessDealer dealer) {
        this.dealer = dealer;
    }

    class SynchronousDeal implements Runnable {
        Event e;
        EventProcessor eventProcessor;

        public SynchronousDeal(EventProcessor eventProcessor, Event e) {
            this.e = e;
            this.eventProcessor = eventProcessor;
        }

        public void run() {
            // 从请求上下文中解析线程上下文
            // ThreadContextUtil.parseCurrentThreadContext(e.getServiceRequest().getContext());
            LocalThreadContextUtil.synBeforeProcess(e);
            try {
                eventProcessor.process(e);
            } finally {
                LocalThreadContextUtil.synAfterProcess();
            }

        }
    }

}