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
package org.tinygroup.template.function.escape;

import org.apache.commons.lang.StringEscapeUtils;
import org.tinygroup.template.TemplateException;

/**
 * 默认转义html函数(底层通过StringEscapeUtils实现)
 * @author yancheng11334
 *
 */
public class EscapeHtmlFunction extends AbstractEscapeFunction {

    public EscapeHtmlFunction() {
        super("escapeHtml");
    }

    protected String replace(String content) throws TemplateException {
        try {
            return StringEscapeUtils.escapeHtml(content);
        } catch (Exception e) {
            throw new TemplateException(e);
        }

    }
}
