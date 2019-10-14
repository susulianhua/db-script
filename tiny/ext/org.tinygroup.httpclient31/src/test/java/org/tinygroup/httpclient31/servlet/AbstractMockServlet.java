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
package org.tinygroup.httpclient31.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 测试的基本类
 *
 * @author yancheng11334
 */
public abstract class AbstractMockServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        dealService(request, response);
    }

    protected void doHead(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        dealService(request, response);
    }

    protected void doDelete(HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException {
        dealService(request, response);
    }

    protected void doOptions(HttpServletRequest request,
                             HttpServletResponse response) throws ServletException, IOException {
        dealService(request, response);
    }

    protected void doTrace(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        dealService(request, response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        dealService(request, response);
    }

    protected void doPut(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        dealService(request, response);
    }

    /**
     * 抽象的服务逻辑
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected abstract void dealService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
