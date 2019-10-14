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

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * 模板引擎
 * Created by luoguo on 2014/6/6.
 */

public interface TemplateEngine extends TemplateContextOperator {

    /**
     * 默认的bean配置名称
     */
    String DEFAULT_BEAN_NAME = "templateEngine";

    boolean isSafeVariable();

    void setSafeVariable(boolean safeVariable);

    boolean isCheckModified();

    /**
     * 是否检查模板是否被修改过
     *
     * @param checkModified
     */
    void setCheckModified(boolean checkModified);

    /**
     * 返回是否是紧凑模式
     *
     * @return
     */
    boolean isCompactMode();

    /**
     * 设置是否是紧凑模式
     *
     * @param compactMode
     */
    void setCompactMode(boolean compactMode);

    /**
     * 返回国际化访问接口实现类
     *
     * @return
     */
    I18nVisitor getI18nVisitor();

    /**
     * 设置
     *
     * @param i18nVistor
     */
    TemplateEngine setI18nVisitor(I18nVisitor i18nVistor);

    /**
     * 添加函数
     *
     * @param function
     */
    TemplateEngine addTemplateFunction(TemplateFunction function);

    /**
     * 返回注册的方法
     *
     * @param methodName 注册的方法名
     * @return
     */
    TemplateFunction getTemplateFunction(String methodName);

    /**
     * 返回注册的方法
     *
     * @param methodName 注册的方法名
     * @return
     */
    TemplateFunction getTemplateFunction(Object object, String methodName);

    /**
     * 返回编码
     *
     * @return
     */
    String getEncode();

    /**
     * 设置编码
     *
     * @param encode
     * @return
     */
    TemplateEngine setEncode(String encode);

    /**
     * 是否支持国际化模板
     *
     * @return
     */
    boolean isLocaleTemplateEnable();

    /**
     * 设置是否支持国际化模板
     *
     * @param localeTemplateEnable
     */
    void setLocaleTemplateEnable(boolean localeTemplateEnable);

    /**
     * 添加类型加载器
     *
     * @param resourceLoader
     */
    TemplateEngine addResourceLoader(ResourceLoader resourceLoader);

    /**
     * 返回所有的 Loader
     *
     * @return
     */
    List<ResourceLoader> getResourceLoaderList();

    /**
     * 设置模板加载器列表
     *
     * @param resourceLoaderList
     */
    void setResourceLoaderList(List<ResourceLoader> resourceLoaderList);

    /**
     * 渲染宏
     *
     * @param macroName    要执行的宏名称
     * @param template     调用宏的模板
     * @param context      上下文
     * @param outputStream 输出器
     */
    void renderMacro(String macroName, Template template, TemplateContext context, OutputStream outputStream) throws IOException, TemplateException;

    /**
     * 渲染指定的宏
     *
     * @param macro        要执行的宏
     * @param template     调用宏的模板
     * @param context      上下文
     * @param outputStream 输出器
     * @throws IOException
     * @throws TemplateException
     */
    void renderMacro(Macro macro, Template template, TemplateContext context, OutputStream outputStream) throws IOException, TemplateException;


    /**
     * 根据路径渲染一个模板文件，如果有布局会同时渲染布局
     *
     * @param path         模板对应的路径
     * @param context      上下文
     * @param outputStream 输出器
     */
    void renderTemplate(String path, TemplateContext context, OutputStream outputStream) throws TemplateException;

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
     * 采用没有上下文，控制台输出方式进行渲染，如果有布局会同时渲染布局
     *
     * @param path
     * @throws TemplateException
     */
    void renderTemplate(String path) throws TemplateException;

    /**
     * 采用没有上下文，控制台输出方式进行渲染，主要用于测试
     *
     * @param template
     * @throws TemplateException
     */
    void renderTemplate(Template template) throws TemplateException;

    /**
     * 直接渲染一个模板，不会应用布局
     *
     * @param template     要渲染的模板
     * @param context      上下文
     * @param outputStream 输出器
     * @throws TemplateException
     */
    void renderTemplate(Template template, TemplateContext context, OutputStream outputStream) throws TemplateException;

    /**
     * 根据宏名查找要调用的宏
     *
     * @param macroName
     * @param template
     * @param context
     * @return
     * @throws TemplateException
     */
    Macro findMacro(Object macroName, Template template, TemplateContext context) throws TemplateException;

    /**
     * 根据路径查找宏
     *
     * @param path
     * @return
     * @throws TemplateException
     */
    Template findTemplate(String path) throws TemplateException;

    /**
     * 根据路径卸载模板
     *
     * @param path
     * @throws TemplateException
     */
    void removeTemplate(String path) throws TemplateException;

    /**
     * 执行方法
     *
     * @param functionName
     * @param parameters
     * @return
     * @throws TemplateException
     */
    Object executeFunction(Template template, TemplateContext context, String functionName, Object... parameters) throws TemplateException;

    /**
     * 获取资源对应的文本
     *
     * @param path
     * @return
     */
    String getResourceContent(String path, String encode) throws TemplateException;

    /**
     * 获取指定路径资源的内容
     *
     * @param path
     * @return
     * @throws TemplateException
     */
    String getResourceContent(String path) throws TemplateException;


    /**
     * 注册库文件中所有的宏
     *
     * @param path
     */
    void registerMacroLibrary(String path) throws TemplateException;

    /**
     * 注册单个宏
     *
     * @param macro
     * @throws TemplateException
     */
    void registerMacro(Macro macro) throws TemplateException;

    /**
     * 注册模板文件中所有的宏
     *
     * @param template
     * @throws TemplateException
     */
    void registerMacroLibrary(Template template) throws TemplateException;

    void write(OutputStream outputStream, Object data) throws TemplateException;

    /**
     * 注册静态类操作
     *
     * @throws TemplateException
     */
    void registerStaticClassOperator(StaticClassOperator operator) throws TemplateException;

    /**
     * 获得静态类操作
     *
     * @param name
     * @return
     * @throws TemplateException
     */
    StaticClassOperator getStaticClassOperator(String name) throws TemplateException;

    /**
     * 从缓存中查找模板
     * @param path
     * @return
     * @throws TemplateException
     */
    public Template findTemplateCache(String path) throws TemplateException;

    /**
     * 从缓存中查找模板
     * @param path
     * @return
     * @throws TemplateException
     */
    public Template findTemplateCache(String path, String absolutePath) throws TemplateException;

    /**
     * 往缓存中增加模板
     * @param path
     * @param template
     * @throws TemplateException
     */
    public void addTemplateCache(String path, Template template) throws TemplateException;

    /**
     * 从缓存中删除模板
     * @param path
     * @param absolutePath
     * @throws TemplateException
     */
    public void removeTemplateCache(String path, String absolutePath) throws TemplateException;

    /**
     * 从缓存中查找宏
     * @param macroName
     * @return
     * @throws TemplateException
     */
    public Macro findMacroCache(String macroName) throws TemplateException;

    /**
     * 往缓存中增加宏
     * @param macroName
     * @param macro
     * @throws TemplateException
     */
    public void addMacroCache(String macroName, Macro macro) throws TemplateException;

    /**
     *  从缓存中删除宏
     * @param macroName
     * @param absolutePath
     * @throws TemplateException
     */
    public void removeMacroCache(String macroName, String absolutePath) throws TemplateException;
}
