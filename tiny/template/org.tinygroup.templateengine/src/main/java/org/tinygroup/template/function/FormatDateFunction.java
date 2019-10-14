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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 格式化日期的函数
 *
 * @author yancheng11334
 */
public class FormatDateFunction extends AbstractTemplateFunction {

    public static final String DEFAULT_PATTEN = "yyyy-MM-dd HH:mm:ss";

    public FormatDateFunction() {
        super("formatDate,formatdate");
    }

    public Object execute(Template template, TemplateContext context,
                          Object... parameters) throws TemplateException {
        if (parameters == null || parameters.length < 1
                || parameters[0] == null) {
            return "";
        } else {
            String patten = parameters.length > 1 ? (String) parameters[1] : DEFAULT_PATTEN;
            SimpleDateFormat format = new SimpleDateFormat(patten);
            return format.format(getSource(parameters[0]));
        }
    }

    private Object getSource(Object obj) {
        if (obj instanceof Long) {
            //支持时间戳
            Long time = (Long) obj;
            return new Date(time);
        } else if (obj instanceof String) {
            //支持时间戳
            Long time = Long.parseLong((String) obj);
            return new Date(time);
        } else {
            return obj;
        }
    }
}
