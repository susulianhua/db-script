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

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.webcontext.form.Form;
import org.tinygroup.weblayer.webcontext.form.FormCheckStrategy;
import org.tinygroup.weblayer.webcontext.form.FormManager;
import org.tinygroup.weblayer.webcontext.form.exception.DuplicateFormSubmitException;
import org.tinygroup.weblayer.webcontext.form.exception.FormDataJuggledException;

import javax.servlet.http.HttpServletRequest;

/**
 * 抽像的表单检查策略定义.
 *
 * @author renhui
 */
public abstract class AbstractFormCheckStrategy implements FormCheckStrategy {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractFormCheckStrategy.class);
    private FormManager formManager;

    public void apply(HttpServletRequest request, boolean isModifiedCheck) {
        String formToken = getFormToken(request);
        // 如果token不存在，是否还继续检查
        if (!applyIfTokenValue(formToken)) {
            return;
        }

        // 检查表单是否存在，如果不存在，说明该表单已经提交过了或者过期了。
        if (!formManager.hasForm(request, formToken)) {
            throw new DuplicateFormSubmitException("表单重复提交，令牌[" + formToken
                    + "]");
        }

        if (isModifiedCheck) {
            String uri = request.getRequestURI().toString();
            Form formImpl = formManager.getForm(request, formToken);
            if (!uri.equalsIgnoreCase(formImpl.getUrl())) {
                throw new DuplicateFormSubmitException("关键属性被篡改，令牌["
                        + formToken + "]");
            }
        }

        // 如果表单数据发生改变，那么记日志，抛异常。
        if (formManager.isModified(request, formToken)) {
            LOGGER.logMessage(LogLevel.WARN, "表单:[{0}]的数据被篡改了，本次请求参数：{1}",
                    formManager.dumpForm(request, formToken),
                    request.getQueryString());
            throw new FormDataJuggledException("表单数据被篡改了");
        }

    }

    /**
     * 获取token
     *
     * @param request
     * @return
     */
    protected String getFormToken(HttpServletRequest request) {
        String formToken = request.getParameter(Form.FORM_TOKEN_FIELD_NAME);
        if (formToken == null) {
            formToken = (String) request
                    .getAttribute(Form.FORM_TOKEN_FIELD_NAME);
        }
        return formToken;
    }

    /**
     * 根据判断token值来决定是否应用检查规则
     *
     * @param formToken
     * @return true 应用检查规则
     */
    protected abstract boolean applyIfTokenValue(String formToken);

    public void setFormManager(FormManager formManager) {
        this.formManager = formManager;
    }

}