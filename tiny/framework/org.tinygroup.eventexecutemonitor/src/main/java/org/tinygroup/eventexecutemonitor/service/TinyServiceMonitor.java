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
package org.tinygroup.eventexecutemonitor.service;

import org.tinygroup.eventexecutemonitor.pojo.ServiceMonitorItem;
import org.tinygroup.tinysqldsl.Pager;

/**
 * Created by zhangliang08072 on 2016/12/23.
 */
public interface TinyServiceMonitor {
    /**
     * 分页查询本地服务监控项
     * @param start
     * @param limit
     * @return
     */
    public Pager<ServiceMonitorItem> queryLocalServiceMonitorItemsByPage(int start, int limit);

    /**
     * 根据服务Id查询本地服务监控项
     * @param serviceId
     * @return
     */
    public ServiceMonitorItem queryLocalServiceMonitorItemById(String serviceId);

    /**
     * 分页查询远程服务监控项
     * @param start
     * @param limit
     * @return
     */
    public Pager<ServiceMonitorItem> queryRemoteServiceMonitorItemsByPage(int start, int limit);

    /**
     * 根据服务Id查询远程服务监控项
     * @param serviceId
     * @return
     */
    public ServiceMonitorItem queryRemoteServiceMonitorItemById(String serviceId);
}
