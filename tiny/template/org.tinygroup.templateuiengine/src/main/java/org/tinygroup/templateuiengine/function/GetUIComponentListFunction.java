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

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;
import org.tinygroup.uiengine.config.UIComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 获得组件列表(默认不输或者false返回根组件列表，如果true返回叶子组件列表)
 *
 * @author yancheng11334
 */
public class GetUIComponentListFunction extends UIComponentManagerFunction {

    public GetUIComponentListFunction() {
        super("getUIList,getUIComponentList");
    }

    public Object execute(Template template, TemplateContext context,
                          Object... parameters) throws TemplateException {
        List<UIComponent> rootList = new ArrayList<UIComponent>();
        boolean tag = false;
        if (parameters != null && parameters.length > 0) {
            tag = (Boolean) parameters[0];
        }
        if (tag) {
            if (getManager().getUiComponents() != null) {
                //记录存在子孙的UI组件
                Set<String> childSets = new HashSet<String>();
                for (UIComponent component : getManager().getUiComponents()) {
                    if (!StringUtil.isEmpty(component.getDependencies())) {
                        String[] dependNames = component.getDependencies().split(",");
                        for (String dependName : dependNames) {
                            childSets.add(dependName);
                        }
                    }
                }

                //返回不存在子孙的叶子UI组件
                for (UIComponent component : getManager().getUiComponents()) {
                    if (!childSets.contains(component.getName())) {
                        rootList.add(component);
                    }
                }
            }
        } else {
            if (getManager().getUiComponents() != null) {
                for (UIComponent component : getManager().getUiComponents()) {
                    // 依赖为空表示根组件
                    if (StringUtil.isEmpty(component.getDependencies())) {
                        rootList.add(component);
                    }
                }
            }
        }

        return rootList;
    }

}
