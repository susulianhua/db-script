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
package org.tinygroup.templateweb;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateEngine;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TinyTempalte Servlet Created by luoguo on 2014/7/14.
 */
public class TinyTemplateServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory
            .getLogger(TinyTemplateServlet.class);
    private static transient TemplateEngine engine;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        engine = BeanContainerFactory.getBeanContainer(
                TinyTemplateServlet.class.getClassLoader()).getBean(
                "templateEngine");
    }

    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        try {
            long startTime = System.currentTimeMillis();
            String servletPath = request.getServletPath();
            if (servletPath == null || servletPath.length() == 0) {
                servletPath = request.getPathInfo();
            }
            TemplateContext context = new RequestTemplateContext(request);
            engine.renderTemplate(servletPath, context, response.getOutputStream());
            long endTime = System.currentTimeMillis();
            logger.logMessage(LogLevel.INFO, "模板路径<{0}>的处理时间：{1}ms",
                    servletPath, endTime - startTime);
        } catch (Exception e) {
            logger.errorMessage(e.getMessage(), e);
            throw new ServletException(e);
        }
    }
}
