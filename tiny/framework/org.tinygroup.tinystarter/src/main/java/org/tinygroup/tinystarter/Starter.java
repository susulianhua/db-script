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
package org.tinygroup.tinystarter;

import java.io.InputStream;
import java.util.List;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.application.impl.ApplicationDefault;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.config.ConfigManagerFactory;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.FileResolverFactory;
import org.tinygroup.fileresolver.FileResolverUtil;
import org.tinygroup.fileresolver.impl.ConfigurationFileProcessor;
import org.tinygroup.fileresolver.impl.LocalPropertiesFileProcessor;
import org.tinygroup.fileresolver.util.FileResolverLoadUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.remoteconfig.RemoteConfigReadClient;
import org.tinygroup.remoteconfig.utils.RemoteConfigHandler;
import org.tinygroup.remoteconfig.utils.TinyConfigParamUtil;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public class Starter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Starter.class);
    private static boolean started = false;
    private static String DEFAULT_CONFIG = "application.xml";
    private static Starter starter;
    private Application application;

    private Starter() {
        super();
    }

    public static Starter getInstance() {
        if (starter == null) {
            starter = new Starter();
        }
        return starter;
    }

    /**
     * 初始化
     *
     * @param xmlFile
     *            是否对classPath进行处理
     */
    public void start(String xmlFile) {
        if (started) {
            return;
        }
        try {
            processConfig(xmlFile);
            LOGGER.logMessage(LogLevel.INFO, "启动应用开始...");
            application.init();
            application.start();
            started = true;
            LOGGER.logMessage(LogLevel.INFO, "启动应用完成...");
        } catch (Exception e) {
            LOGGER.errorMessage("tiny应用启动出错:", e);
            throw new RuntimeException(e);
        }
    }

    public void processConfig(String xmlFile) {
        String configXml = xmlFile;
        if (null == configXml || "".equals(configXml)) {
            configXml = DEFAULT_CONFIG;
        }
        InputStream inputStream = Starter.class.getClassLoader()
                .getResourceAsStream(configXml);
        if (inputStream == null) {
            inputStream = Starter.class.getResourceAsStream(configXml);
        }
        String applicationConfig = "";
        if (inputStream != null) {
            try {
                applicationConfig = StreamUtil.readText(inputStream, "UTF-8",
                        false);
                application = new ApplicationDefault();
                if (applicationConfig != null) {
                    ConfigurationManager c = ConfigurationUtil
                            .getConfigurationManager();
                    XmlNode applicationXml = new XmlStringParser().parse(
                            applicationConfig).getRoot();
                    c.setApplicationConfiguration(applicationXml);

                }
                loadProperties(applicationConfig);
                loadConfig(applicationConfig);

                XmlNode applicationXml = ConfigurationUtil
                        .getConfigurationManager()
                        .getApplicationConfiguration();
                if (applicationXml != null) {
                    List<XmlNode> processorConfigs = applicationXml
                            .getSubNodesRecursively("application-processor");
                    if (processorConfigs != null) {
                        for (XmlNode processorConfig : processorConfigs) {
                            String processorBean = processorConfig
                                    .getAttribute("bean");
                            ApplicationProcessor processor = BeanContainerFactory
                                    .getBeanContainer(
                                            this.getClass().getClassLoader())
                                    .getBean(processorBean);
                            application.addApplicationProcessor(processor);
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.errorMessage("载入应用配置信息时出现异常，错误原因：{}！", e, e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private void loadConfig(String applicationConfig) {
        FileResolver fileResolver = FileResolverFactory.getFileResolver();
        FileResolverUtil.addClassPathPattern(fileResolver);
        FileResolverLoadUtil.loadFileResolverConfig(fileResolver, applicationConfig);
        fileResolver.addFileProcessor(new ConfigurationFileProcessor());
        fileResolver.resolve();
    }

    public void stop() {
        if (started) {
            application.stop();
        }
    }

    /**
     * 加载配置，包含本地配置和远程配置 优先加载本地配置，然后加载远程配置 如果存在key一样的配置，以远程配置为主
     *
     * @param applicationConfig
     */
    private void loadProperties(String applicationConfig) {
        // 加载本地
        LOGGER.infoMessage("加载application常量配置...开始");
        LocalPropertiesFileProcessor localFileProcessor = new LocalPropertiesFileProcessor(
                applicationConfig);
        localFileProcessor.start();
        // 远程配置
        RemoteConfigReadClient client = TinyConfigParamUtil.getReadClient();
        if (client != null) {
            RemoteConfigHandler remoteConfig = new RemoteConfigHandler(
                    applicationConfig, client);
            remoteConfig.start();
        }
        ConfigManagerFactory.getManager().addLast(ConfigManagerFactory.TYPE_ENVVAR,System.getenv());//在配置里增加环境变量配置
        // 仅替换配置
        ConfigurationUtil.getConfigurationManager().replace();
//        MergePropertiesFileProcessor mergeProcessor = new MergePropertiesFileProcessor();
//        mergeProcessor.start();
        LOGGER.infoMessage("加载application常量配置...结束");
    }

}
