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
 * 测试GBK编码
 *
 * @author yancheng11334
 */
public class GBKServlet extends AbstractMockServlet {

    /**
     *
     */
    private static final long serialVersionUID = -5415009275609653520L;

    protected void dealService(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("GBK");
        PrintWriter out = response.getWriter();
        out.write("中文,GBK");
    }

}
