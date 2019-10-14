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
 * 表单管理器，负责管理Session中的表单。
 *
 * @author renhui
 */
public interface FormManager {

    /**
     * 生成一个新的表单
     */
    public Form newForm(HttpServletRequest request);

    /**
     * 判断表单是否存在。
     */
    public boolean hasForm(HttpServletRequest request, String token);

    /**
     * 访问参数中是否存在表单Token。
     */
    public boolean hasFormToken(HttpServletRequest request);

    /**
     * 检查表单参数是否发生过变化。
     */
    public boolean isModified(HttpServletRequest request, String token);

    /**
     * 销毁一个表单
     */
    public void destroyToken(HttpServletRequest request, String token);

    /**
     * 获取表单
     *
     * @param request
     * @param formToken
     * @return
     */
    public Form getForm(HttpServletRequest request, String formToken);

    /**
     * 打印表单信息。
     */
    public String dumpForm(HttpServletRequest request, String token);

}