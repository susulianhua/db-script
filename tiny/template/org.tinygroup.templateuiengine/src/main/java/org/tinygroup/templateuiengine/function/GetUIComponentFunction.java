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

import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;

/**
 * 根据name属性获得UI组件
 *
 * @author yancheng11334
 */
public class GetUIComponentFunction extends UIComponentManagerFunction {

    public GetUIComponentFunction() {
        super("getUI,getUIComponent");
    }

    public Object execute(Template template, TemplateContext context,
                          Object... parameters) throws TemplateException {
        String name = (String) parameters[0];
        return getManager().getUIComponent(name);
    }

}
