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
package org.tinygroup.flow.mbean;


public interface FlowMonitorMBean {

    /**
     * @description：获取本地逻辑流程服务总数
     * @author: qiucn
     * @version: 2016年6月21日上午11:15:59
     */
    public Integer getFlowServiceTotal();

    /**
     * @description：是否存在逻辑该流程服务
     * @flowId：流程服务ID
     * @author: qiucn
     * @version: 2016年6月21日上午11:17:29
     */
    public boolean isExistFlowService(String flowId);

    /**
     * @description：获取组件总数
     * @author: qiucn
     * @version: 2016年6月23日上午11:03:06
     */
    public Integer getComponentTotal();

    /**
     * @description：获取组件是否存在
     * @author: qiucn
     * @version: 2016年6月23日上午11:03:06
     */
    public boolean isExistComponent(String componentName);
}

	