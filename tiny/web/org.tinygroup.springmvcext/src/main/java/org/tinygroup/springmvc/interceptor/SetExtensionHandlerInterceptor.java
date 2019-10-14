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
package org.tinygroup.springmvc.interceptor;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;
import org.tinygroup.springmvc.extension.RequestInstanceHolder;
import org.tinygroup.springmvc.util.WebUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设置请求扩展名的拦截器
 * @author renhui
 *
 */
public class SetExtensionHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        String requestUri = urlPathHelper.getRequestUri(request);
        RequestInstanceHolder.setServletWebRequest(new ServletWebRequest(
                request, response));
        RequestInstanceHolder.setExtension(WebUtil.getExtension(requestUri));
        return true;
    }

}
