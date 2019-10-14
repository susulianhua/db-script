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

import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.event.ServiceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataObject extends DataCreate {
    private Map<String, EventProcessor> processorMap = new HashMap<String, EventProcessor>();
    private Map<String, ServiceInfo> localServiceMap = new HashMap<String, ServiceInfo>();
    private List<ServiceInfo> localServices = new ArrayList<ServiceInfo>();
    private Map<String, List<ServiceInfo>> eventProcessorServices = new HashMap<String, List<ServiceInfo>>();
    private Map<String, List<EventProcessor>> serviceIdMap = new HashMap<String, List<EventProcessor>>();
    private Map<String, ServiceInfo> remoteServiceMap = new HashMap<String, ServiceInfo>();

    public Map<String, EventProcessor> getProcessorMap() {
        return processorMap;
    }

    public void setProcessorMap(Map<String, EventProcessor> processorMap) {
        this.processorMap = processorMap;
    }

    public Map<String, ServiceInfo> getLocalServiceMap() {
        return localServiceMap;
    }

    public void setLocalServiceMap(Map<String, ServiceInfo> localServiceMap) {
        this.localServiceMap = localServiceMap;
    }

    public List<ServiceInfo> getLocalServices() {
        return localServices;
    }

    public void setLocalServices(List<ServiceInfo> localServices) {
        this.localServices = localServices;
    }

    public Map<String, List<ServiceInfo>> getEventProcessorServices() {
        return eventProcessorServices;
    }

    public void setEventProcessorServices(
            Map<String, List<ServiceInfo>> eventProcessorServices) {
        this.eventProcessorServices = eventProcessorServices;
    }

    public Map<String, List<EventProcessor>> getServiceIdMap() {
        return serviceIdMap;
    }

    public void setServiceIdMap(Map<String, List<EventProcessor>> serviceIdMap) {
        this.serviceIdMap = serviceIdMap;
    }

    public Map<String, ServiceInfo> getRemoteServiceMap() {
        return remoteServiceMap;
    }

    public void setRemoteServiceMap(Map<String, ServiceInfo> remoteServiceMap) {
        this.remoteServiceMap = remoteServiceMap;
    }

    public void createData(EventProcessor processor, String eventProssorId,
                           String ids, int type) {
        // 创建processorMap
        Map<String, EventProcessor> processorMap = createProcessorMap();
        addProcessorMap(processorMap, eventProssorId, type, ids);
        this.processorMap.putAll(processorMap);

        // 创建eventProcessorServices
        Map<String, List<ServiceInfo>> eventProcessorServices = createEventProcessorServices();
        addEventProcessorServices(eventProcessorServices, eventProssorId, ids);
        this.eventProcessorServices.putAll(eventProcessorServices);

        // 创建serviceIdMap
        List<String> idList = trans(ids);
        for (String str : idList) {
            if (serviceIdMap.containsKey(str)) {
                List<EventProcessor> existList = serviceIdMap.get(str);
                existList.add(processor);
            } else {
                List<EventProcessor> list = new ArrayList<EventProcessor>();
                list.add(processor);
                serviceIdMap.put(str, list);
            }
        }

        if (type == EventProcessor.TYPE_REMOTE) {
            // 创建remoteServiceMap
            Map<String, ServiceInfo> remoteServiceMap = createRemoteServiceMap(ids);
            this.remoteServiceMap.putAll(remoteServiceMap);
        } else {
            // 创建localServiceMap
            Map<String, ServiceInfo> localServiceMap = createLocalServiceMap(ids);
            this.localServiceMap.putAll(localServiceMap);
            // 创建localServices
            List<ServiceInfo> addLocalServices = createLocalServices(ids);
            // 将非重复的服务添加到localServices中
            for (ServiceInfo info : addLocalServices) {
                boolean contains = false;
                for (ServiceInfo addInfo : localServices) {
                    if (info.getServiceId().equals(addInfo.getServiceId())) {
                        contains = true;
                        break;
                    }
                }
                if (!contains) {
                    this.localServices.add(info);
                }
            }
        }
    }

}
