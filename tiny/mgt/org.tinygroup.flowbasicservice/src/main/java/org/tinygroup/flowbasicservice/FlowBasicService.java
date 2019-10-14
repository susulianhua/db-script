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
package org.tinygroup.flowbasicservice;

import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.config.ComponentDefine;
import org.tinygroup.flow.config.Flow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlowBasicService {
    private FlowExecutor executor;

    public FlowExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(FlowExecutor executor) {
        this.executor = executor;
    }

    public List<Flow> getFlows() {
        Map<String, Flow> map = executor.getFlowIdMap();
        List<Flow> list = new ArrayList<Flow>();
        for (Flow flow : map.values()) {
            list.add(flow);
        }
        return list;
    }

    public Flow getFlow(String id) {
        return executor.getFlow(id);
    }

    public ComponentDefine getComponentDefine(String componentName) {
        return executor.getComponentDefine(componentName);
    }

    public List<ComponentDefine> getComponentDefines() {
        return executor.getComponentDefines();
    }


}
