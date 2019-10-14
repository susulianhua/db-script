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
package org.tinygroup.springmvc.handleradapter;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;

import javax.servlet.http.HttpServletRequest;


/**
 * WebContext类型参数组装
 * @author renhui
 *
 */
public class WebContextWebArgumentResolver implements WebArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  NativeWebRequest webRequest) throws Exception {
        if (WebContext.class.isAssignableFrom(methodParameter.getParameterType())) {
            return WebContextUtil.getWebContext(webRequest.getNativeRequest(HttpServletRequest.class));
        }
        return WebArgumentResolver.UNRESOLVED;
    }

}
