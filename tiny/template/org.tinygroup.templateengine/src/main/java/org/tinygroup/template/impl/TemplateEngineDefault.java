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

import org.tinygroup.commons.tools.ExceptionUtil;
import org.tinygroup.template.*;
import org.tinygroup.template.application.DefaultStaticClassOperator;
import org.tinygroup.template.function.*;
import org.tinygroup.template.function.escape.EscapeHtmlFunction;
import org.tinygroup.template.function.escape.UnEscapeHtmlFunction;
import org.tinygroup.template.interpret.TemplateInterpreter;
import org.tinygroup.template.interpret.context.*;
import org.tinygroup.template.interpret.terminal.*;
import org.tinygroup.template.rumtime.TemplateUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模板引擎实现类
 * Created by luoguo on 2014/6/6.
 */
public class TemplateEngineDefault implements TemplateEngine {

    public static final TemplateInterpreter interpreter = new TemplateInterpreter();
    //提供模板引擎渲染的静态类
    private static Map<String, StaticClassOperator> staticClassOperatorMap = new HashMap<String, StaticClassOperator>();

//    protected static final Logger LOGGER = LoggerFactory
//            .getLogger(TemplateEngineDefault.class);

    static {
        interpreter.addTerminalNodeProcessor(new IntegerOctNodeProcessor());
        interpreter.addTerminalNodeProcessor(new EscapeTextNodeProcessor());
        interpreter.addTerminalNodeProcessor(new FalseNodeProcessor());
        interpreter.addTerminalNodeProcessor(new IntegerNodeProcessor());
        interpreter.addTerminalNodeProcessor(new NullNodeProcessor());
        interpreter.addTerminalNodeProcessor(new StringDoubleNodeProcessor());
        interpreter.addTerminalNodeProcessor(new StringSingleNodeProcessor());
        interpreter.addTerminalNodeProcessor(new TextCdataNodeProcessor());
        interpreter.addTerminalNodeProcessor(new TextPlainNodeProcessor());
        interpreter.addTerminalNodeProcessor(new TrueNodeProcessor());
        interpreter.addTerminalNodeProcessor(new FloatProcessor());
        interpreter.addTerminalNodeProcessor(new IntegerHexNodeProcessor());
    }

    static {
        interpreter.addContextProcessor(new PageContentProcessor());
        interpreter.addContextProcessor(new MapProcessor());
        interpreter.addContextProcessor(new ExpressionGroupProcessor());
        interpreter.addContextProcessor(new ValueProcessor());
        interpreter.addContextProcessor(new ForProcessor());
        interpreter.addContextProcessor(new SetProcessor());
        interpreter.addContextProcessor(new IfProcessor());
        interpreter.addContextProcessor(new ElseIfProcessor());
        interpreter.addContextProcessor(new RangeProcessor());
        interpreter.addContextProcessor(new ArrayProcessor());
        interpreter.addContextProcessor(new MathBinaryProcessor());
        interpreter.addContextProcessor(new MathCompareProcessor());
        interpreter.addContextProcessor(new MathSingleRightProcessor());
        interpreter.addContextProcessor(new MathSingleLeftProcessor());
        interpreter.addContextProcessor(new BlankProcessor());
        interpreter.addContextProcessor(new TabProcessor());
        interpreter.addContextProcessor(new EolProcessor());
        interpreter.addContextProcessor(new CommentProcessor());
        interpreter.addContextProcessor(new MathIdentifierProcessor());
        interpreter.addContextProcessor(new ForBreakProcessor());
        interpreter.addContextProcessor(new ForContinueProcessor());
        interpreter.addContextProcessor(new MapListProcessor());
        interpreter.addContextProcessor(new MathUnaryProcessor());
        interpreter.addContextProcessor(new MathConditionProcessor());
        interpreter.addContextProcessor(new MathConditionSimpleProcessor());
        interpreter.addContextProcessor(new MathCompareConditionProcessor());
        interpreter.addContextProcessor(new MathCompareRalationProcessor());
        interpreter.addContextProcessor(new MathBinaryShiftProcessor());
        interpreter.addContextProcessor(new MathBitwiseProcessor());
        interpreter.addContextProcessor(new ArrayGetProcessor());
        interpreter.addContextProcessor(new ImportIgnoreProcessor());
        interpreter.addContextProcessor(new MacroDefineIgnoreProcessor());
        interpreter.addContextProcessor(new CallProcessor());
        interpreter.addContextProcessor(new CallWithBodyProcessor());
        interpreter.addContextProcessor(new CallMacroProcessor());
        interpreter.addContextProcessor(new CallMacroWithBodyProcessor());
        interpreter.addContextProcessor(new LayoutDefineProcessor());
        interpreter.addContextProcessor(new LayoutImplementProcessor());
        interpreter.addContextProcessor(new DentProcessor());
        interpreter.addContextProcessor(new IndentProcessor());
        interpreter.addContextProcessor(new TextProcessor());
        interpreter.addContextProcessor(new BodyContentProcessor());
        interpreter.addContextProcessor(new FieldProcessor());
        interpreter.addContextProcessor(new StopProcessor());
        interpreter.addContextProcessor(new ReturnProcessor());
        interpreter.addContextProcessor(new IncludeProcessor());
        interpreter.addContextProcessor(new MemberFunctionCallProcessor());
        interpreter.addContextProcessor(new FunctionCallProcessor());
        interpreter.addContextProcessor(new WhileProcessor());
    }

