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

public class CommonExecuteInfoContainer {
    /**
     * serviceId,所有次数
     */
    private Map<String, Long> serviceTimeMap = new HashMap<String, Long>();

    /**
     * serviceId,异常次数
     */
    private Map<String, Long> serviceExceptionTimeMap = new HashMap<String, Long>();
    /**
     * serviceId,成功次数
     */
    private Map<String, Long> serviceSuccessTimeMap = new HashMap<String, Long>();

    /**
     * eventId,serviceId
     */
    private Map<String, String> serviceInfo = new HashMap<String, String>();

    /**
     * eventId,beginTime
     */
    private Map<String, Long> beginTimeInfo = new HashMap<String, Long>();

    /**
     * service,ExecuteTimeInfo
     */
    private Map<String, ExecuteTimeInfo> executeTimeInfoMap = new HashMap<String, ExecuteTimeInfo>();

    public Long getTotalTimes() {
        return getTotalValue(serviceTimeMap);
    }

    /**
     * 过期方法
     * 请使用getSuccessTimes
     *
     * @return
     */
    @Deprecated
    public Long getSucessTimes() {
        return getSuccessTimes();
    }

    /**
     * @return
     */
    public Long getSuccessTimes() {
        return getTotalValue(serviceSuccessTimeMap);
    }

    public Long getExceptionTimes() {
        return getTotalValue(serviceExceptionTimeMap);
    }

    private Long getTotalValue(Map<String, Long> map) {
        Long result = new Long(0);
        for (Long value : map.values()) {
            result = value + result;
        }
        return result;
    }

    public boolean contain(String eventId) {
        return serviceInfo.containsKey(eventId);
    }

    public void addExecuteBefore(Event e) {
        String serviceId = getServiceId(e);
        serviceInfo.put(e.getEventId(), serviceId);
        addTimeInfo(serviceId, e.getEventId());
        addExecute(serviceId, serviceTimeMap);
    }

    private void addTimeInfo(String serviceId, String eventId) {
        long beginTime = System.currentTimeMillis();
        beginTimeInfo.put(eventId, beginTime);
        if (!executeTimeInfoMap.containsKey(serviceId)) {
            executeTimeInfoMap.put(serviceId, new ExecuteTimeInfo(serviceId));
        }
    }

    public void addExecuteAfter(Event e) {
        String serviceId = getServiceId(e);
        serviceInfo.remove(e.getEventId());
        updateTimeInfo(serviceId, e.getEventId());
        addExecute(serviceId, serviceSuccessTimeMap);
    }

    private void updateTimeInfo(String serviceId, String eventId) {
        long afterTime = System.currentTimeMillis();
        long beginTime = beginTimeInfo.remove(eventId);
        long executeTime = afterTime - beginTime;
        executeTimeInfoMap.get(serviceId).addTime(executeTime);
    }

    public void addExecuteException(Event e, Exception t) {
        String serviceId = getServiceId(e);
        serviceInfo.remove(e.getEventId());
        updateTimeInfo(serviceId, e.getEventId());
        addExecute(serviceId, serviceExceptionTimeMap);
    }

    private void addExecute(String serviceId, Map<String, Long> map) {

        if (map.containsKey(serviceId)) {
            map.put(serviceId, map.get(serviceId) + 1);
            return;
        }
        Long value = Long.valueOf(1);
        map.put(serviceId, value);
    }

    private String getServiceId(Event e) {
        return e.getServiceRequest().getServiceId();
    }

    public void reset() {
        serviceInfo.clear();
        serviceTimeMap.clear();
        serviceSuccessTimeMap.clear();
        serviceExceptionTimeMap.clear();
    }

    public ExecuteTimeInfo getServiceExecuteTimeInfo(String serviceId) {
        return executeTimeInfoMap.get(serviceId);
    }

}
