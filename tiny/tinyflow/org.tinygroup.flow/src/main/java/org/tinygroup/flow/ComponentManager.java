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

import org.tinygroup.flow.config.ComponentDefine;
import org.tinygroup.flow.config.ComponentDefines;

import java.util.List;

public interface ComponentManager {
    void addComponents(ComponentDefines components);

    void addComponent(ComponentDefine component);

    void removeComponents(ComponentDefines components);

    void removeComponent(ComponentDefine component);

    ComponentDefine getComponentDefine(String componentName);

    List<ComponentDefine> getComponentDefines();

    /**
     * 根据组件名称获取组件实例
     *
     * @param componentName
     * @return
     * @throws Exception
     */
    ComponentInterface getComponentInstance(String componentName)
            throws Exception;
}