    static {
        addDefaultStaticClassOperator(new DefaultStaticClassOperator("Integer", Integer.class));
        addDefaultStaticClassOperator(new DefaultStaticClassOperator("Long", Long.class));
        addDefaultStaticClassOperator(new DefaultStaticClassOperator("Short", Short.class));
        addDefaultStaticClassOperator(new DefaultStaticClassOperator("Double", Double.class));
        addDefaultStaticClassOperator(new DefaultStaticClassOperator("Float", Float.class));
        addDefaultStaticClassOperator(new DefaultStaticClassOperator("Boolean", Boolean.class));
        addDefaultStaticClassOperator(new DefaultStaticClassOperator("String", String.class));
        addDefaultStaticClassOperator(new DefaultStaticClassOperator("Byte", Byte.class));
        addDefaultStaticClassOperator(new DefaultStaticClassOperator("Number", Number.class));
        addDefaultStaticClassOperator(new DefaultStaticClassOperator("Math", Math.class));
        addDefaultStaticClassOperator(new DefaultStaticClassOperator("System", System.class));
    }

    private Map<String, TemplateFunction> functionMap = new HashMap<String, TemplateFunction>();
    private Map<Class, Map<String, TemplateFunction>> typeFunctionMap = new HashMap<Class, Map<String, TemplateFunction>>();
    private TemplateContext templateEngineContext;
    private List<ResourceLoader> resourceLoaderList = new ArrayList<ResourceLoader>();
    private String encode = "UTF-8";
    private I18nVisitor i18nVisitor;
    private TemplateCache<String, List<Template>> layoutPathListCache = new TemplateCacheDefault<String, List<Template>>();

    private TemplateCache<String, Object> localeSearchResults = new TemplateCacheDefault<String, Object>();

    //模板缓存,第一级key:path,第二级key:absolutePath
    private Map<String, Map<String, Template>> templateCache = new ConcurrentHashMap<String, Map<String, Template>>();
    //宏缓存,第一级key:macroName,第二级key:absolutePath
    private Map<String, Map<String, Macro>> macroCache = new ConcurrentHashMap<String, Map<String, Macro>>();
    //排序比较器
    private UpdatableComparator comparator = new UpdatableComparator();

    private boolean checkModified = false;
    private boolean localeTemplateEnable = false;
    private boolean compactMode;
    //Lexer是否抛出侦听到的异常
    private boolean throwLexerError = false;

    public TemplateEngineDefault() {
        //添加一个默认的加载器
        addTemplateFunction(new FormatterTemplateFunction());
        addTemplateFunction(new InstanceOfTemplateFunction());
        addTemplateFunction(new GetResourceContentFunction());
        addTemplateFunction(new EvaluateTemplateFunction());
        addTemplateFunction(new CallMacroFunction());
        addTemplateFunction(new GetFunction());
        addTemplateFunction(new RandomFunction());
        addTemplateFunction(new ToIntFunction());
        addTemplateFunction(new ToLongFunction());
        addTemplateFunction(new ToBoolFunction());
        addTemplateFunction(new ToFloatFunction());
        addTemplateFunction(new ToDoubleFunction());
        addTemplateFunction(new FormatDateFunction());
        addTemplateFunction(new TodayFunction());
        addTemplateFunction(new ParseTemplateFunction());
        addTemplateFunction(new I18nFunction());
        addTemplateFunction(new ExtendMapFunction());
        addTemplateFunction(new RenderLayerFunction());
        addTemplateFunction(new EscapeHtmlFunction());
        addTemplateFunction(new UnEscapeHtmlFunction());
        addTemplateFunction(new UrlEncodeFunction());
        addTemplateFunction(new UrlDecodeFunction());
    }

