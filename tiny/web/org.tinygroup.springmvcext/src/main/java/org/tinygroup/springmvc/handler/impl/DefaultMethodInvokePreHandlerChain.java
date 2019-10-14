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

import org.tinygroup.springmvc.handler.MethodInvokePreHandler;
import org.tinygroup.springmvc.handler.MethodInvokePreHandlerChain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 默认的MethodInvokePreHandler链
 *
 * @author renhui
 */
public class DefaultMethodInvokePreHandlerChain implements
        MethodInvokePreHandlerChain {

    private List<MethodInvokePreHandler> methodInvokePreHandlers;
    private int currentPosition = 0;

    public List<MethodInvokePreHandler> getMethodInvokePreHandlers() {
        return methodInvokePreHandlers;
    }

    public void setMethodInvokePreHandlers(
            List<MethodInvokePreHandler> methodInvokePreHandlers) {
        this.methodInvokePreHandlers = methodInvokePreHandlers;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.alipay.sofa.runtime.web.smvc.servlet.handler.MethodInvokePreHandlerChain
     * #doPreMethodInvokeHandler(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.reflect.Method,
     * java.lang.Object)
     */
    public void doPreMethodInvokeHandler(HttpServletRequest request,
                                         HttpServletResponse response, Method handlerMethod, Object handler) {

        if (methodInvokePreHandlers == null
                || currentPosition > methodInvokePreHandlers.size()) {
            return;
        }
        if (currentPosition == methodInvokePreHandlers.size()) {
            return;
        } else {
            currentPosition++;
            MethodInvokePreHandler methodInvokePreHandler = methodInvokePreHandlers
                    .get(currentPosition - 1);
            methodInvokePreHandler.doPreMethodInvokeHandler(request, response,
                    handlerMethod, handler, this);
        }

    }

}