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
package org.tinygroup.cepcore;

import org.tinygroup.event.Event;
import org.tinygroup.event.ServiceInfo;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.List;

public interface CEPCore {
    String CEP_CORE_BEAN = "cepcore";


    void setEventProcessorChoose(EventProcessorChoose chooser);

    /**
     * 获取结点名称
     *
     * @return
     */
    String getNodeName();

    void setNodeName(String nodeName);

    CEPCoreOperator getOperator();

    void setOperator(CEPCoreOperator operator);

    /**
     * 注册一个事件处理器
     *
     * @param eventProcessor
     */
    void registerEventProcessor(EventProcessor eventProcessor);

    /**
     * 注销一个事件处理器
     *
     * @param eventProcessor
     */
    void unregisterEventProcessor(EventProcessor eventProcessor);

    /**
     * 处理事件
     *
     * @param event
     */
    void process(Event event);

    /**
     * 开始CEP
     */
    void start();

    /**
     * 停止CEP
     */
    void stop();

    List<EventProcessor> getEventProcessors();

    /**
     * 获取本地服务列表
     *
     * @return
     */
    List<ServiceInfo> getServiceInfos();

    int getServiceInfosVersion();

    ServiceInfo getServiceInfo(String serviceId);

    void addEventProcessorRegisterTrigger(EventProcessorRegisterTrigger trigger);

    void refreshEventProcessors();

    void setConfig(XmlNode config);

}
