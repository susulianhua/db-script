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
package org.tinygroup.cepcoregovernance.mbean;

import org.tinygroup.cepcoregovernance.container.ExecuteTimeInfo;


public interface GovernanceMonitorMBean {

    /**
     * @description：返回异常总数
     * @author: qiucn
     * @version: 2016年6月27日下午7:36:18
     */
    public Long getExceptionTotal();

    /**
     * 返回本地服务调用次数
     *
     * @return
     */
    public Long getLocalTotalTimes();

    /**
     * 返回本地服务调用成功次数(发生业务异常也算调用失败)
     *
     * @return
     */
    public Long getLocalSuccessTimes();

    /**
     * 返回本地服务调用发生异常次数(发生业务异常也算调用失败)
     *
     * @return
     */
    public Long getLocalExceptionTimes();

    /**
     * 返回远程
     *
     * @return
     */
    public Long getRemoteTotalTimes();

    /**
     * 返回调用远程服务成功次数(发生业务异常也算调用失败)
     *
     * @return
     */
    public Long getRemoteSucessTimes();

    /**
     * 返回调用远程服务发生异常次数(发生业务异常也算调用失败)
     *
     * @return
     */
    public Long getRemoteExceptionTimes();

    /**
     * 返回具体的远程服务调用时间统计数据
     *
     * @param serviceId 服务id
     * @return
     */
    public ExecuteTimeInfo getRemoteServiceExecuteTimeInfo(String serviceId);

    /**
     * 返回具体的本地服务调用时间统计数据
     *
     * @param serviceId 服务id
     * @return
     */
    public ExecuteTimeInfo getLocalServiceExecuteTimeInfo(String serviceId);
}

	