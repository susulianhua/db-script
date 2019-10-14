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
package org.tinygroup.weblayer.webcontext.form.impl;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.weblayer.webcontext.form.exception.FormDataJuggledException;

/**
 * 严格的表单检查策略，不提供form token就报错
 *
 * @author renhui
 */
public class CommonStrictFormCheckStrategy extends AbstractFormCheckStrategy {

    @Override
    protected boolean applyIfTokenValue(String formToken) {
        if (StringUtil.isBlank(formToken)) {
            throw new FormDataJuggledException("表单数据可能被篡改了,form token不存在。");
        }
        return true;
    }

}