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
package org.tinygroup.weblayer.webcontext;

import org.tinygroup.weblayer.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import static org.tinygroup.commons.tools.Assert.assertNotNull;


/**
 * 被<code>WebContext</code>支持的<code>HttpServletRequestWrapper</code>。
 *
 * @author renhui
 */
public abstract class AbstractRequestWrapper extends HttpServletRequestWrapper {
    private WebContext context;

    /**
     * 创建一个request wrapper。
     *
     * @param context request context
     * @param request request
     */
    public AbstractRequestWrapper(WebContext context, HttpServletRequest request) {
        super(request);

        this.context = assertNotNull(context, "requestContext");
    }

    /**
     * 取得当前request所处的servlet context环境。
     *
     * @return <code>ServletContext</code>对象
     */
    public ServletContext getServletContext() {
        return getWebContext().getServletContext();
    }

    /**
     * 取得支持它们的<code>WebContext</code>。
     *
     * @return <code>WebContext</code>实例
     */
    protected WebContext getWebContext() {
        return context;
    }
}
