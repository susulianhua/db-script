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
package org.tinygroup.cepcoregovernance;

import org.tinygroup.cepcoregovernance.container.CommonExecuteInfoContainer;
import org.tinygroup.cepcoregovernance.container.ExecuteTimeInfo;
import org.tinygroup.event.Event;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class CommonServiceExecuteContainer {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SpecialServiceExecuteContainer.class);
    private static CommonExecuteInfoContainer localContainer = new CommonExecuteInfoContainer();
    private static CommonExecuteInfoContainer remoteContainer = new CommonExecuteInfoContainer();

    protected static void addLocalExecuteBefore(Event e) {
        localContainer.addExecuteBefore(e);
    }

    protected static void addLocalExecuteAfter(Event e) {
        localContainer.addExecuteAfter(e);
    }

    protected static void addRemoteExecuteBefore(Event e) {
        remoteContainer.addExecuteBefore(e);
    }

    protected static void addRemoteExecuteAfter(Event e) {
        remoteContainer.addExecuteAfter(e);
    }

    protected static void addExecuteException(Event e, Exception t) {
        if (localContainer.contain(e.getEventId())) {
            localContainer.addExecuteException(e, t);
        } else if (remoteContainer.contain(e.getEventId())) {
            remoteContainer.addExecuteException(e, t);
        } else {
            LOGGER.errorMessage("事件:" + e.getEventId() + ",服务:"
                    + e.getServiceRequest().getServiceId()
                    + ",在本地/远程列表中不存在");
        }
    }

    /**
     * 返回本地服务调用次数
     *
     * @return
     */
    public static Long getLocalTotalTimes() {
        return localContainer.getTotalTimes();
    }

    /**
     * 返回本地服务调用成功次数(发生业务异常也算调用失败)
     *
     * @return
     */
    @Deprecated
    public static Long getLocalSucessTimes() {
        return getLocalSuccessTimes();
    }

    /**
     * 返回本地服务调用成功次数(发生业务异常也算调用失败)
     *
     * @return
     */
    public static Long getLocalSuccessTimes() {
        return localContainer.getSuccessTimes();
    }

    /**
     * 返回本地服务调用发生异常次数(发生业务异常也算调用失败)
     *
     * @return
     */
    public static Long getLocalExceptionTimes() {
        return localContainer.getExceptionTimes();
    }

    /**
     * 返回远程
     *
     * @return
     */
    public static Long getRemoteTotalTimes() {
        return remoteContainer.getTotalTimes();
    }

    /**
     * 返回调用远程服务成功次数(发生业务异常也算调用失败)
     *
     * @return
     */
    public static Long getRemoteSucessTimes() {
        return remoteContainer.getSucessTimes();
    }

    /**
     * 返回调用远程服务发生异常次数(发生业务异常也算调用失败)
     *
     * @return
     */
    public static Long getRemoteExceptionTimes() {
        return remoteContainer.getExceptionTimes();
    }

    /**
     * 返回具体的远程服务调用时间统计数据
     *
     * @param serviceId 服务id
     * @return
     */
    public static ExecuteTimeInfo getRemoteServiceExecuteTimeInfo(String serviceId) {
        return remoteContainer.getServiceExecuteTimeInfo(serviceId);
    }

    /**
     * 返回具体的本地服务调用时间统计数据
     *
     * @param serviceId 服务id
     * @return
     */
    public static ExecuteTimeInfo getLocalServiceExecuteTimeInfo(String serviceId) {
        return localContainer.getServiceExecuteTimeInfo(serviceId);
    }


}

