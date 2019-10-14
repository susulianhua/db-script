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
package org.tinygroup.flow;

import org.tinygroup.flow.config.Flow;

import java.util.Map;

public interface FlowManager extends SubFlowManager {

    void addFlow(Flow flow);

    void removeFlow(Flow flow);

    void removeFlow(String flowId);

    Flow getFlow(String flowId);

    /**
     * 组装flow，主要用于处理继承关系
     */
    void assemble();

    /**
     * 返回流程Map
     *
     * @return
     */
    Map<String, Flow> getFlowIdMap();
}
