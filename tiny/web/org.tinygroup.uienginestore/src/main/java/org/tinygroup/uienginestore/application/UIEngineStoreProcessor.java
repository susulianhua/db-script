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
package org.tinygroup.uienginestore.application;

import org.springframework.util.AntPathMatcher;
import org.tinygroup.application.AbstractApplicationProcessor;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.fileresolver.ProcessorCallBack;
import org.tinygroup.fileresolver.impl.MultiThreadFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.uienginestore.FileObjectStore;
import org.tinygroup.uienginestore.StoreConfig;
import org.tinygroup.uienginestore.UIOperator;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectFilter;
import org.tinygroup.vfs.FileObjectProcessor;
import org.tinygroup.vfs.VFS;
import org.tinygroup.weblayer.listener.ServletContextHolder;
import org.tinygroup.xmlparser.node.XmlNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * UIEngine应用处理器
 * @author yancheng11334
 *
 */
public class UIEngineStoreProcessor extends AbstractApplicationProcessor {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(UIEngineStoreProcessor.class);

    private static final String STORE_PATH = "storePath";
    private static final String MERGE_TAG = "mergeTag";
    private static final String INCLUDE_PATTERN = "includePattern";
    private static final String EXCLUDE_PATTERN = "excludePattern";
    private static final String PATTERN = "pattern";
    private static final String MERGE_CSS_NAME = "mergeCssName";
    private static final String MERGE_JS_NAME = "mergeJsName";
    private static final String THREAD_NUM_NAME = "threadNum";
    private static final String SYN_STORE_TAG = "synStore";
    private static final String CSS_LIMIT = "cssLimit";
    private static final String DEFAULT_MERGE_CSS_NAME = "uiengine.css";
    private static final String DEFAULT_MERGE_JS_NAME = "uiengine.js";

    private XmlNode applicationConfig;
    private XmlNode componentConfig;

    private FileObjectStore fileObjectStore;
    private FullContextFileRepository fullContextFileRepository;
    private UIOperator uIOperator;

    public FileObjectStore getFileObjectStore() {
        return fileObjectStore;
    }

    public void setFileObjectStore(FileObjectStore fileObjectStore) {
        this.fileObjectStore = fileObjectStore;
    }

    public UIOperator getuIOperator() {
        return uIOperator;
    }

    public void setuIOperator(UIOperator uIOperator) {
        this.uIOperator = uIOperator;
    }

    public FullContextFileRepository getFullContextFileRepository() {
        return fullContextFileRepository;
    }

    public void setFullContextFileRepository(
            FullContextFileRepository fullContextFileRepository) {
        this.fullContextFileRepository = fullContextFileRepository;
    }

    /**
     * 解析存储配置信息
     * @param totalConfig
     * @return
     */
    protected StoreConfig createStoreConfig(XmlNode totalConfig) {
        String storePath = totalConfig == null ? null : totalConfig.getAttribute(STORE_PATH);
        if (StringUtil.isEmpty(storePath)) {
            LOGGER.logMessage(LogLevel.WARN, "没有找到UI资源存储路径,请检查是否配置{}存储路径", STORE_PATH);
            return null;
        } else {
            //storePath存在表示totalConfig一定存在
            StoreConfig config = new StoreConfig();
            boolean mergeTag = Boolean.parseBoolean(totalConfig.getAttribute(MERGE_TAG, "true"));
            String mergeCssName = totalConfig.getAttribute(MERGE_CSS_NAME, DEFAULT_MERGE_CSS_NAME);
            String mergeJsName = totalConfig.getAttribute(MERGE_JS_NAME, DEFAULT_MERGE_JS_NAME);
            int threadNum = Integer.parseInt(totalConfig.getAttribute(THREAD_NUM_NAME, String.valueOf(Runtime.getRuntime().availableProcessors())));
            boolean synStore = Boolean.parseBoolean(totalConfig.getAttribute(SYN_STORE_TAG, "false"));
            long cssLimit = Long.parseLong(totalConfig.getAttribute(CSS_LIMIT, "0"));
            List<XmlNode> includePatterns = totalConfig.getSubNodes(INCLUDE_PATTERN);
            List<XmlNode> excludePatterns = totalConfig.getSubNodes(EXCLUDE_PATTERN);

            if (!CollectionUtil.isEmpty(includePatterns)) {
                for (XmlNode xmlNode : includePatterns) {
                    config.addIncludePattern(xmlNode.getAttribute(PATTERN));
                }
            }
            if (!CollectionUtil.isEmpty(excludePatterns)) {
                for (XmlNode xmlNode : excludePatterns) {
                    config.addExcludePattern(xmlNode.getAttribute(PATTERN));
                }
            }
            config.setMergeTag(mergeTag);
            config.setMergeJsName(mergeJsName);
            config.setMergeCssName(mergeCssName);
            config.setStorePath(storePath);
            config.setThreadNum(threadNum);
            config.setSynStore(synStore);
            config.setCssLimit(cssLimit * 1024);
            return config;
        }

    }

