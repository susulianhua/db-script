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
import org.tinygroup.cepcoreimpl.test.newcase.testcase.BaseDataCreate;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.event.ServiceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCreate extends BaseDataCreate {
    protected Map<String, List<EventProcessor>> createServiceIdMap(
            CEPCore cepcore) {
        return new HashMap<String, List<EventProcessor>>();
    }


    protected Map<String, EventProcessor> createProcessorMap() {
        return new HashMap<String, EventProcessor>();
    }

    protected Map<String, EventProcessor> addProcessorMap(Map<String, EventProcessor> map, String processorId, int type, String ids) {
        EventProcessor processor = getEventProcessor(processorId, type, ids);
        map.put(processorId, processor);
        return map;
    }

    protected Map<String, ServiceInfo> createLocalServiceMap(String ids) {
        List<ServiceInfo> list = createLocalServices(ids);
        Map<String, ServiceInfo> map = new HashMap<String, ServiceInfo>();
        for (ServiceInfo info : list) {
            map.put(info.getServiceId(), info);
        }
        return map;
    }

    protected Map<String, ServiceInfo> createRemoteServiceMap(String ids) {
        Map<String, ServiceInfo> map = new HashMap<String, ServiceInfo>();
        if (StringUtil.isBlank(ids)) {
            return map;
        }
        List<ServiceInfo> list = getService(ids);

        for (ServiceInfo info : list) {
            map.put(info.getServiceId(), info);
        }
        return map;
    }

    protected List<ServiceInfo> createLocalServices(String ids) {
        if (StringUtil.isBlank(ids)) {
            return new ArrayList<ServiceInfo>();
        }
        return getService(ids);
    }

    protected Map<String, List<ServiceInfo>> createEventProcessorServices() {
        return new HashMap<String, List<ServiceInfo>>();
    }

    protected Map<String, List<ServiceInfo>> addEventProcessorServices(
            Map<String, List<ServiceInfo>> map, String eventProcessorId,
            String ids) {
        if (StringUtil.isBlank(eventProcessorId)) {
            return map;
        }
        map.put(eventProcessorId, getService(ids));
        return map;
    }

    public void testVoid() {
    }
}
