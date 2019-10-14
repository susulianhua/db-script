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
package org.tinygroup.template.fileresolver;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.ProcessorCallBack;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.fileresolver.impl.MultiThreadFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.template.Macro;
import org.tinygroup.template.ResourceLoader;
import org.tinygroup.template.Template;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.impl.TemplateEngineDefault;
import org.tinygroup.template.loader.FileObjectResourceLoader;
import org.tinygroup.template.loader.FileResourceManager;
import org.tinygroup.template.loader.TemplateLoadUtil;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * tiny宏文件扫描
 *
 * @author renhui
 */
public class TinyMacroFileProcessor extends AbstractFileProcessor {

    private static final String TINY_TEMPLATE_CONFIG = "/application/template-config";
    private static final String TINY_TEMPLATE_CONFIG_PATH = "/templateconfig.config.xml";
    //配置的三个参数名
    private static final String TEMPLATE_EXT_FILE_NAME = "templateExtFileName";
    private static final String LAYOUT_EXT_FILE_NAME = "layoutExtFileName";
    private static final String COMPONENT_EXT_FILE_NAME = "componentExtFileName";
    private static final String THREAD_NUM = "threadNum";
    private static final String SYN_LOAD = "synLoad";
    private static final String TEXT_ORDER_BY_TIME = "textOrderByTime";
    private static final String THROW_LEXER_ERROR = "throwLexerError";

    //配置参数的默认值
    private static final String TEMPLATE_EXT_DEFALUT = "page";
    private static final String LAYOUT_EXT_DEFALUT = "layout";
    private static final String COMPONENT_EXT_DEFALUT = "component";

    private String templateExtFileName;
    private String layoutExtFileName;
    private String componentExtFileName;

    private boolean initTag = false;
    private boolean synLoad = true;
    private TemplateEngine engine;
    private FileResourceManager fileResourceManager;
    private int threadNum = Runtime.getRuntime().availableProcessors();
    private boolean textOrderByTime = false;
    private boolean throwLexerError = false;

    public TemplateEngine getEngine() {
        return engine;
    }

    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    public FileResourceManager getFileResourceManager() {
        return fileResourceManager;
    }

    public void setFileResourceManager(FileResourceManager fileResourceManager) {
        this.fileResourceManager = fileResourceManager;
    }

    public boolean checkMatch(FileObject fileObject) {
        init();
        return isPage(fileObject.getFileName()) || isLayout(fileObject.getFileName()) || isComponent(fileObject.getFileName());
    }

    private boolean isPage(String fileName) {
        return fileName.endsWith("." + templateExtFileName);
    }

    private boolean isLayout(String fileName) {
        return fileName.endsWith("." + layoutExtFileName);
    }

    private boolean isComponent(String fileName) {
        return fileName.endsWith("." + componentExtFileName);
    }

    /**
     * 解析XML配置，获得动态的页面文件/布局文件/宏文件后缀(时间点必须在checkMatch之前)
     */
    private void init() {
        if (!initTag) {
            reloadTemplateConfig();
            initTag = true;
        }
    }

    public void process() {
        if (synLoad) {
            synProcess();
        } else {
            nonSynProcess();
        }
    }

    /**
     * 同步方式处理
     */
    private void synProcess() {
        if (deleteList != null && !deleteList.isEmpty()) {
            deleteTemplate();
        }
        if (changeList != null && !changeList.isEmpty()) {
            updateTemplate();
        }

    }

    /**
     * 异步方式处理
     */
    private void nonSynProcess() {
        if (deleteList != null && !deleteList.isEmpty()) {
            new Thread(new DeleteTemplateThread()).start();
        }

        if (changeList != null && !changeList.isEmpty()) {
            new Thread(new UpdateTemplateThread()).start();
        }
    }


    /**
     * 设置模板引擎TemplateEngine
     */
    private synchronized void reloadTemplateConfig() {
        //合并节点
        XmlNode totalConfig = ConfigurationUtil.combineXmlNode(applicationConfig, componentConfig);

        templateExtFileName = TEMPLATE_EXT_DEFALUT;
        layoutExtFileName = LAYOUT_EXT_DEFALUT;
        componentExtFileName = COMPONENT_EXT_DEFALUT;

        if (totalConfig != null) {
            templateExtFileName = StringUtil.defaultIfBlank(totalConfig.getAttribute(TEMPLATE_EXT_FILE_NAME), TEMPLATE_EXT_DEFALUT);
            layoutExtFileName = StringUtil.defaultIfBlank(totalConfig.getAttribute(LAYOUT_EXT_FILE_NAME), LAYOUT_EXT_DEFALUT);
            componentExtFileName = StringUtil.defaultIfBlank(totalConfig.getAttribute(COMPONENT_EXT_FILE_NAME), COMPONENT_EXT_DEFALUT);
            threadNum = Integer.parseInt(StringUtil.defaultIfBlank(totalConfig.getAttribute(THREAD_NUM), String.valueOf(Runtime.getRuntime().availableProcessors())));
            synLoad = Boolean.parseBoolean(StringUtil.defaultIfBlank(totalConfig.getAttribute(SYN_LOAD), "true"));
            textOrderByTime = Boolean.parseBoolean(StringUtil.defaultIfBlank(totalConfig.getAttribute(TEXT_ORDER_BY_TIME), "false"));
            throwLexerError = Boolean.parseBoolean(StringUtil.defaultIfBlank(totalConfig.getAttribute(THROW_LEXER_ERROR), "false"));
        }

        boolean addTag = true;
        for (ResourceLoader loader : engine.getResourceLoaderList()) {
            if (loader instanceof FileObjectResourceLoader) {
                addTag = false;
                break;
            }
        }
        fileResourceManager.setFileResolver(fileResolver);
        fileResourceManager.setTextOrderByTime(textOrderByTime);

        //执行动态注册逻辑
        if (addTag) {
            FileObjectResourceLoader fileObjectResourceLoader = new FileObjectResourceLoader(templateExtFileName, layoutExtFileName, componentExtFileName);
            fileObjectResourceLoader.setFileResourceManager(fileResourceManager);
            engine.addResourceLoader(fileObjectResourceLoader);
        }

        if (engine instanceof TemplateEngineDefault) {
            TemplateEngineDefault templateEngine = (TemplateEngineDefault) engine;
            templateEngine.setThrowLexerError(throwLexerError);
        }
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        super.config(applicationConfig, componentConfig);
    }

