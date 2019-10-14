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

public class ToFloatFunction extends AbstractTemplateFunction {

    public ToFloatFunction() {
        super("toFloat,tofloat");
    }


    public Object execute(Template template, TemplateContext context,
                          Object... parameters) throws TemplateException {
        if (parameters == null || parameters.length < 1
                || parameters[0] == null) {
            throw new TemplateException("toFloat函数必须输入转换的参数");
        } else {
            if (parameters[0] instanceof Float) {
                return parameters[0];
            } else if (parameters[0] instanceof String) {
                return Float.parseFloat((String) parameters[0]);
            }
        }
        return null;
    }

}
