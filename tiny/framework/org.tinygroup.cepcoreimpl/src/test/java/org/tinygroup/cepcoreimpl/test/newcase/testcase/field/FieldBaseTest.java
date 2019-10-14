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
package org.tinygroup.cepcoreimpl.test.newcase.testcase.field;

import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcoreimpl.CEPCoreImpl;
import org.tinygroup.cepcoreimpl.test.newcase.VirtualEventProcesor;
import org.tinygroup.event.ServiceInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class FieldBaseTest extends DataCreate {

    private Object getField(CEPCore cepcore, String field) {
        try {
            Field fieldObject = CEPCoreImpl.class.getDeclaredField(field);
            fieldObject.setAccessible(true);
            return fieldObject.get(cepcore);

        } catch (Exception e) {
        }
        return null;
    }

    // 存放所有service所对应的EventProcessor列表
    private Map<String, List<EventProcessor>> getServiceIdMap(CEPCore cepcore) {
        return copyServiceIdMap((Map<String, List<EventProcessor>>) getField(
                cepcore, "serviceIdMap"));
    }

    private Map<String, List<EventProcessor>> copyServiceIdMap(
            Map<String, List<EventProcessor>> map) {
        Map<String, List<EventProcessor>> newMap = new HashMap<String, List<EventProcessor>>();
        for (String key : map.keySet()) {
            List<EventProcessor> list = map.get(key);
            List<EventProcessor> newList = new ArrayList<EventProcessor>();
            newMap.put(key, newList);
            for (EventProcessor e : list) {
                if (e instanceof VirtualEventProcesor) {
                    newList.add(((VirtualEventProcesor) e).clone());
                }
            }
        }
        return newMap;
    }

    // 断言存放所有service所对应的EventProcessor列表
    protected void myAssertServiceIdMap(
            Map<String, List<EventProcessor>> expect, CEPCore cepcore) {
        boolean flag = compareServiceIdMap(expect, getServiceIdMap(cepcore));
        if (!flag) {
            fail();
        }
    }

    protected boolean compareServiceIdMap(
            Map<String, List<EventProcessor>> expect,
            Map<String, List<EventProcessor>> fact) {

        if (fact == null || fact.size() != expect.size()) {
            // 如果不存在或者长度不匹配
            fail();
            return false;
        }
        for (String serviceId : expect.keySet()) {
            List<EventProcessor> list = expect.get(serviceId);
            List<EventProcessor> processors = fact.remove(serviceId);
            // 如果不匹配
            if (!compare(list, processors)) {
                fail();
                return false;
            }
        }
        if (fact.isEmpty()) {
            return true;
        }
        fail();
        return false;
    }

    protected boolean compare(List<EventProcessor> expect,
                              List<EventProcessor> fact) {
        // 如果不存在或者长度不匹配
        if (fact == null || expect.size() != fact.size()) {
            fail();
            return false;
        }

        for (EventProcessor processor : expect) {
            if (!remove(processor, fact)) { // 如果不包含，说明不对
                fail();
                return false;
            }
        }
        if (fact.isEmpty()) {
            return true;
        }
        fail();
        return false;
    }

    protected boolean remove(EventProcessor exceptProcessor,
                             List<EventProcessor> list) {
        EventProcessor target = null;
        String processorId = exceptProcessor.getId();
        for (EventProcessor processor : list) {
            if (processorId.equals(processor.getId()) && processor.getType() == exceptProcessor.getType()) {
                target = processor;
                break;
            }
        }
        if (target == null) {
            fail();
            return false;
        }
        list.remove(target);
        return compare(exceptProcessor, target);
    }

    // 存放所有的EventProcessor
    // private Map<String, EventProcessor> processorMap = new HashMap<String,
    // EventProcessor>();
    private Map<String, EventProcessor> getProcessorMap(CEPCore cepcore) {
        return copyProcessorMap((Map<String, EventProcessor>) getField(cepcore, "processorMap"));
    }

    Map<String, EventProcessor> copyProcessorMap(Map<String, EventProcessor> map) {
        Map<String, EventProcessor> newMap = new HashMap<String, EventProcessor>();
        for (String key : map.keySet()) {
            EventProcessor e = map.get(key);
            if (e instanceof VirtualEventProcesor) {
                newMap.put(key, ((VirtualEventProcesor) e).clone());
            }
        }
        return newMap;
    }

    // 断言存放所有的EventProcessor
    protected void myAssertProcessorMap(Map<String, EventProcessor> except,
                                        CEPCore cepcore) {
        Map<String, EventProcessor> fact = getProcessorMap(cepcore);
        if (!compareProcessorMap(except, fact)) {
            fail();
        }
    }

    private boolean compareProcessorMap(Map<String, EventProcessor> except,
                                        Map<String, EventProcessor> fact) {
        if (fact == null || except.size() != fact.size()) {
            fail();
            return false;
        }
        for (String processorId : except.keySet()) {
            EventProcessor exceptProcessor = except.get(processorId);
            EventProcessor factProcessor = fact.remove(processorId);
            if (!compare(exceptProcessor, factProcessor)) {
                fail();
                return false;
            }
        }
        if (fact.isEmpty()) {
            return true;
        }
        fail();
        return false;
    }

    private boolean compare(EventProcessor exceptProcessor,
                            EventProcessor factProcessor) {
        return compareServiceInfo(exceptProcessor.getServiceInfos(),
                factProcessor.getServiceInfos());
    }

    private boolean compareServiceInfo(List<ServiceInfo> except,
                                       List<ServiceInfo> fact) {
        if (except.size() != fact.size()) {
            fail();
            return false;
        }
        for (ServiceInfo exceptService : except) {
            if (!remove(exceptService, fact)) {
                fail();
                return false;
            }
        }
        if (fact.isEmpty()) {
            return true;
        }
        fail();
        return false;
    }

    protected boolean remove(ServiceInfo except, List<ServiceInfo> fact) {
        ServiceInfo target = null;
        for (ServiceInfo factServiceInfo : fact) {
            String exceptId = except.getServiceId();
            String factId = factServiceInfo.getServiceId();
            if (factId.equals(exceptId)) {
                target = factServiceInfo;
                break;
            }
        }
        if (target == null) {
            fail();
            return false;
        }
        fact.remove(target);
        return true;
    }

    // 为本地service建立的map，便于通过serviceId迅速找到service
    // private Map<String, ServiceInfo> localServiceMap = new HashMap<String,
    // ServiceInfo>();
    private Map<String, ServiceInfo> getLocalServiceMap(CEPCore cepcore) {
        return copy((Map<String, ServiceInfo>) getField(cepcore, "localServiceMap"));
    }

    private Map<String, ServiceInfo> copy(Map<String, ServiceInfo> map) {
        Map<String, ServiceInfo> newMap = new HashMap<String, ServiceInfo>();
        newMap.putAll(map);
        return newMap;
    }

    protected void myAssertLocalServiceMap(Map<String, ServiceInfo> except,
                                           CEPCore cepcore) {
        Map<String, ServiceInfo> fact = getLocalServiceMap(cepcore);
        if (!compare(except, fact)) {
            fail();
        }
    }

    private boolean compare(Map<String, ServiceInfo> except,
                            Map<String, ServiceInfo> fact) {
        if (fact == null || fact.size() != except.size()) {
            fail();
            return false;
        }
        for (String exceptServiceId : except.keySet()) {
            if (!fact.containsKey(exceptServiceId)) {
                fail();
                return false;
            }
            fact.remove(exceptServiceId);

        }
        if (fact.isEmpty()) {
            return true;
        }
        return false;
    }

    // 为远程service建立的map，便于通过serviceId迅速找到service
    // private Map<String, ServiceInfo> remoteServiceMap = new HashMap<String,
    // ServiceInfo>();
    private Map<String, ServiceInfo> getRemoteServiceMap(CEPCore cepcore) {
        return copy((Map<String, ServiceInfo>) getField(cepcore, "remoteServiceMap"));
    }

    protected void myAssertRemoteServiceMap(Map<String, ServiceInfo> except,
                                            CEPCore cepcore) {
        Map<String, ServiceInfo> fact = getRemoteServiceMap(cepcore);
        if (!compare(except, fact)) {
            fail();
        }
    }

    // 本地service列表
    // private List<ServiceInfo> localServices = new ArrayList<ServiceInfo>();
    private List<ServiceInfo> getLocalServices(CEPCore cepcore) {
        return copy((List<ServiceInfo>) getField(cepcore, "localServices"));
    }

    private List<ServiceInfo> copy(List<ServiceInfo> list) {
        List<ServiceInfo> newList = new ArrayList<ServiceInfo>();
        newList.addAll(list);
        return newList;
    }

    protected void myAssertLocalServices(List<ServiceInfo> except,
                                         CEPCore cepcore) {
        List<ServiceInfo> fact = getLocalServices(cepcore);
        if (!compareServiceInfo(except, fact)) {
            fail();
        }
    }

    // 为eventProcessor每次注册时所拥有的service信息 key为eventProcessor的id
    // private Map<String, List<ServiceInfo>> eventProcessorServices = new
    // HashMap<String, List<ServiceInfo>>();
    private Map<String, List<ServiceInfo>> getEventProcessorServices(
            CEPCore cepcore) {
        return copyEventProcessorServices((Map<String, List<ServiceInfo>>) getField(cepcore,
                "eventProcessorServices"));
    }

    private Map<String, List<ServiceInfo>> copyEventProcessorServices(Map<String, List<ServiceInfo>> map
    ) {
        Map<String, List<ServiceInfo>> newMap = new HashMap<String, List<ServiceInfo>>();
        for (String key : map.keySet()) {
            List<ServiceInfo> list = map.get(key);
            List<ServiceInfo> newList = new ArrayList<ServiceInfo>();
            newList.addAll(list);
            newMap.put(key, newList);
        }
        return newMap;
    }

    protected void myAssertEventProcessorServices(
            Map<String, List<ServiceInfo>> except, CEPCore cepcore) {
        Map<String, List<ServiceInfo>> fact = getEventProcessorServices(cepcore);
        if (!compareEventProcessorServices(except, fact)) {
            fail();
        }
    }

    private boolean compareEventProcessorServices(
            Map<String, List<ServiceInfo>> except,
            Map<String, List<ServiceInfo>> fact) {
        if (fact == null || fact.size() != except.size()) {
            fail();
            return false;
        }
        for (String exceptId : except.keySet()) {
            List<ServiceInfo> exceptServices = except.get(exceptId);
            if (!fact.containsKey(exceptId)) {
                fail();
                return false;
            }
            List<ServiceInfo> factServices = fact.remove(exceptId);
            if (!compareServiceInfo(exceptServices, factServices)) {
                fail();
                return false;
            }
        }
        if (fact.isEmpty()) {
            return true;
        }
        fail();
        return false;
    }

    public void test() {
    }
}