    public void start() {
        LOGGER.logMessage(LogLevel.INFO, "UIEngine应用处理器操作UI资源开始...");

        XmlNode totalConfig = ConfigurationUtil.combineXmlNode(applicationConfig, componentConfig);
        final StoreConfig config = createStoreConfig(totalConfig);
        if (config != null) {

            if (config.isSynStore()) {
                //与主线程同步.应用处理器结束保证上传结束
                processJs(config);
                processCss(config);
                processOther(config);
            } else {
                new Thread(new Runnable() {
                    //与主线程异步.应用处理器不知道上传何时结束
                    public void run() {
                        processJs(config);
                        processCss(config);
                        processOther(config);
                    }
                }).start();
            }

        }

        LOGGER.logMessage(LogLevel.INFO, "UIEngine应用处理器操作UI资源结束.");
    }

    public void stop() {
        applicationConfig = null;
        componentConfig = null;
        fileObjectStore = null;
        uIOperator = null;
    }

    public String getApplicationNodePath() {
        return "/application/uiengine-store";
    }

    public String getComponentConfigPath() {
        return "/uienginestore.config.xml";
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        this.applicationConfig = applicationConfig;
        this.componentConfig = componentConfig;
    }

    public XmlNode getComponentConfig() {
        return componentConfig;
    }

    public XmlNode getApplicationConfig() {
        return applicationConfig;
    }

    public int getOrder() {
        return 0;
    }

    private void processOther(final StoreConfig config) {
        LOGGER.logMessage(LogLevel.INFO, "UIEngine应用处理器操作一般静态资源开始...");
        final OtherFileObjectFilter fileObjectFilter = new OtherFileObjectFilter(config.getIncludePatterns(), config.getExcludePatterns());
        final OtherFileObjectProcessor fileObjectProcessor = new OtherFileObjectProcessor(config.getStorePath());

        Map<String, FileObject> allFileObjects = fullContextFileRepository.getAllFileObjects();

        List<FileObject> fileObjects = new ArrayList<FileObject>(allFileObjects.values());
        //遍历所有全文静态资源查询到的资源
        MultiThreadFileProcessor.multiProcessor(config.getThreadNum(), "storeOther", fileObjects, new ProcessorCallBack() {
            public void callBack(FileObject fileObject) {
                fileObject.foreach(fileObjectFilter, fileObjectProcessor);
            }

        });
        LOGGER.logMessage(LogLevel.INFO, "UIEngine应用处理器操作一般静态资源结束.");
    }

    private void processJs(final StoreConfig config) {
        LOGGER.logMessage(LogLevel.INFO, "UIEngine应用处理器操作JS开始...");
        try {
            List<FileObject> fileObjects = uIOperator.createJS(config);

            MultiThreadFileProcessor.multiProcessor(config.getThreadNum(), "storeJs", fileObjects, new ProcessorCallBack() {

                public void callBack(FileObject source) {
                    try {
                        storeResource(source, config.getStorePath());
                    } catch (Exception e) {
                        LOGGER.errorMessage("UIEngine应用处理器操作JS发生异常,存储路径[{0}]", e, config.getStorePath());
                    } finally {
                        source.clean();
                    }

                }
            });
        } catch (Exception e) {
            LOGGER.errorMessage("UIEngine应用处理器操作JS发生异常,存储路径[{0}]", e, config.getStorePath());
        }

        LOGGER.logMessage(LogLevel.INFO, "UIEngine应用处理器操作JS结束.");
    }

