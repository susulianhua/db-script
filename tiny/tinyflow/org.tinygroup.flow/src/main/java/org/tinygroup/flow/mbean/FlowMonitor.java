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

import org.tinygroup.flow.FlowExecutor;

public class FlowMonitor implements FlowMonitorMBean {

    FlowExecutor flowExecutor;

    public FlowExecutor getFlowExecutor() {
        return flowExecutor;
    }

    public void setFlowExecutor(FlowExecutor flowExecutor) {
        this.flowExecutor = flowExecutor;
    }

    public Integer getFlowServiceTotal() {
        return flowExecutor.getFlowIdMap().size();
    }

    public boolean isExistFlowService(String flowId) {
        if (flowExecutor.getFlow(flowId) != null) {
            return true;
        }
        return false;
    }

    public Integer getComponentTotal() {
        return flowExecutor.getComponentDefines().size();
    }

    public boolean isExistComponent(String componentName) {
        if (flowExecutor.getComponentDefine(componentName) != null) {
            return true;
        }
        return false;
    }

}

	