    private static void addDefaultStaticClassOperator(StaticClassOperator operator) {
        staticClassOperatorMap.put(operator.getName(), operator);
    }

    public boolean isLocaleTemplateEnable() {
        return localeTemplateEnable;
    }

    public void setLocaleTemplateEnable(boolean localeTemplateEnable) {
        this.localeTemplateEnable = localeTemplateEnable;
    }

    public boolean isCheckModified() {
        return checkModified;
    }

    public void setCheckModified(boolean checkModified) {
        this.checkModified = checkModified;
    }

    public boolean isSafeVariable() {
        return TemplateUtil.isSafeVariable();
    }

    public void setSafeVariable(boolean safeVariable) {
        TemplateUtil.setSafeVariable(safeVariable);
    }

    public TemplateInterpreter getTemplateInterpreter() {
        return interpreter;
    }

    public boolean isCompactMode() {
        return compactMode;
    }

    public void setCompactMode(boolean compactMode) {
        this.compactMode = compactMode;
    }

    public boolean isThrowLexerError() {
        return throwLexerError;
    }

    public void setThrowLexerError(boolean throwLexerError) {
        this.throwLexerError = throwLexerError;
    }

    public void registerMacroLibrary(String path) throws TemplateException {
        registerMacroLibrary(getMacroLibrary(path));
    }

    public void registerMacro(Macro macro) throws TemplateException {
        addMacroCache(macro.getName(), macro);
    }

    public void registerMacroLibrary(Template template) throws TemplateException {
        for (Map.Entry<String, Macro> entry : template.getMacroMap().entrySet()) {
            Macro macro = entry.getValue();
            if (macro.getMacroPath() == null) {
                macro.setMacroPath(template.getPath());
            }
            if (macro.getAbsolutePath() == null) {
                macro.setAbsolutePath(template.getAbsolutePath());
            }
            macro.setLastModifiedTime(template.getLastModifiedTime());
            registerMacro(entry.getValue());
        }
    }

    public void write(OutputStream outputStream, Object data) throws TemplateException {
        try {
            if (data != null) {
                if (data instanceof byte[]) {
                    outputStream.write((byte[]) data);
                } else if (data instanceof ByteArrayOutputStream) {
                    outputStream.write(((ByteArrayOutputStream) data).toByteArray());
                } else {
                    outputStream.write(data.toString().getBytes(getEncode()));
                }
            }
        } catch (IOException e) {
            throw new TemplateException(e);
        }
    }

    public TemplateContext getTemplateContext() {
        if (templateEngineContext == null) {
            templateEngineContext = new TemplateContextDefault(staticClassOperatorMap);
        }
        return templateEngineContext;
    }

    public I18nVisitor getI18nVisitor() {
        return i18nVisitor;
    }

    public TemplateEngineDefault setI18nVisitor(I18nVisitor i18nVistor) {
        this.i18nVisitor = i18nVistor;
        return this;
    }

