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
package org.tinygroup.uiengine.manager;

import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.config.UIComponents;

import java.util.List;

/**
 * 界面组件管理器
 *
 * @author luoguo
 */
public interface UIComponentManager {
    String UIComponentManager_BEAN = "uiComponentManager";
    String UIComponentManager_XSTREAM = "uicomponents";

    /**
     * 添加界面组件
     *
     * @param uiComponents
     */
    void addUIComponents(UIComponents uiComponents);

    /**
     * 返回界面组件
     *
     * @return
     */
    List<UIComponent> getUiComponents();

    /**
     * 根据名称返回界面组件
     *
     * @param name
     * @return
     */
    UIComponent getUIComponent(String name);

    /**
     * 是否健康，如果依赖的包都存在，则是健康的
     *
     * @param name
     * @return
     */
    boolean isHealth(String name);

    /**
     * 返回所有的健康组件列表
     *
     * @return
     */
    List<UIComponent> getHealthUiComponents();

    /**
     * 返回JSPath
     *
     * @param component 返回JS类型
     * @return
     */
    String[] getComponentJsArray(UIComponent component);

    /**
     * 返回CssPath
     *
     * @param component
     * @return
     */
    String[] getComponentCssArray(UIComponent component);

    /**
     * 移除组件
     *
     * @param uiComponents
     */
    void removeUIComponents(UIComponents uiComponents);

    /**
     * 计算组件的依赖关系
     */
    void compute();

    /**
     * 重置组件的依赖关系
     */
    void reset();
}
