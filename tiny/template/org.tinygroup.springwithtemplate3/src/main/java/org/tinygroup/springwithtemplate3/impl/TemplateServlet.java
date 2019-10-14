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
package org.tinygroup.springwithtemplate3.impl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.i18n.LocaleUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.springwithtemplate3.ServletContextUtil;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateContextDefault;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TemplateServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = -4046031305101985471L;
    private final String DEFAULT_VIEW_TEMPLATE = ".pagelet";
    private final String DEFAULT_VIEW_LAYOUT = ".page";
    private String viewTemplate;
    private String viewLayout;
    private TemplateEngine templateEngine;

    public String getViewTemplate() {
        return viewTemplate;
    }

    public void setViewTemplate(String viewTemplate) {
        this.viewTemplate = viewTemplate;
    }

    public String getViewLayout() {
        return viewLayout;
    }

    public void setViewLayout(String viewLayout) {
        this.viewLayout = viewLayout;
    }

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doRequest(request, response);
    }

    public void init() throws ServletException {
        super.init();
        viewTemplate = StringUtil.isEmpty(getInitParameter("viewTemplate")) ? DEFAULT_VIEW_TEMPLATE : getInitParameter("viewTemplate");
        viewLayout = StringUtil.isEmpty(getInitParameter("viewLayout")) ? DEFAULT_VIEW_LAYOUT : getInitParameter("viewLayout");
        templateEngine = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBean(TemplateEngine.DEFAULT_BEAN_NAME);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doRequest(request, response);
    }

    protected void doRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (StringUtil.isEmpty(response.getContentType())) {
            response.setContentType("text/html;charset=utf-8");
        }
        String path = request.getServletPath();
        try {
            TemplateContext templateContext = createTemplateContext(request, response);
            if (path.endsWith(viewTemplate)) {
                path = path.substring(0, path.length() - viewTemplate.length()) + ".page";
                templateEngine.renderTemplateWithOutLayout(path, templateContext, response.getOutputStream());
            } else if (path.endsWith(viewLayout)) {
                path = path.substring(0, path.length() - viewLayout.length()) + ".page";
                templateEngine.renderTemplate(path, templateContext, response.getOutputStream());
            } else {
                throw new ServletException("无法处理的路径:" + path);
            }
        } catch (TemplateException e) {
            throw new ServletException(e);
        }
    }

    private TemplateContext createTemplateContext(HttpServletRequest request, HttpServletResponse response) {
        TemplateContext templateContext = new TemplateContextDefault();
        templateContext.put(
                "uiengine",
                BeanContainerFactory.getBeanContainer(
                        this.getClass().getClassLoader()).getBean(
                        "uiComponentManager"));

        //设置templateContext的内置对象
        templateContext.put("request", request);
        templateContext.put("response", response);
        templateContext.put("session", request.getSession());
        templateContext.put("application", ServletContextUtil.getServletContext());
        templateContext.put("defaultLocale", LocaleUtil.getContext().getLocale());

        templateContext.put("TINY_CONTEXT_PATH", request.getContextPath());
        templateContext.put("TINY_REQUEST_URI", request.getRequestURI());

        return templateContext;
    }
}
