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
package org.tinygroup.flowservicecomponent;

import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.FlowExecutor;

public class CallPageFlow implements ComponentInterface {
    String flowId;
    FlowExecutor executor;


    public FlowExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(FlowExecutor executor) {
        this.executor = executor;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public void execute(Context context) {
        executor.execute(flowId, null, context);
    }

    private boolean notNull(String str) {
        return str != null && !"".equals(str);
    }

}
