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
package org.tinygroup.weblayer.webcontext.basic.interceptor;

import javax.servlet.http.Cookie;

/**
 * 检查cookie
 *
 * @author renhui
 */
public interface CookieInterceptor extends ResponseHeaderInterceptor {
    /**
     * 检查cookie。
     *
     * @return 返回值表示修改cookie，返回<code>null</code>则表示拒绝该cookie。 特别支持
     * <code>CookieSupport</code>类。
     */
    Cookie checkCookie(Cookie cookie);
}
