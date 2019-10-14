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
package org.tinygroup.weblayer.webcontext.form;

import javax.servlet.http.HttpServletRequest;

/**
 * 表单检查策略接口
 *
 * @author renhui
 */
public interface FormCheckStrategy {

    /**
     * 实施检查策略
     *
     * @param request         当前请求
     * @param isModifiedCheck 是否检查表单数据变更
     */
    void apply(HttpServletRequest request, boolean isModifiedCheck);

}