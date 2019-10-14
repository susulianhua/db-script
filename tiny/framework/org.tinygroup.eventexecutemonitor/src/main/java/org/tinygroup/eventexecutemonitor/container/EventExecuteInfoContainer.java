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
package org.tinygroup.eventexecutemonitor.container;

import org.tinygroup.event.Event;
import org.tinygroup.eventexecutemonitor.pojo.ServiceMonitorItem;
import org.tinygroup.tinysqldsl.Pager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zhangliang08072 on 2016/12/20.
 */
public class EventExecuteInfoContainer {

    /**
     * serviceId,所有次数
     */
    private ConcurrentMap<String, Long> serviceTimeMap = new ConcurrentHashMap<String, Long>();


    /**
     * eventId,beginTime
     */
    private ConcurrentMap<String, Long> beginTimeInfo = new ConcurrentHashMap<String, Long>();

    /**
     * eventId,endTime
     */
    private ConcurrentMap<String, Long> endTimeInfo = new ConcurrentHashMap<String, Long>();

    /**
     * service,ExecuteTimeInfo
     */
    private ConcurrentMap<String, EventExecuteTimeInfo> executeTimeInfoMap = new ConcurrentHashMap<String, EventExecuteTimeInfo>();

    public void addExecuteBeforeSyn(Event e) {
        long beginTime = System.currentTimeMillis();
        beginTimeInfo.put(e.getEventId(), beginTime);
    }

    public void addExecuteAfterSyn(Event e) {
        long endTime = System.currentTimeMillis();
        endTimeInfo.put(e.getEventId(), endTime);
    }


    public synchronized void addExecuteBeforeAsyn(Event e) {
        String serviceId = getServiceId(e);
        if (serviceTimeMap.containsKey(serviceId)) {
            serviceTimeMap.put(serviceId, serviceTimeMap.get(serviceId) + 1);
            return;
        }
        serviceTimeMap.put(serviceId, Long.valueOf(1));
    }

    public void addExecuteAfterAsyn(Event e) {
        String serviceId = getServiceId(e);
        String eventId = e.getEventId();
        long afterTime = endTimeInfo.remove(eventId);
        long beginTime = beginTimeInfo.remove(eventId);
        long executeTime = afterTime - beginTime;
        dealExecuteTime(serviceId, executeTime);
    }

    private synchronized void dealExecuteTime(String serviceId, Long executeTime) {
        if (!executeTimeInfoMap.containsKey(serviceId)) {
            executeTimeInfoMap.put(serviceId, new EventExecuteTimeInfo(serviceId));
        }
        executeTimeInfoMap.get(serviceId).addTime(executeTime);
    }

    private String getServiceId(Event e) {
        return e.getServiceRequest().getServiceId();
    }

    public void reset() {
        serviceTimeMap.clear();
        executeTimeInfoMap.clear();
    }

    public EventExecuteTimeInfo getServiceExecuteTimeInfo(String serviceId) {
        return executeTimeInfoMap.get(serviceId);
    }

    public Long getTotalTimesByServiceId(String serviceId) {
        return serviceTimeMap.get(serviceId);
    }

    public Pager<ServiceMonitorItem> queryServiceMonitorItemsByPage(int start, int limit) {
        int index = 0;
        int count = serviceTimeMap.size();
        List<ServiceMonitorItem> serviceMonitorItems = new ArrayList<ServiceMonitorItem>();
        for (Map.Entry<String, Long> entry : serviceTimeMap.entrySet()) {
            if (index >= start && index < (start + limit)) {
                String serviceId = entry.getKey();
                Long totalTimes = entry.getValue();
                EventExecuteTimeInfo info = executeTimeInfoMap.get(serviceId);
                ServiceMonitorItem item = new ServiceMonitorItem();
                item.setServiceId(serviceId);
                item.setCalltimes(totalTimes);
                if (info != null) {
                    item.setSuccessTimes(info.getTimes());
                    item.setTimes0ms_10ms(info.getTimes0ms_10ms());
                    item.setTimes10ms_100ms(info.getTimes10ms_100ms());
                    item.setTimes100ms_1s(info.getTimes100ms_1s());
                    item.setTimes1s_10s(info.getTimes1s_10s());
                    item.setTimes10s_infinity(info.getTimes10s_infinity());
                }
                serviceMonitorItems.add(item);
            }
            index++;
        }

        return new Pager<ServiceMonitorItem>(count, start, serviceMonitorItems);
    }

    public ServiceMonitorItem queryServiceMonitorItemById(String serviceId) {
        Long totalTimes = serviceTimeMap.get(serviceId);
        EventExecuteTimeInfo info = executeTimeInfoMap.get(serviceId);
        ServiceMonitorItem item = new ServiceMonitorItem();
        item.setServiceId(serviceId);
        item.setCalltimes(totalTimes);
        if (info != null) {
            item.setSuccessTimes(info.getTimes());
            item.setTimes0ms_10ms(info.getTimes0ms_10ms());
            item.setTimes10ms_100ms(info.getTimes10ms_100ms());
            item.setTimes100ms_1s(info.getTimes100ms_1s());
            item.setTimes1s_10s(info.getTimes1s_10s());
            item.setTimes10s_infinity(info.getTimes10s_infinity());
        }
        return item;
    }


//    private Long getTotalValue(Map<String, Long> map) {
//        Long result = new Long(0);
//        for (Long value : map.values()) {
//            result = value + result;
//        }
//        return result;
//    }
}