    public TemplateEngineDefault addTemplateFunction(TemplateFunction function) {
        function.setTemplateEngine(this);
        String[] names = function.getNames().split(",");
        if (function.getBindingTypes() == null) {
            for (String name : names) {
                functionMap.put(name, function);
            }
        } else {
            String[] types = function.getBindingTypes().split(",");
            for (String type : types) {
                try {
                    Class clazz = Class.forName(type);
                    Map<String, TemplateFunction> nameMap = typeFunctionMap.get(clazz);
                    if (nameMap == null) {
                        nameMap = new HashMap<String, TemplateFunction>();
                        typeFunctionMap.put(clazz, nameMap);
                    }
                    for (String name : names) {
                        nameMap.put(name, function);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return this;
    }

    public TemplateFunction getTemplateFunction(String methodName) {
        return functionMap.get(methodName);
    }

    public TemplateFunction getTemplateFunction(Object object, String methodName) {
        Map<String, TemplateFunction> typeMap = typeFunctionMap.get(object.getClass());
        if (typeMap != null) {
            TemplateFunction function = typeMap.get(methodName);
            if (function != null) {
                return function;
            }
        }
        for (Class clz : typeFunctionMap.keySet()) {
            if (clz.isInstance(object)) {
                TemplateFunction function = typeFunctionMap.get(clz).get(methodName);
                if (function != null) {
                    return function;
                }
            }
        }
        return null;
    }

    public String getEncode() {
        return encode;
    }

    public TemplateEngineDefault setEncode(String encode) {
        this.encode = encode;
        return this;
    }

    public TemplateEngineDefault addResourceLoader(ResourceLoader resourceLoader) {
        resourceLoader.setTemplateEngine(this);
        resourceLoaderList.add(resourceLoader);
        return this;
    }

    private Template findTemplate(TemplateContext context, String path) throws TemplateException {
        if (localeTemplateEnable) {
            //查询国际化模板
            Locale locale = context.get("defaultLocale");
            if (locale != null) {
                String localePath = TemplateUtil.getLocalePath(path, locale);
                if (localeSearchResults.contains(localePath)) {
                    return findTemplate(path);
                }
                try {
                    Template template = findTemplate(localePath);
                    if (template != null) {
                        return template;
                    } else {
                        localeSearchResults.put(localePath, "");
                    }
                } catch (TemplateException e) {
                    //findTemplate查找不到国际化模板资源可能会抛出异常，这时候再找默认模板资源
                    localeSearchResults.put(localePath, "");
                    return findTemplate(path);
                }
            }
        }
        //查询默认模板
        return findTemplate(path);
    }

    private Template findLayout(TemplateContext context, String path) throws TemplateException {
        if (localeTemplateEnable) {
            //查询国际化模板
            Locale locale = context.get("defaultLocale");
            if (locale != null) {
                String localePath = TemplateUtil.getLocalePath(path, locale);
                if (localeSearchResults.contains(localePath)) {
                    return findLayout(path, false);
                }
                try {
                    Template template = findLayout(localePath, false);
                    if (template != null) {
                        return template;
                    } else {
                        localeSearchResults.put(localePath, "");
                    }
                } catch (TemplateException e) {
                    //findLayout查找不到国际化模板资源可能会抛出异常，这时候再找默认模板资源
                    localeSearchResults.put(localePath, "");
                    return findLayout(path, false);
                }
            }
        }
        //查询原始模板
        return findLayout(path, false);
    }

    public Template findTemplate(String path) throws TemplateException {
        Template template = null;
        if (!checkModified) {
            template = findTemplateCache(path);
            if (template != null) {
                updateTemplate(template);
                return template;
            }
        }
        if (template == null) {
            for (ResourceLoader loader : resourceLoaderList) {
                template = loader.getTemplate(path);
                if (template != null) {
                    updateTemplate(template);
                    return template;
                }
            }
        }
        throw wrapperException("找不到模板：" + path);
    }

    private TemplateException wrapperException(String error) {
        Exception e = new Exception(error);
        return new TemplateException(e);
    }

    private void updateTemplate(Template template) {
        if (template.getTemplateEngine() == null) {
            template.setTemplateEngine(this);
        }
        if (template.getTemplateContext().getParent() == null) {
            template.getTemplateContext().setParent(getTemplateContext());
        }
    }

    /**
     * 卸载指定路径的全部模板
     */
    public void removeTemplate(String path) throws TemplateException {
        Map<String, Template> map = templateCache.get(path);
        if (map != null) {
            for (Template template : map.values()) {
                //删除宏
                for (Macro macro : template.getMacroMap().values()) {
                    removeMacroCache(macro.getName(), macro.getAbsolutePath());
                }
                removeTemplateCache(template.getPath(), template.getAbsolutePath());
            }
        }
        layoutPathListCache.remove(path);
    }

    public Template findLayout(String path) throws TemplateException {
        return findLayout(path, true);
    }

    private Template findLayout(String path, boolean throwException) throws TemplateException {
        Template template = null;
        //LOGGER.logMessage(LogLevel.INFO, String.format("findLayout开始查询布局%s", path));
        if (!checkModified) {
            template = findTemplateCache(path);
            if (template != null) {
                updateTemplate(template);
                //LOGGER.logMessage(LogLevel.INFO, String.format("findLayout结束,查询到布局%s !", path));
                return template;
            }
        }
        if (template == null) {
            for (ResourceLoader loader : resourceLoaderList) {
                template = loader.getLayout(path);
                if (template != null) {
                    updateTemplate(template);
                    //LOGGER.logMessage(LogLevel.INFO, String.format("findLayout结束,查询到布局%s !", path));
                    return template;
                }
            }
        }
        //LOGGER.logMessage(LogLevel.INFO, String.format("findLayout结束,没有查询到布局%s !",path));
        if (throwException) {
            throw wrapperException("找不到模板：" + path);
        } else {
            return null;
        }
    }

    private Template getMacroLibrary(String path) throws TemplateException {
        Template template = null;
        if (!checkModified) {
            template = findTemplateCache(path);
            if (template != null) {
                updateTemplate(template);
                return template;
            }
        }
        for (ResourceLoader loader : resourceLoaderList) {
            template = loader.getMacroLibrary(path);
            if (template != null) {
                updateTemplate(template);
                return template;
            }
        }
        throw wrapperException("找不到模板：" + path);
    }

    public TemplateEngineDefault put(String key, Object value) {
        templateEngineContext.put(key, value);
        return this;
    }

    public void renderMacro(String macroName, Template Template, TemplateContext context, OutputStream outputStream) throws TemplateException {
        findMacro(macroName, Template, context).render(Template, context, context, outputStream);
    }

    public void renderMacro(Macro macro, Template Template, TemplateContext context, OutputStream outputStream) throws TemplateException {
        macro.render(Template, context, context, outputStream);
    }

    public void renderTemplate(String path, TemplateContext context, OutputStream outputStream) throws TemplateException {
        try {
            //LOGGER.logMessage(LogLevel.INFO, String.format("渲染路径%s开始", path));
            Template template = findTemplate(context, path);
            //LOGGER.logMessage(LogLevel.INFO, "查询模板文件成功!");
            if (template != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                //先执行page的渲染
                template.render(context, byteArrayOutputStream);
                //LOGGER.logMessage(LogLevel.INFO, "渲染页面文件成功!");
                Integer renderLayer = context.get("$renderLayer", 0);

                List<Template> layoutPaths = getLayoutList(context, renderLayer, template.getPath());
                //LOGGER.logMessage(LogLevel.INFO, String.format("查询到%s个布局文件!", layoutPaths.size()));
                if (layoutPaths.size() > 0) {
                    context.put("pageContent", byteArrayOutputStream);
                    ByteArrayOutputStream layoutWriter = null;

                    TemplateContext layoutContext = context;
                    for (int i = layoutPaths.size() - 1; i >= 0; i--) {
                        //每次都构建新的Writer和Context来执行
                        TemplateContext tempContext = new TemplateContextDefault();
                        tempContext.setParent(layoutContext);
                        layoutContext = tempContext;
                        layoutWriter = new ByteArrayOutputStream();
                        layoutPaths.get(i).render(layoutContext, layoutWriter);
                        if (i > 0) {
                            layoutContext.put("pageContent", layoutWriter);
                        }
                    }
                    outputStream.write(layoutWriter.toByteArray());
                } else {
                    outputStream.write(byteArrayOutputStream.toByteArray());
                }
                //LOGGER.logMessage(LogLevel.INFO, String.format("渲染路径%s结束", path));
            } else {
                throw wrapperException("找不到模板：" + path);
            }
        } catch (IOException e) {
            throw new TemplateException(e);
        } catch (TemplateException e) {
            TemplateException te = processTemplateException(e);
            throw te;
        }
    }

    public void renderTemplateWithOutLayout(String path, TemplateContext context, OutputStream outputStream) throws TemplateException {
        Template template = findTemplate(context, path);
        if (template != null) {
            renderTemplate(template, context, outputStream);
        } else {
            throw wrapperException("找不到模板：" + path);
        }
    }

    private List<Template> getLayoutList(TemplateContext context, Integer renderLayer, String templatePath) throws TemplateException {
        List<Template> layoutPathList = null;

        String cacheKey = templatePath + "|" + renderLayer.toString(); //重新定义缓存的key值
        if (!checkModified) {
            layoutPathList = layoutPathListCache.get(cacheKey);
            if (layoutPathList != null) {
                return layoutPathList;
            }
        }
        if (layoutPathList == null) {
            layoutPathList = new ArrayList<Template>();
        }
        String[] paths = templatePath.split("/");   // 拆分路径
        String[] dirs = new String[paths.length - 1];
        String pageName = paths[paths.length - 1];

        for (int i = 0; i < dirs.length; i++) {   //目录路径
            if (i == 0) {
                dirs[i] = templatePath.startsWith("/") ? "/" : "";
            } else {
                dirs[i] = dirs[i - 1] + paths[i] + "/";
            }
        }

        for (int i = 0; i < dirs.length; i++) {
            //LOGGER.logMessage(LogLevel.INFO, String.format("查询目录%s的布局文件开始!",dirs[i]));
            if (i == dirs.length - 1) {
                findLayoutTemplateList(dirs[i], paths[i + 1], dirs[i] + pageName, context, false, layoutPathList);
            } else {
                findLayoutTemplateList(dirs[i], paths[i + 1], dirs[i] + pageName, context, true, layoutPathList);
            }
            //LOGGER.logMessage(LogLevel.INFO, String.format("查询目录%s的布局文件结束!",dirs[i]));
        }

        if (renderLayer > 0 && renderLayer < layoutPathList.size()) {
            //除去多余的层次
            while (renderLayer < layoutPathList.size()) {
                layoutPathList.remove(0); //从最外层删起
            }
        }
        if (!checkModified) {
            layoutPathListCache.put(cacheKey, layoutPathList);
        }

        return layoutPathList;
    }

    private void findLayoutTemplateList(String dirPath, String dirName, String pagePath, TemplateContext context, boolean queryTag, List<Template> layoutPathList) throws TemplateException {
        Template layout = null;
        //先查询同名文件的布局模板
        layout = findLayoutTemplate(context, pagePath);
        if (layout != null) {
            layoutPathList.add(layout);
            return;
        }

        //再查询与当前目录同名的布局模板(最底层不查)
        if (queryTag) {
            layout = findLayoutTemplate(context, dirPath, dirName);
            if (layout != null) {
                layoutPathList.add(layout);
                return;
            }
        }


        //最后查询default名称的布局模板
        layout = findLayoutTemplate(context, dirPath, "default");
        if (layout != null) {
            layoutPathList.add(layout);
            return;
        }
    }

    /**
     * 根据页面查询同名布局模板
     *
     * @param context
     * @param pagePath
     * @return
     * @throws TemplateException
     */
    private Template findLayoutTemplate(TemplateContext context, String pagePath) throws TemplateException {
        Template layout = null;
        for (ResourceLoader loader : resourceLoaderList) {
            String layoutPath = loader.getLayoutPath(pagePath); //资源加载器会自动将页面路径转换成布局路径(所以页面路径可以虚拟的)
            if (layoutPath != null) {
                layout = findLayout(context, layoutPath);
                if (layout != null) {
                    return layout;
                }
            }
        }
        return null;
    }

    /**
     * 根据配置布局名查询布局模板
     *
     * @param context
     * @param path
     * @param layoutName
     * @return
     * @throws TemplateException
     */
    private Template findLayoutTemplate(TemplateContext context, String path, String layoutName) throws TemplateException {
        Template layout = null;
        for (ResourceLoader loader : resourceLoaderList) {
            String layoutPath = path + layoutName + loader.getLayoutExtName();
            layout = findLayout(context, layoutPath);
            if (layout != null) {
                return layout;
            }
        }
        return null;
    }

    public void renderTemplate(String path) throws TemplateException {
        renderTemplate(path, new TemplateContextDefault(), System.out);
    }

    public void renderTemplate(Template Template) throws TemplateException {
        renderTemplate(Template, new TemplateContextDefault(), System.out);
    }

    public void renderTemplate(Template template, TemplateContext context, OutputStream outputStream) throws TemplateException {
        try {
            template.render(context, outputStream);
        } catch (TemplateException e) {
            TemplateException te = processTemplateException(e);
            throw te;
        }
    }

    public Macro findMacro(Object macroNameObject, Template template, TemplateContext context) throws TemplateException {
        //上下文中的宏优先处理，主要是考虑bodyContent宏
        String macroName = macroNameObject.toString();
        Object obj = context.getItemMap().get(macroName);
        if (obj instanceof Macro) {
            return (Macro) obj;
        }
        //查找私有宏
        Macro macro = template.getMacroMap().get(macroName);
        if (macro != null) {
            return macro;
        }
        //先查找import的列表，后添加的优先
        for (int i = template.getImportPathList().size() - 1; i >= 0; i--) {
            Template macroLibrary = getMacroLibrary(template.getImportPathList().get(i));
            if (macroLibrary != null) {
                macro = macroLibrary.getMacroMap().get(macroName);
                if (macro != null) {
                    if (macro.getMacroPath() == null) {
                        macro.setMacroPath(macroLibrary.getPath());
                    }
                    return macro;
                }
            }
        }

        //根据名称查找最新的宏
        macro = findMacroCache(macroName);
        if (macro != null) {
            return macro;
        }

        throw wrapperException("找不到宏：" + macroName);
    }

    public Object executeFunction(Template Template, TemplateContext context, String functionName, Object... parameters) throws TemplateException {
        TemplateFunction function = functionMap.get(functionName);
        if (function != null) {
            return function.execute(Template, context, parameters);
        }
        throw wrapperException("找不到函数：" + functionName);
    }

    public List<ResourceLoader> getResourceLoaderList() {
        return resourceLoaderList;
    }

    public void setResourceLoaderList(List<ResourceLoader> resourceLoaderList) {
        this.resourceLoaderList = resourceLoaderList;
    }

    public String getResourceContent(String path, String encode) throws TemplateException {
        for (ResourceLoader resourceLoader : resourceLoaderList) {
            String content = resourceLoader.getResourceContent(path, encode);
            if (content != null) {
                return content;
            }
        }
        throw wrapperException("找不到资源：" + path);
    }

    public String getResourceContent(String path) throws TemplateException {
        return getResourceContent(path, getEncode());
    }

    public void registerStaticClassOperator(StaticClassOperator operator)
            throws TemplateException {
        staticClassOperatorMap.put(operator.getName(), operator);
    }

    public StaticClassOperator getStaticClassOperator(String name)
            throws TemplateException {
        return staticClassOperatorMap.get(name);
    }

    /**
     * 对模板引擎的异常进行处理
     *
     * @param e
     * @return
     */
    private TemplateException processTemplateException(TemplateException e) {
        List<Throwable> list = ExceptionUtil.getCauses(e, true);
        if (list != null) {
            TemplateException te = null;
            for (Throwable throwable : list) {
                if (throwable instanceof TemplateException) {
                    te = (TemplateException) throwable;
                    te.recombine();
                    return te;
                }
            }
        }
        return e;
    }

    public Template findTemplateCache(String path) {
        Map<String, Template> map = templateCache.get(path);
        if (map != null && map.size() > 0) {
            return Collections.min(map.values(), comparator);
        }
        return null;
    }

    public Template findTemplateCache(String path, String absolutePath) {
        Map<String, Template> map = templateCache.get(path);
        if (map != null) {
            return map.get(absolutePath);
        }
        return null;
    }

    public void addTemplateCache(String path, Template template) {
        Map<String, Template> map = templateCache.get(path);
        if (map == null) {
            map = new HashMap<String, Template>();
            templateCache.put(path, map);
        }
        map.put(template.getAbsolutePath(), template);
    }

    public void removeTemplateCache(String path, String absolutePath) {
        Map<String, Template> map = templateCache.get(path);
        if (map != null) {
            map.remove(absolutePath);
            if (map.isEmpty()) {
                templateCache.remove(path);
            }
        }
    }

    public Macro findMacroCache(String macroName) {
        Map<String, Macro> map = macroCache.get(macroName);
        if (map != null && map.size() > 0) {
            return Collections.min(map.values(), comparator);
        }
        return null;
    }

    public void addMacroCache(String macroName, Macro macro) {
        Map<String, Macro> map = macroCache.get(macroName);
        if (map == null) {
            map = new HashMap<String, Macro>();
            macroCache.put(macroName, map);
        }
        map.put(macro.getAbsolutePath(), macro);
    }

    public void removeMacroCache(String macroName, String absolutePath) {
        Map<String, Macro> map = macroCache.get(macroName);
        if (map != null) {
            map.remove(absolutePath);
            if (map.isEmpty()) {
                macroCache.remove(macroName);
            }
        }
    }

    public Map<String, Map<String, Macro>> getMacroCache() {
        return macroCache;
    }


    static class UpdatableComparator implements Comparator<Updatable> {

        public int compare(Updatable o1, Updatable o2) {
            if (o1.getLastModifiedTime() > o2.getLastModifiedTime()) {
                return -1;
            } else if (o1.getLastModifiedTime() < o2.getLastModifiedTime()) {
                return 1;
            }
            return 0;
        }

    }

}
