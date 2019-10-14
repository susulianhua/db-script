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
package org.tinygroup.template.function;

import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateException;

/**
 * 指定渲染层数
 *
 * @author yancheng11334
 */
public class RenderLayerFunction extends AbstractTemplateFunction {

    public RenderLayerFunction() {
        super("renderLayer");
    }

    public Object execute(Template template, TemplateContext context,
                          Object... parameters) throws TemplateException {
        if (parameters == null || parameters.length < 1
                || parameters[0] == null) {
            throw new TemplateException("渲染布局层数必须输入限定层数");
        } else {
            if (parameters[0] instanceof Integer) {
                context.put("$renderLayer", (Integer) parameters[0]);
            } else if (parameters[0] instanceof String) {
                context.put("$renderLayer", Integer.valueOf((String) parameters[0]));
            }
        }
        return null;
    }

}
