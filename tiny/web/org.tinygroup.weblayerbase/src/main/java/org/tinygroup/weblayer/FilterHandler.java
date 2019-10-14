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
package org.tinygroup.weblayer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器处理接口
 *
 * @author renhui
 */
public interface FilterHandler {
    /**
     * 获取处理上下文
     *
     * @return
     */
    public WebContext getContext();

    /**
     * 处理方法
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    public void tinyFilterProcessor(HttpServletRequest request,
                                    HttpServletResponse response) throws IOException, ServletException;

    /**
     * 头部提交，保证内容只提交一次
     *
     * @param wrapperedContext
     */
    public void commitHeaders(WebContext wrapperedContext);

    /**
     * 返回请求路径
     *
     * @return
     */
    public String getServletPath();

}
