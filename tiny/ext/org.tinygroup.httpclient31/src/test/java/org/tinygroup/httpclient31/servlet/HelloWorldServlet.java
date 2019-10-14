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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 基本的HelloWorld
 *
 * @author yancheng11334
 */
public class HelloWorldServlet extends AbstractMockServlet {

    /**
     *
     */
    private static final long serialVersionUID = 3898906063455512009L;

    protected void dealService(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = null;
        // 默认处理
        out = response.getWriter();
        out.write("hello world");
    }

}