    public String getComponentConfigPath() {
        return TINY_TEMPLATE_CONFIG_PATH;
    }

    public String getApplicationNodePath() {
        return TINY_TEMPLATE_CONFIG;
    }

    private void updateTemplate() {
        //如果线程数超过文件数,multiProcessor运行会发生异常
        int runThreadNum = changeList.size() > threadNum ? threadNum : changeList.size();
        MultiThreadFileProcessor.multiProcessor(runThreadNum, "updateTemplate-muti", changeList,
                new ProcessorCallBack() {
                    public void callBack(FileObject fileObject) {
                        LOGGER.logMessage(LogLevel.INFO, "模板资源文件[{0}]开始加载", fileObject.getAbsolutePath());
                        try {
                            fileResourceManager.addResource(fileObject.getPath(), fileObject);
                            Template template = engine.findTemplateCache(fileObject.getPath(), fileObject.getAbsolutePath());
                            if (template != null) {
                                //执行删除旧模板宏的逻辑
                                if (isComponent(fileObject.getFileName())) {
                                    for (Macro macro : template.getMacroMap().values()) {
                                        engine.removeMacroCache(macro.getName(), macro.getAbsolutePath());
                                    }
                                }
                                engine.removeTemplateCache(fileObject.getPath(), fileObject.getAbsolutePath());
                            }
                            template = TemplateLoadUtil.loadComponent((TemplateEngineDefault) engine, fileObject);
                            template.setTemplateEngine(engine);
                            engine.addTemplateCache(template.getPath(), template);
                            if (isComponent(fileObject.getFileName())) {
                                engine.registerMacroLibrary(template);
                            }

                        } catch (Exception e) {
                            LOGGER.errorMessage("加载模板资源文件[{0}]出错,注册主键[{1}]", e, fileObject.getAbsolutePath(), fileObject.getPath());
                        }
                        LOGGER.logMessage(LogLevel.INFO, "模板资源文件[{0}]加载完毕,注册主键[{1}]", fileObject.getAbsolutePath(), fileObject.getPath());

                    }
                });
    }

    private void deleteTemplate() {
        //如果线程数超过文件数,multiProcessor运行会发生异常
        int runThreadNum = deleteList.size() > threadNum ? threadNum : deleteList.size();
        MultiThreadFileProcessor.multiProcessor(runThreadNum, "deleteTemplate-muti", deleteList,
                new ProcessorCallBack() {
                    public void callBack(FileObject fileObject) {
                        LOGGER.logMessage(LogLevel.INFO, "模板资源文件[{0}]开始移除", fileObject.getAbsolutePath());
                        try {
                            fileResourceManager.removeResource(fileObject);
                            Template template = engine.findTemplateCache(fileObject.getPath(), fileObject.getAbsolutePath());
                            if (template != null) {
                                if (isComponent(fileObject.getFileName())) {
                                    for (Macro macro : template.getMacroMap().values()) {
                                        engine.removeMacroCache(macro.getName(), macro.getAbsolutePath());
                                    }
                                }
                                engine.removeTemplateCache(template.getPath(), template.getAbsolutePath());
                            }

                        } catch (Exception e) {
                            LOGGER.errorMessage("移除模板资源文件[{0}]出错,卸载主键[{1}]", e, fileObject.getAbsolutePath(), fileObject.getPath());
                        }
                        LOGGER.logMessage(LogLevel.INFO, "模板资源文件[{0}]移除完毕,卸载主键[{1}]", fileObject.getAbsolutePath(), fileObject.getPath());
                    }
                });
    }

    /**
     * 异步更新模板线程
     *
     * @author yancheng11334
     */
    class UpdateTemplateThread implements Runnable {

        public void run() {
            updateTemplate();
        }

    }

    /**
     * 异步删除模板线程
     *
     * @author yancheng11334
     */
    class DeleteTemplateThread implements Runnable {

        public void run() {
            deleteTemplate();
        }

    }
}
