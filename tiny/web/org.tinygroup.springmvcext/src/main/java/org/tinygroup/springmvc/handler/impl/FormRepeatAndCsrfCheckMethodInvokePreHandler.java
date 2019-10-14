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
package org.tinygroup.springmvc.handler.impl;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.tinygroup.springmvc.handler.Form;
import org.tinygroup.springmvc.handler.MethodInvokePreHandler;
import org.tinygroup.springmvc.handler.MethodInvokePreHandlerChain;
import org.tinygroup.weblayer.webcontext.form.FormCheckStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 表单重复提交和CSRF检查
 *
 * @author renhui
 */
public class FormRepeatAndCsrfCheckMethodInvokePreHandler implements
        MethodInvokePreHandler, Ordered {

    private FormCheckStrategy formCheckStrategy;

    public FormCheckStrategy getFormCheckStrategy() {
        return formCheckStrategy;
    }

    public void setFormCheckStrategy(FormCheckStrategy formCheckStrategy) {
        this.formCheckStrategy = formCheckStrategy;
    }

    public void doPreMethodInvokeHandler(HttpServletRequest request,
                                         HttpServletResponse response, Method handlerMethod, Object handler,
                                         MethodInvokePreHandlerChain handlerChain) {

        Form form = AnnotationUtils.findAnnotation(handlerMethod, Form.class);
        // 重复提交检测
        if (form != null && formCheckStrategy != null
                && (form.isCsrfCheck() || form.isRepeatCheck())) {
            formCheckStrategy.apply(request, form.isModifiedCheck());

            if (!(form.isRepeatCheck())) {
                request.setAttribute(
                        org.tinygroup.weblayer.webcontext.form.Form.FORM_FORBID_DUP_SUBMIT, "no");
            }
        }
        handlerChain.doPreMethodInvokeHandler(request, response, handlerMethod,
                handler);
    }


    /**
     * @see org.springframework.core.Ordered#getOrder()
     */
    public int getOrder() {
        return 200;
    }
}