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
package org.tinygroup.eventexecutemonitor.counter;

import org.tinygroup.event.Event;
import org.tinygroup.eventexecutemonitor.container.EventExecuteInfoContainer;
import org.tinygroup.eventexecutemonitor.container.EventExecuteTimeInfo;
import org.tinygroup.eventexecutemonitor.pojo.ServiceMonitorItem;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinysqldsl.Pager;

/**
 * Created by zhangliang08072 on 2016/12/21.
 */
public class EventExecuteMonitor {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventExecuteMonitor.class);

    public static EventExecuteInfoContainer localContainer = new EventExecuteInfoContainer();
    public static EventExecuteInfoContainer remoteContainer = new EventExecuteInfoContainer();

    //同步
    protected static void addLocalExecuteBeforeSyn(Event e) {
        localContainer.addExecuteBeforeSyn(e);
    }

    protected static void addLocalExecuteAfterSyn(Event e) {
        localContainer.addExecuteAfterSyn(e);
    }

    protected static void addRemoteExecuteBeforeSyn(Event e) {
        remoteContainer.addExecuteBeforeSyn(e);
    }

    protected static void addRemoteExecuteAfterSyn(Event e) {
        remoteContainer.addExecuteAfterSyn(e);
    }

    //异步
    protected static void addLocalExecuteBeforeAsyn(Event e) {
        localContainer.addExecuteBeforeAsyn(e);
    }

    protected static void addLocalExecuteAfterAsyn(Event e) {
        localContainer.addExecuteAfterAsyn(e);
    }

    protected static void addRemoteExecuteBeforeAsyn(Event e) {
        remoteContainer.addExecuteBeforeAsyn(e);
    }

    protected static void addRemoteExecuteAfterAsyn(Event e) {
        remoteContainer.addExecuteAfterAsyn(e);
    }

    /**
     * 返回本地服务调用次数
     *
     * @return
     */
    public static Long getLocalTotalTimes(String serviceId) {
        return localContainer.getTotalTimesByServiceId(serviceId);
    }


    /**
     * 返回远程
     *
     * @return
     */
    public static Long getRemoteTotalTimes(String serviceId) {
        return remoteContainer.getTotalTimesByServiceId(serviceId);
    }

    /**
     * 返回具体的远程服务调用时间统计数据
     *
     * @param serviceId 服务id
     * @return
     */
    public static EventExecuteTimeInfo getRemoteServiceExecuteTimeInfo(String serviceId) {
        return remoteContainer.getServiceExecuteTimeInfo(serviceId);
    }

    /**
     * 返回具体的本地服务调用时间统计数据
     *
     * @param serviceId 服务id
     * @return
     */
    public static EventExecuteTimeInfo getLocalServiceExecuteTimeInfo(String serviceId) {
        return localContainer.getServiceExecuteTimeInfo(serviceId);
    }

    public static Pager<ServiceMonitorItem> queryLocalServiceMonitorItemsByPage(int start, int limit) {
        return localContainer.queryServiceMonitorItemsByPage(start, limit);
    }

    public static Pager<ServiceMonitorItem> queryRemoteServiceMonitorItemsByPage(int start, int limit) {
        return remoteContainer.queryServiceMonitorItemsByPage(start, limit);
    }

    public static ServiceMonitorItem queryLocalServiceMonitorItemsById(String serviceId) {
        return localContainer.queryServiceMonitorItemById(serviceId);
    }

    public static ServiceMonitorItem queryRemoteServiceMonitorItemsById(String serviceId) {
        return remoteContainer.queryServiceMonitorItemById(serviceId);
    }
}
