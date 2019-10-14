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
package org.tinygroup.template;

import java.io.OutputStream;

/**
 * 模板渲染辅助类，简化渲染接口
 *
 * @author yancheng11334
 */
public interface TemplateRender {

    /**
     * 默认的bean配置名称
     */
    String DEFAULT_BEAN_NAME = "templateRender";

    TemplateEngine getTemplateEngine();

    void setTemplateEngine(TemplateEngine engine);

    /**
     * 根据路径渲染一个模板文件，如果有布局会同时渲染布局
     *
     * @param path         模板对应的路径
     * @param context      上下文
     * @param outputStream 输出器
     */
    void renderTemplate(String path, TemplateContext context, OutputStream outputStream) throws TemplateException;

    /**
     * 根据路径渲染一个模板文件，如果有布局会同时渲染布局,返回字符串
     *
     * @param path
     * @param context
     * @return
     * @throws TemplateException
     */
    String renderTemplate(String path, TemplateContext context) throws TemplateException;

    /**
     * 根据路径渲染一个模板文件，但不会渲染布局
     *
     * @param path
     * @param context
     * @param outputStream
     * @throws TemplateException
     */
    void renderTemplateWithOutLayout(String path, TemplateContext context, OutputStream outputStream) throws TemplateException;

    /**
     * 根据路径渲染一个模板文件，但不会渲染布局,返回字符串
     *
     * @param path
     * @param context
     * @return
     * @throws TemplateException
     */
    String renderTemplateWithOutLayout(String path, TemplateContext context) throws TemplateException;

    /**
     * 根据文本内容渲染
     *
     * @param content
     * @param context
     * @param outputStream
     * @throws TemplateException
     */
    void renderTemplateContent(String content, TemplateContext context, OutputStream outputStream) throws TemplateException;

    /**
     * 根据文本内容渲染，返回字符串
     *
     * @param content
     * @param context
     * @return
     * @throws TemplateException
     */
    String renderTemplateContent(String content, TemplateContext context) throws TemplateException;
}
