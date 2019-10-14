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
import org.tinygroup.template.rumtime.TemplateUtil;

/**
 * Created by luoguo on 2014/6/9.
 */
public class GetFunction extends AbstractTemplateFunction {

    public GetFunction() {
        super("get");
    }

    public String getBindingTypes() {
        return "java.lang.Object";
    }

    public Object execute(Template template, TemplateContext context, Object... parameters) throws TemplateException {
        Object object = parameters[0];
        Object indexObject = parameters[1];
        try {
            //先尝试从集合取值
            return TemplateUtil.getArrayValue(object, indexObject);
        } catch (TemplateException e) {
            throw e;
        } catch (Exception e) {
            try {
                //再尝试从对象属性取值
                Object[] newParameters = new Object[parameters.length - 1];
                System.arraycopy(parameters, 1, newParameters, 0, newParameters.length);
                return TemplateUtil.executeClassMethod(object, "get", newParameters);
            } catch (Exception e1) {
                if (getTemplateEngine().isSafeVariable()) {
                    return null;
                } else {
                    throw new TemplateException(object.getClass().getName() + "的get方法取值失败");
                }
            }
        }
    }
}

