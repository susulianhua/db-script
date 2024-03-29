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
package org.tinygroup.template.impl;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.template.*;
import org.tinygroup.template.loader.StringResourceLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 解决文件加载器和字符串加载器行为差异过大的问题
 *
 * @author yancheng11334
 */
public class TemplateRenderDefault implements TemplateRender {

    private TemplateEngine templateEngine;

    public TemplateEngine getTemplateEngine() {
        if (templateEngine == null) {
            return BeanContainerFactory.getBeanContainer(getClass()
                    .getClassLoader()).getBean(TemplateEngine.DEFAULT_BEAN_NAME);
        }
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine engine) {
        this.templateEngine = engine;
    }

    public void renderTemplate(String path, TemplateContext context,
                               OutputStream outputStream) throws TemplateException {
        getTemplateEngine().renderTemplate(path, context, outputStream);
    }

    public String renderTemplate(String path, TemplateContext context)
            throws TemplateException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            renderTemplate(path, context, outputStream);
            return new String(outputStream.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new TemplateException(e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new TemplateException(e);
            }
        }
    }

    public void renderTemplateWithOutLayout(String path,
                                            TemplateContext context, OutputStream outputStream)
            throws TemplateException {
        getTemplateEngine().renderTemplateWithOutLayout(path, context,
                outputStream);
    }

    public String renderTemplateWithOutLayout(String path,
                                              TemplateContext context) throws TemplateException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            renderTemplateWithOutLayout(path, context, outputStream);
            return new String(outputStream.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new TemplateException(e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new TemplateException(e);
            }
        }
    }

    public void renderTemplateContent(String content, TemplateContext context,
                                      OutputStream outputStream) throws TemplateException {
        StringResourceLoader stringResourceLoader = new StringResourceLoader();
        try {
            stringResourceLoader.setTemplateEngine(getTemplateEngine()); //设置引擎依赖
            Template template = stringResourceLoader.loadTemplate(content);
            template.render(context, outputStream);
        } finally {
            stringResourceLoader.setTemplateEngine(null); //解除引擎依赖
        }
    }

    public String renderTemplateContent(String content, TemplateContext context)
            throws TemplateException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            renderTemplateContent(content, context, outputStream);
            return new String(outputStream.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new TemplateException(e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new TemplateException(e);
            }
        }
    }

}