    private void storeResource(FileObject source, String storePath) throws Exception {
        String path = computeStorePath(source, storePath);
        FileObject target = null;
        try {
            target = VFS.resolveFile(path);
            fileObjectStore.store(source, target);
        } finally {
            //source释放由业务逻辑决定
            if (target != null) {
                target.clean();
            }
        }
    }

    private String computeStorePath(FileObject source, String storePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(storePath); //添加存储前缀
        boolean endtag = storePath.endsWith("/");
        boolean starttag = source.getPath().startsWith("/");
        if (endtag) {
            if (starttag) {
                sb.append(source.getPath().substring(1));
            } else {
                sb.append(source.getPath());
            }
        } else {
            if (starttag) {
                sb.append(source.getPath());
            } else {
                sb.append("/").append(source.getPath());
            }
        }
        return sb.toString();
    }

    private void processCss(final StoreConfig config) {
        LOGGER.logMessage(LogLevel.INFO, "UIEngine应用处理器操作CSS开始...");
        try {
            String contextPath = ServletContextHolder.getServletContext().getContextPath();
            List<FileObject> fileObjects = uIOperator.createCSS(contextPath, "/" + config.getMergeCssName(), config);

            MultiThreadFileProcessor.multiProcessor(config.getThreadNum(), "storeCss", fileObjects, new ProcessorCallBack() {

                public void callBack(FileObject source) {
                    try {
                        storeResource(source, config.getStorePath());
                    } catch (Exception e) {
                        LOGGER.errorMessage("UIEngine应用处理器操作CSS发生异常,存储路径[{0}]", e, config.getStorePath());
                    } finally {
                        source.clean();
                    }

                }
            });

        } catch (Exception e) {
            LOGGER.errorMessage("UIEngine应用处理器操作CSS发生异常,存储路径[{0}]", e, config.getStorePath());
        }
        LOGGER.logMessage(LogLevel.INFO, "UIEngine应用处理器操作CSS结束.");
    }

    class OtherFileObjectFilter implements FileObjectFilter {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        private List<String> includePatterns = null;
        private List<String> excludePatterns = null;

        public OtherFileObjectFilter(List<String> includePatternNodes, List<String> excludePatternNodes) {
            this.includePatterns = includePatternNodes;
            this.excludePatterns = excludePatternNodes;
        }

        public boolean accept(FileObject fileObject) {
            //忽略目录
            if (!fileObject.isExist() || fileObject.isFolder()) {
                return false;
            }
            //优先判断排除规则
            String path = fileObject.getPath();
            for (String pattern : excludePatterns) {
                if (antPathMatcher.match(pattern, path)) {
                    return false;
                }
            }

            //如果用户没有设置匹配规则，默认全部匹配;设置匹配规则，只有匹配某条规则才能通过
            if (includePatterns.isEmpty()) {
                return true;
            } else {
                for (String pattern : includePatterns) {
                    if (antPathMatcher.match(pattern, path)) {
                        return true;
                    }
                }
                return false;
            }

        }

    }

    class OtherFileObjectProcessor implements FileObjectProcessor {
        private String storePath;

        public OtherFileObjectProcessor(String path) {
            storePath = path;
        }

        //执行处理逻辑
        public void process(FileObject fileObject) {
            try {
                storeResource(fileObject, storePath);
            } catch (Exception e) {
                LOGGER.errorMessage("UIEngine应用处理器操作一般资源发生异常,源路径[{0}],存储路径[{1}]", e, fileObject.getAbsolutePath(), storePath);
            }
        }

    }

}
