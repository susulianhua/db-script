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

import org.tinygroup.commons.file.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TextServlet extends AbstractMockServlet {

    /**
     *
     */
    private static final long serialVersionUID = -5863574058246844016L;

    protected void dealService(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        try {

//			for (Enumeration<String> names = request.getHeaderNames(); names.hasMoreElements();){
//				String name = names.nextElement();
//				System.out.println(name+" "+request.getHeader(name));
//			}
            //需要注意文件编码和传输编码的区别
            String text = IOUtils.readFromInputStream(request.getInputStream(), "ISO-8859-1");
            PrintWriter out = null;
            // 默认处理
            out = response.getWriter();
            out.write(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
