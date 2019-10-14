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
package org.tinygroup.flow.impl;

import org.tinygroup.flow.config.SubFlow;

import java.util.HashMap;
import java.util.Map;

public abstract class SubFlowManagerImpl extends ComponentManagerImpl {
    private Map<String, SubFlow> map = new HashMap<String, SubFlow>();

    public void assemble() {
        for (SubFlow flow : map.values()) {
            flow.assemble();
        }
    }

    public void addSubFlow(SubFlow subFlow) {
        map.put(subFlow.getId(), subFlow);
    }

    public void removeSubFlow(SubFlow subFlow) {
        removeSubFlow(subFlow.getId());
    }

    public void removeSubFlow(String subFlowId) {
        map.remove(subFlowId);
    }

    public SubFlow getSubFlow(String subFlowId) {
        return map.get(subFlowId);
    }

    public Map<String, SubFlow> getSubFlowIdMap() {
        return map;
    }
}
