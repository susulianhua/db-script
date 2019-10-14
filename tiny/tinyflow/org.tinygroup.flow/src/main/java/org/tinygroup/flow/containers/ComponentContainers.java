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
package org.tinygroup.flow.containers;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flow.config.ComponentDefine;
import org.tinygroup.flow.config.ComponentDefines;
import org.tinygroup.flow.exception.FlowRuntimeException;
import org.tinygroup.flow.exception.errorcode.FlowExceptionErrorCode;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组件容器
 *
 * @author renhui
 */
public class ComponentContainers {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ComponentContainers.class);
    /**
     * key=name
     */
    private Map<String, ComponentDefine> nameMap = new HashMap<String, ComponentDefine>();

    /**
     * 增加组件信息
     */
    public void addComponents(ComponentDefines components) {

        for (ComponentDefine component : components.getComponentDefines()) {
            addComponent(component);
        }

    }

    public void removeComponents(ComponentDefines components) {
        for (ComponentDefine component : components.getComponentDefines()) {
            removeComponent(component);
        }

    }

    public void removeComponent(ComponentDefine component) {
        if (component == null)
            return;
        LOGGER.logMessage(LogLevel.INFO, "移除组件Component[name:{0},bean:{1}]",
                component.getName(), component.getBean());
        nameMap.remove(component.getName());
    }

    /**
     * 根据流程组件名称得到组件实例
     *
     * @param componentName 流程组件名称
     * @return
     */
    public ComponentInterface getComponentInstance(String componentName) {

        if (nameMap.get(componentName) != null) {
            // 20121127获取bean的源头从simpleFactory修改为spring
            return (ComponentInterface) BeanContainerFactory.getBeanContainer(
                    this.getClass().getClassLoader()).getBean(
                    nameMap.get(componentName).getBean());
        }
        LOGGER.logMessage(LogLevel.ERROR, "组件:{0}找不到", componentName);
        throw new FlowRuntimeException(FlowExceptionErrorCode.FLOW_COMPONENT_NOT_EXIST, componentName);
    }

    public void addComponent(ComponentDefine component) {
        if (component == null)
            return;
        LOGGER.logMessage(LogLevel.INFO, "添加组件Component[name:{0},bean:{1}]",
                component.getName(), component.getBean());
        nameMap.put(component.getName(), component);
    }

    public ComponentDefine getComponentDefine(String componentName) {
        return nameMap.get(componentName);
    }

    public List<ComponentDefine> getComponentDefines() {
        return new ArrayList<ComponentDefine>(nameMap.values());
    }
}
