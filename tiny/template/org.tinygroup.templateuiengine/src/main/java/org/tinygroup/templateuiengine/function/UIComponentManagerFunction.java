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
package org.tinygroup.templateuiengine.function;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.template.function.AbstractTemplateFunction;
import org.tinygroup.uiengine.config.UIComponent;
import org.tinygroup.uiengine.manager.UIComponentManager;

/**
 * 基本的UIComponentManager函数
 *
 * @author yancheng11334
 */
public abstract class UIComponentManagerFunction extends
        AbstractTemplateFunction {

    private UIComponentManager manager;

    public UIComponentManagerFunction(String names) {
        super(names);
    }

    public UIComponentManager getManager() {
        if (manager == null) {
            manager = BeanContainerFactory.getBeanContainer(
                    getClass().getClassLoader()).getBean(
                    UIComponentManager.UIComponentManager_BEAN);
        }
        return manager;
    }

    public void setManager(UIComponentManager manager) {
        this.manager = manager;
    }


    protected boolean isChild(String name, UIComponent uIComponent) {
        if (!StringUtil.isEmpty(uIComponent.getDependencies())) {
            String[] dependNames = uIComponent.getDependencies().split(",");
            for (String dependName : dependNames) {
                if (name.equals(dependName)) {
                    return true;
                }
            }
        }
        return false;
    }

}
