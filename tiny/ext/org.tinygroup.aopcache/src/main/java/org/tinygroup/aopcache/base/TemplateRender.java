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
package org.tinygroup.aopcache.base;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.ParameterNameDiscoverer;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.StringResourceLoader;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;

/**
 * 模板渲染类
 *
 * @author renhui
 */
public class TemplateRender {

    private static TemplateRender templateRender;
    private TemplateEngine templateEngine;
    private StringResourceLoader resourceLoader;
    private ParameterNameDiscoverer parameterNameDiscoverer;

    private TemplateRender(ParameterNameDiscoverer parameterNameDiscoverer) {
        templateEngine = new TemplateEngineDefault();
        resourceLoader = new StringResourceLoader();
        this.parameterNameDiscoverer = parameterNameDiscoverer;
        templateEngine.addResourceLoader(resourceLoader);
    }

    public static TemplateRender newInstance(ParameterNameDiscoverer parameterNameDiscoverer) {
        if (templateRender == null) {
            templateRender = new TemplateRender(parameterNameDiscoverer);
        }
        return templateRender;
    }

    public String renderTemplate(MethodInvocation invocation, String content)
            throws TemplateException, UnsupportedEncodingException {
        if (StringUtil.isBlank(content)) {
            return "";
        }
        TemplateContext templateContext = assemblyContext(invocation);
        return renderTemplate(templateContext, content);
    }

    public String renderTemplate(TemplateContext context, String content)
            throws TemplateException, UnsupportedEncodingException {
        if (StringUtil.isBlank(content)) {
            return "";
        }
        Template template = resourceLoader.loadTemplate(content);
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        template.render(context, writer);
        return new String(writer.toByteArray(), "UTF-8");
    }

    public TemplateContext assemblyContext(MethodInvocation invocation) {
        TemplateContext context = new TemplateContextDefault();
        Method method = invocation.getMethod();
        String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.put(paramNames[i], invocation.getArguments()[i]);
            }
        }
        return context;
    }

    public Object getParamValue(TemplateContext context, String key) {
        return context.get(key);
    }

}
