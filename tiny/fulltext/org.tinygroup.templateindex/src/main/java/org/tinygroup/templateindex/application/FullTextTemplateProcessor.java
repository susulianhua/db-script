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
package org.tinygroup.templateindex.application;

import org.tinygroup.application.AbstractApplicationProcessor;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.loader.FileResourceManager;
import org.tinygroup.vfs.VFS;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.List;

/**
 * 加载全文检索模板的应用处理器(暂不考虑支持扩展函数和扩展静态类)
 *
 * @author yancheng11334
 */
public class FullTextTemplateProcessor extends AbstractApplicationProcessor {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(FullTextTemplateProcessor.class);

    private static final String FULLTEXT_TEMPLATE_PAGE = "fulltextpage";

    private TemplateEngine templateEngine;

    private FileResourceManager fileResourceManager;

    private FileResolver fileResolver;

    public TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    public void setTemplateEngine(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public FileResourceManager getFileResourceManager() {
        return fileResourceManager;
    }

    public void setFileResourceManager(FileResourceManager fileResourceManager) {
        this.fileResourceManager = fileResourceManager;
    }

    public FileResolver getFileResolver() {
        return fileResolver;
    }

    public void setFileResolver(FileResolver fileResolver) {
        this.fileResolver = fileResolver;
    }

    public void start() {
        LOGGER.logMessage(LogLevel.INFO,
                "FullTextTemplateProcessor开始处理资源加载器...");
        List<String> scanningPaths = fileResolver.getScanningPaths();
        for (String path : scanningPaths) {
            LOGGER.logMessage(LogLevel.INFO, "开始添加{0}的资源加载器...", path);
//            FileObjectResourceLoader fileResourceLoader = new FileObjectResourceLoader(
//                    FULLTEXT_TEMPLATE_PAGE, null,
//                    null, path);
//            templateEngine.addResourceLoader(fileResourceLoader);
            fileResourceManager.addScanFile(VFS.resolveFile(path));
            LOGGER.logMessage(LogLevel.INFO, "添加{0}的资源加载器结束", path);
        }
        LOGGER.logMessage(LogLevel.INFO, "FullTextTemplateProcessor处理资源加载器结束");
    }

    public void stop() {

    }

    public String getApplicationNodePath() {
        return null;
    }

    public String getComponentConfigPath() {
        return null;
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {

    }

    public XmlNode getComponentConfig() {
        return null;
    }

    public XmlNode getApplicationConfig() {
        return null;
    }

    public int getOrder() {
        return 0;
    }

}
