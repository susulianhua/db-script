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
import org.tinygroup.flow.containers.ComponentContainers;

import java.util.List;

public abstract class ComponentManagerImpl implements FlowExecutor {
    // 组件容器
    private ComponentContainers containers = new ComponentContainers();

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

    public List<ComponentDefine> getComponentDefines() {
        return containers.getComponentDefines();
    }
}
