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

import java.util.HashMap;
import java.util.Map;

/**
 * 对map对象的扩展处理
 *
 * @author yancheng11334
 */
public class ExtendMapFunction extends AbstractBindTemplateFunction {

    public ExtendMapFunction() {
        super("extend,extendmap", "java.util.Map");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public Object execute(Template template, TemplateContext context,
                          Object... parameters) throws TemplateException {
        if (parameters == null || parameters.length < 2) {
            throw new TemplateException("extend函数缺少参数!");
        }

        boolean mode = true;
        if (parameters.length >= 3) {
            if (parameters[2] instanceof Boolean) {
                mode = (Boolean) parameters[2];
            } else {
                throw new TemplateException("extend函数参数格式错误:第二个参数必须是布尔型");
            }
        }

        if (parameters[0] instanceof Map && parameters[1] instanceof Map) {
            //得到Map
            Map m0 = (Map) parameters[0];
            Map m1 = (Map) parameters[1];
            Map m2 = new HashMap();

            if (mode) {
                //合并m0和m1的参数
                m2.putAll(m0);
                m2.putAll(m1);
            } else {
                //用m1替换m0的相同参数的默认值
                for (Object key : m0.keySet()) {
                    Object value = m1.containsKey(key) ? m1.get(key) : m0.get(key);
                    m2.put(key, value);
                }
            }


            return m2;
        } else {
            throw new TemplateException("extend函数参数格式错误:必须都是Map类型");
        }

    }

}
