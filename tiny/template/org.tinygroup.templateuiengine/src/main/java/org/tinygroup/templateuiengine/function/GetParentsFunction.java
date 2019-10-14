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
import java.util.List;

/**
 * 获得某个UI组件的父节点列表(依赖列表)
 *
 * @author yancheng11334
 */
public class GetParentsFunction extends UIComponentManagerFunction {

    public GetParentsFunction() {
        super("getParents");
    }

    public String getBindingTypes() {
        return "org.tinygroup.uiengine.config.UIComponent";
    }

    public Object execute(Template template, TemplateContext context,
                          Object... parameters) throws TemplateException {
        UIComponent component = (UIComponent) parameters[0];

        List<UIComponent> list = new ArrayList<UIComponent>();
        if (!StringUtil.isEmpty(component.getDependencies())) {
            String[] dependencies = component.getDependencies().split(",");
            for (String dependName : dependencies) {
                UIComponent uIComponent = getManager().getUIComponent(dependName);
                if (uIComponent != null) {
                    list.add(uIComponent);
                }
            }
        }
        return list;
    }

}
