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

import org.tinygroup.flow.config.Flow;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class FlowManagerImpl extends SubFlowManagerImpl {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FlowManagerImpl.class);
    private Map<String, Flow> flowIdMap = new HashMap<String, Flow>();// 包含了name和id两个，所以通过名字和id都可以访问

    public void assemble() {
        super.assemble();
        for (Flow flow : flowIdMap.values()) {
            flow.validate();
            flow.assemble();
        }
    }

    public void addFlow(Flow flow) {
        if (flow.getId() != null && flowIdMap.get(flow.getId()) != null) {
            LOGGER.logMessage(LogLevel.ERROR, "flow:[id:{0}]已经存在！",
                    flow.getId());
        }
        if (flow.getName() != null && flowIdMap.get(flow.getName()) != null) {
            LOGGER.logMessage(LogLevel.ERROR, "flow:[name:{0}]已经存在！",
                    flow.getName());
        }
        if (flow.getId() != null) {
            LOGGER.logMessage(LogLevel.INFO, "添加flow:[id:{0}]", flow.getId());
            flowIdMap.put(flow.getId(), flow);
        }
        if (flow.getName() != null) {
            LOGGER.logMessage(LogLevel.INFO, "添加flow:[Name:{0}]",
                    flow.getName());
            flowIdMap.put(flow.getName(), flow);
        }
        flow.setFlowExecutor(this);
        setChange(true);
    }

    public void removeFlow(Flow flow) {
        LOGGER.logMessage(LogLevel.INFO, "移除flow:[id:{0}]", flow.getId());
        flowIdMap.remove(flow.getId());
        LOGGER.logMessage(LogLevel.INFO, "移除flow:[name:{0}]", flow.getName());
        flowIdMap.remove(flow.getName());
        setChange(true);
    }

    public void removeFlow(String flowId) {
        Flow flow = getFlow(flowId);
        removeFlow(flow);
    }

    public Flow getFlow(String flowId) {
        return flowIdMap.get(flowId);
    }

    public Map<String, Flow> getFlowIdMap() {
        return flowIdMap;
    }
}
