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
package org.tinygroup.tinyrunner;

import java.io.InputStream;
import java.util.List;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.application.impl.ApplicationDefault;
import org.tinygroup.beancontainer.BeanContainer;
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
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.remoteconfig.RemoteConfigReadClient;
import org.tinygroup.remoteconfig.utils.RemoteConfigHandler;
import org.tinygroup.springutil.SpringBeanContainer;
import org.tinygroup.springutil.fileresolver.SpringBeansFileProcessor;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public class Runner {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Runner.class);
    private static final String TINY_JAR_PATTERN = "org\\.tinygroup\\.(.)*\\.jar";
    // "/Application.preloadbeans.xml";
    private static boolean init = false;
    private static Application application;
    private static String DEFAULT_CONFIG = "application.xml";
    private static Class<? extends BeanContainer<?>> beanContainer;
    private static RemoteConfigReadClient remoteConfigReadClient;

    public static void setRemoteConfigReadClient(
            RemoteConfigReadClient remoteConfigReadClient) {
        Runner.remoteConfigReadClient = remoteConfigReadClient;
    }

    public static void init(String xmlFile, List<String> includePathPatterns) {
        init(SpringBeanContainer.class, xmlFile, includePathPatterns);
    }

    public static void init(Class<? extends BeanContainer<?>> container, String xmlFile, List<String> includePathPatterns) {
        if (init) {
            return;
        }
        if (container != null) {
            beanContainer = container;
        } else {
            beanContainer = SpringBeanContainer.class;
        }
        initDirect(xmlFile, includePathPatterns);
    }

    /**
     * 初始化
     *
     * @param classPathResolve 是否对classPath进行处理
     */
    public static void initDirect(String xmlFile, List<String> includePathPatterns) {

        // init(xmlFile, classPathResolve, null, null);
        String configXml = xmlFile;
        if (null == configXml || "".equals(configXml)) {
            configXml = DEFAULT_CONFIG;
        }
        InputStream inputStream = Runner.class.getClassLoader()
                .getResourceAsStream(configXml);
        if (inputStream == null) {
            inputStream = Runner.class.getResourceAsStream(configXml);
        }
        if(inputStream == null){
        	LOGGER.errorMessage("tiny应用启动出错,找不到配置文件：[{}]", configXml);
            throw new RuntimeException("tiny应用启动出错,找不到配置文件：[" + configXml + "]");
        }
        
        String applicationConfig = "";
        try {
            applicationConfig = StreamUtil.readText(inputStream, "UTF-8",
                    false);
            application = new ApplicationDefault();
            if (applicationConfig != null) {
                ConfigurationManager c = ConfigurationUtil
                        .getConfigurationManager();
                XmlNode applicationXml = new XmlStringParser().parse(applicationConfig).getRoot();
                c.setApplicationConfiguration(applicationXml);

            }
            initSpring(applicationConfig, includePathPatterns);
            FileResolver fileResolver = BeanContainerFactory
                    .getBeanContainer(
                            Runner.class.getClassLoader())
                    .getBean(FileResolver.BEAN_NAME);
            FileResolverUtil.addClassPathPattern(fileResolver);
            fileResolver.addIncludePathPattern(TINY_JAR_PATTERN);
            addIncludePathPatterns(fileResolver, includePathPatterns);
            FileResolverLoadUtil.loadFileResolverConfig(fileResolver, applicationConfig);
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
                                        Runner.class
                                                .getClassLoader()).getBean(
                                        processorBean);// TODO
                        application.addApplicationProcessor(processor);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.errorMessage("载入应用配置信息时出现异常，错误原因：{}！", e, e.getMessage());
            throw new RuntimeException(e);
        }

        application.init();
        application.start();
        init = true;
    }

    private static void initSpring(String applicationConfig, List<String> includePathPatterns) {
        if (beanContainer == null) {
            BeanContainerFactory.initBeanContainer(SpringBeanContainer.class.getName());
        } else {
            BeanContainerFactory.initBeanContainer(beanContainer.getName());
        }
        FileResolver fileResolver = FileResolverFactory.getFileResolver();
        FileResolverUtil.addClassPathPattern(fileResolver);
        fileResolver.addIncludePathPattern(TINY_JAR_PATTERN);
        addIncludePathPatterns(fileResolver, includePathPatterns);
        FileResolverLoadUtil.loadFileResolverConfig(fileResolver, applicationConfig);
        //加载本地
        LocalPropertiesFileProcessor localFileProcessor = new LocalPropertiesFileProcessor(applicationConfig);
        localFileProcessor.start();

        //远程配置
        if (remoteConfigReadClient != null) {
            RemoteConfigHandler remoteConfig = new RemoteConfigHandler(applicationConfig, remoteConfigReadClient);
            remoteConfig.start();
        }
        ConfigManagerFactory.getManager().addLast(ConfigManagerFactory.TYPE_ENVVAR,System.getenv());//在配置里增加环境变量配置

        //仅替换配置
        ConfigurationUtil.getConfigurationManager().replace();
//        MergePropertiesFileProcessor mergeProcessor = new MergePropertiesFileProcessor();
//        mergeProcessor.start();

        fileResolver.addFileProcessor(new SpringBeansFileProcessor());

        fileResolver.addFileProcessor(new ConfigurationFileProcessor());
        // SpringUtil.regSpringConfigXml(xmlFile);
        fileResolver.resolve();
    }



    private static void addIncludePathPatterns(FileResolver fileResolver, List<String> includePathPatterns) {
        if (includePathPatterns == null) {
            return;
        }
        for (String pattern : includePathPatterns) {
            fileResolver.addIncludePathPattern(pattern);
        }
    }

    public static void stop() {
        if (init) {
            application.stop();
        }
    }

}
