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
package org.tinygroup.eventexecutemonitor.service.impl;

import org.tinygroup.eventexecutemonitor.counter.EventExecuteMonitor;
import org.tinygroup.eventexecutemonitor.pojo.ServiceMonitorItem;
import org.tinygroup.eventexecutemonitor.service.TinyServiceMonitor;
import org.tinygroup.tinysqldsl.Pager;

/**
 *
 * @author zhangliang08072
 * @version $Id: TinyServiceMonitorImpl.java, v 0.1 2016年12月23日 下午2:01:15 zhangliang08072 Exp $
 */
public class TinyServiceMonitorImpl implements TinyServiceMonitor {

    /**
     * @see org.tinygroup.eventexecutemonitor.service.TinyServiceMonitor#queryLocalServiceMonitorItemsByPage(int, int)
     */
    @Override
    public Pager<ServiceMonitorItem> queryLocalServiceMonitorItemsByPage(int start, int limit) {// TODO Auto-generated method stub
        return EventExecuteMonitor.queryLocalServiceMonitorItemsByPage(start, limit);
    }

    /**
     * @see org.tinygroup.eventexecutemonitor.service.TinyServiceMonitor#queryLocalServiceMonitorItemById(java.lang.String)
     */
    @Override
    public ServiceMonitorItem queryLocalServiceMonitorItemById(String serviceId) {
        return EventExecuteMonitor.queryLocalServiceMonitorItemsById(serviceId);
    }

    /**
     * @see org.tinygroup.eventexecutemonitor.service.TinyServiceMonitor#queryRemoteServiceMonitorItemsByPage(int, int)
     */
    @Override
    public Pager<ServiceMonitorItem> queryRemoteServiceMonitorItemsByPage(int start, int limit) {
        return EventExecuteMonitor.queryRemoteServiceMonitorItemsByPage(start, limit);
    }

    /**
     * @see org.tinygroup.eventexecutemonitor.service.TinyServiceMonitor#queryRemoteServiceMonitorItemById(java.lang.String)
     */
    @Override
    public ServiceMonitorItem queryRemoteServiceMonitorItemById(String serviceId) {
        return EventExecuteMonitor.queryRemoteServiceMonitorItemsById(serviceId);
    }


}
