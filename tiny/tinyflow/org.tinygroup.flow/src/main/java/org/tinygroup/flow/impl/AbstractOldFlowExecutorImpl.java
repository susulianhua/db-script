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

import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.FlowExecutor;
import org.tinygroup.flow.config.ComponentDefine;
import org.tinygroup.flow.config.ComponentDefines;
import org.tinygroup.flow.config.Flow;
import org.tinygroup.flow.config.SubFlow;
import org.tinygroup.flow.containers.ComponentContainers;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractOldFlowExecutorImpl implements FlowExecutor {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractOldFlowExecutorImpl.class);
    private boolean change;
    // 组件容器
    private ComponentContainers containers = new ComponentContainers();
    private Map<String, Flow> flowIdMap = new HashMap<String, Flow>();// 包含了name和id两个，所以通过名字和id都可以访问

    public void addComponents(ComponentDefines components) {
        containers.addComponents(components);
    }

    public void removeComponents(ComponentDefines components) {
        containers.removeComponents(components);
    }

    public ComponentInterface getComponentInstance(String componentName) {
        return containers.getComponentInstance(componentName);
    }

    public void addComponent(ComponentDefine component) {
        containers.addComponent(component);
    }

    public void removeComponent(ComponentDefine component) {
        containers.removeComponent(component);
    }

    public ComponentDefine getComponentDefine(String componentName) {
        return containers.getComponentDefine(componentName);
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public List<ComponentDefine> getComponentDefines() {
        return containers.getComponentDefines();
    }

    public void assemble() {
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


    public void addSubFlow(SubFlow subFlow) {
    }

    public void removeSubFlow(SubFlow subFlow) {
    }

    public void removeSubFlow(String subFlowId) {
    }

    public SubFlow getSubFlow(String subFlowId) {
        return null;
    }
}


