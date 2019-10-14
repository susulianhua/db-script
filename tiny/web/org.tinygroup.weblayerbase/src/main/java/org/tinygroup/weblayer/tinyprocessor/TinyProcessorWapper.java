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
package org.tinygroup.weblayer.tinyprocessor;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.AbstractTinyProcessor;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.impl.TinyServletConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 把Servlet包装成TinyProcessor
 *
 * @author luoguo
 */
public class TinyProcessorWapper extends AbstractTinyProcessor {
    private static final Logger logger = LoggerFactory
            .getLogger(TinyProcessorWapper.class);
    private HttpServlet servlet;

    public void reallyProcess(String urlString, WebContext context)
            throws ServletException, IOException {

        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();
        servlet.service(request, response);
    }

    public void destroy() {
        super.destroy();
        servlet.destroy();
    }

    @Override
    protected void customInit() throws ServletException {
        String servletBeanName = get(TinyServletConfig.SERVLET_BEAN);
        if (StringUtil.isBlank(servletBeanName)) {
            logger.logMessage(LogLevel.ERROR,
                    "servlet_bean attribute value must not  be empty");
            throw new RuntimeException(
                    "servlet_bean attribute value must not be empty");
        }
        servlet = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(servletBeanName);
        if (servlet != null) {
            TinyServletConfig servletConfig = new TinyServletConfig();
            servletConfig.setInitParams(getInitParamMap());
            servletConfig.setServletConfig(servlet.getServletConfig());
            servlet.init(servletConfig);
        }
    }

}
