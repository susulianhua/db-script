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
package org.tinygroup.weblayer2;

import org.springframework.beans.*;
import org.springframework.util.StringUtils;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.TinyProcessorManager;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.parser.ParserWebContext;
import org.tinygroup.weblayer.webcontext.util.QueryStringParser;
import org.tinygroup.weblayer.webcontext.util.WebContextUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Enumeration;

import static org.tinygroup.weblayer.webcontext.parser.ParserWebContext.DEFAULT_CHARSET_ENCODING;

/**
 * weblayer集成的HttpServlet，内部封装多个TinyProcessor,委派处理器去执行
 *
 * @author renhui
 */
public class TinyHttpServlet extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TinyHttpServlet.class);
    private static final String SEARCH_STR = "?";
    private TinyProcessorManager tinyProcessorManager;

    /** Should we dispatch an HTTP OPTIONS request to {@link #doService}? */
    private boolean dispatchOptionsRequest = false;

    /** Should we dispatch an HTTP TRACE request to {@link #doService}? */
    private boolean dispatchTraceRequest = false;

    public void setDispatchOptionsRequest(boolean dispatchOptionsRequest) {
        this.dispatchOptionsRequest = dispatchOptionsRequest;
    }

    public void setDispatchTraceRequest(boolean dispatchTraceRequest) {
        this.dispatchTraceRequest = dispatchTraceRequest;
    }

    @Override
    protected void service(HttpServletRequest request,
                           HttpServletResponse response) throws ServletException, IOException {
        HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
        if (HttpMethod.PATCH == httpMethod || httpMethod == null) {
            processRequest(request, response);
        } else {
            super.service(request, response);
        }
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response) throws ServletException, IOException {
        WebContext webContext = WebContextUtil.getWebContext(request);
        String servletPath = getServletPath(request, webContext);
        if (!tinyProcessorManager.execute(servletPath, webContext)) {
            throw new ServletException(String.format(
                    "未找到请求路径：%s，对应的TinyProcessor", servletPath));
        }
    }

    private String getServletPath(HttpServletRequest request,
                                  WebContext webContext) {
        String servletPath = (String) request
                .getAttribute(TinyHttpFilter.DEFAULT_PAGE_KEY);
        if (StringUtil.isBlank(servletPath)) {
            servletPath = request.getServletPath();
            if (StringUtil.isBlank(servletPath)) {
                servletPath = request.getPathInfo();
            }
            if (StringUtil.isBlank(servletPath)) {// 兼容tomcat8的处理情况，如果servletPath为空，设置为"/"
                servletPath = "/";
            }
        }
        if (StringUtil.contains(servletPath, SEARCH_STR)) {
            parserRequestPath(servletPath, request, webContext);
        }
        return StringUtil.substringBefore(servletPath, SEARCH_STR);
    }

    /**
     * 解析请求路径servletPath的参数信息，把参数信息存放在上下文中
     *
     * @param request
     * @param servletPath
     */
    private void parserRequestPath(String servletPath,
                                   HttpServletRequest request, final WebContext webContext) {
        ParserWebContext requestContext = WebContextUtil.findWebContext(
                request, ParserWebContext.class);
        String charset = requestContext.isUseBodyEncodingForURI() ? request
                .getCharacterEncoding() : requestContext.getURIEncoding();
        QueryStringParser parser = new QueryStringParser(charset,
                DEFAULT_CHARSET_ENCODING) {
            protected void add(String key, String value) {
                webContext.put(key, value);
            }
        };
        parser.parse(StringUtil.substringAfter(servletPath, SEARCH_STR));
    }

    public void init(ServletConfig filterConfig) throws ServletException {
        LOGGER.logMessage(LogLevel.INFO, "tinyHttpServlet初始化开始...");
        super.init(filterConfig);
        // Set bean properties from init parameters.
        try {
            PropertyValues pvs = new ServletConfigPropertyValues(
                    getServletConfig());
            BeanWrapper bw = PropertyAccessorFactory
                    .forBeanPropertyAccess(this);
            initBeanWrapper(bw);
            bw.setPropertyValues(pvs, true);
        } catch (BeansException ex) {
            LOGGER.logMessage(LogLevel.ERROR,
                    "Failed to set bean properties on servlet {0}", ex,
                    getServletName());
            throw ex;
        }
        initTinyProcessors();
        LOGGER.logMessage(LogLevel.INFO, "tinyHttpServlet初始化结束...");
    }

    /**
     * Initialize the BeanWrapper for this HttpServletBean, possibly with custom
     * editors.
     * <p>
     * This default implementation is empty.
     *
     * @param bw
     *            the BeanWrapper to initialize
     * @throws BeansException
     *             if thrown by BeanWrapper methods
     * @see org.springframework.beans.BeanWrapper#registerCustomEditor
     */
    protected void initBeanWrapper(BeanWrapper bw) throws BeansException {
    }

    /**
     * tiny-processors初始化
     *
     * @throws IOException
     * @throws ServletException
     */
    private void initTinyProcessors() throws ServletException {
        tinyProcessorManager = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader()).getBean(
                TinyProcessorManager.TINY_PROCESSOR_MANAGER);
        tinyProcessorManager.initTinyResources();
    }

    /**
     * 销毁tiny-processors
     */
    private void destroyTinyProcessors() {
        tinyProcessorManager.destoryTinyResources();
    }

    public void destroy() {
        destroyTinyProcessors();
    }

    @Override
    protected final void doGet(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected final void doPost(HttpServletRequest request,
                                HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected final void doPut(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected final void doDelete(HttpServletRequest request,
                                  HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected final void doOptions(HttpServletRequest request,
                                   HttpServletResponse response) throws ServletException, IOException {
        if (this.dispatchOptionsRequest
                || CorsUtils.isPreFlightRequest(request)) {
            processRequest(request, response);
            if (response.containsHeader("Allow")) {
                // Proper OPTIONS response coming from a handler - we're done.
                return;
            }
        }
        // Use response wrapper for Servlet 2.5 compatibility where
        // the getHeader() method does not exist
        super.doOptions(request, new HttpServletResponseWrapper(response) {
            @Override
            public void setHeader(String name, String value) {
                if ("Allow".equals(name)) {
                    value = (StringUtils.hasLength(value) ? value + ", " : "")
                            + HttpMethod.PATCH.name();
                }
                super.setHeader(name, value);
            }
        });
    }

    @Override
    protected final void doTrace(HttpServletRequest request,
                                 HttpServletResponse response) throws ServletException, IOException {
        if (this.dispatchTraceRequest) {
            processRequest(request, response);
            if ("message/http".equals(response.getContentType())) {
                // Proper TRACE response coming from a handler - we're done.
                return;
            }
        }
        super.doTrace(request, response);
    }

    /**
     * PropertyValues implementation created from ServletConfig init parameters.
     */
    private static class ServletConfigPropertyValues extends
            MutablePropertyValues {

        /**
         * Create new ServletConfigPropertyValues.
         *
         * @param config
         *            ServletConfig we'll use to take PropertyValues from
         * @param requiredProperties
         *            set of property names we need, where we can't accept
         *            default values
         * @throws ServletException
         *             if any required properties are missing
         */
        public ServletConfigPropertyValues(ServletConfig config)
                throws ServletException {

            Enumeration<String> en = config.getInitParameterNames();
            while (en.hasMoreElements()) {
                String property = en.nextElement();
                Object value = config.getInitParameter(property);
                addPropertyValue(new PropertyValue(property, value));
            }

        }
    }

}
