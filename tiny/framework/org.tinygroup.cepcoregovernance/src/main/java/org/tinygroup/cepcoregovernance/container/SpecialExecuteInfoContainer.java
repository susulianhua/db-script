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
package org.tinygroup.cepcoregovernance.container;

import org.tinygroup.event.Event;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class SpecialExecuteInfoContainer {
    /**
     * serviceId,所有次数
     */
    private Map<String, AtomicLong> serviceTimeMap = new HashMap<String, AtomicLong>();

    /**
     * serviceId,异常次数
     */
    private Map<String, AtomicLong> serviceExceptionTimeMap = new HashMap<String, AtomicLong>();
    /**
     * serviceId,成功次数
     */
    private Map<String, AtomicLong> serviceSuccessTimeMap = new HashMap<String, AtomicLong>();
    private Map<String, String> serviceInfo = new HashMap<String, String>();

    //TODO:单个服务越界怎么办，所有服务之和越界怎么办
    public Long getTotalTimes() {
        return getTotalValue(serviceTimeMap);
    }

    @Deprecated
    public Long getSucessTimes() {
        return getSuccessTimes();
    }

    public Long getSuccessTimes() {
        return getTotalValue(serviceSuccessTimeMap);
    }

    public Long getExceptionTimes() {
        return getTotalValue(serviceExceptionTimeMap);
    }


    private Long getTotalValue(Map<String, AtomicLong> map) {
        Long result = new Long(0);
        for (AtomicLong a : map.values()) {
            result = a.get() + result;
        }
        return result;
    }


    public boolean contain(String eventId) {
        return serviceInfo.containsKey(eventId);
    }

    public void addExecuteBefore(Event e) {
        String serviceId = getServiceId(e);
        serviceInfo.put(e.getEventId(), serviceId);
        addExecute(serviceId, serviceTimeMap);
    }

    public void addExecuteAfter(Event e) {
        String serviceId = getServiceId(e);
        serviceInfo.remove(e.getEventId());
        addExecute(serviceId, serviceSuccessTimeMap);
    }

    public void addExecuteException(Event e, Exception t) {
        String serviceId = getServiceId(e);
        serviceInfo.remove(e.getEventId());
        addExecute(serviceId, serviceExceptionTimeMap);
    }

    private void addExecute(String serviceId, Map<String, AtomicLong> map) {

        if (map.containsKey(serviceId)) {
            map.get(serviceId).incrementAndGet();
            return;
        }
        synchronized (map) {
            AtomicLong value = new AtomicLong();
            map.put(serviceId, value);
        }
        map.get(serviceId).incrementAndGet();
    }

    private String getServiceId(Event e) {
        return e.getServiceRequest().getServiceId();
    }

    public void reset() {
        synchronized (this) {
            serviceInfo.clear();
            serviceTimeMap.clear();
            serviceSuccessTimeMap.clear();
            serviceExceptionTimeMap.clear();
        }
    }
}
