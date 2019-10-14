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
package org.tinygroup.weblayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.WebApplicationContext;
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
import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.fileresolver.impl.ConfigurationFileProcessor;
import org.tinygroup.fileresolver.impl.FileResolverImpl;
import org.tinygroup.fileresolver.impl.I18nFileProcessor;
import org.tinygroup.fileresolver.impl.LocalPropertiesFileProcessor;
import org.tinygroup.fileresolver.util.FileResolverLoadUtil;
import org.tinygroup.loader.LoaderManager;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.remoteconfig.RemoteConfigReadClient;
import org.tinygroup.remoteconfig.utils.RemoteConfigHandler;
import org.tinygroup.remoteconfig.utils.TinyConfigParamUtil;
import org.tinygroup.springmerge.beanfactory.SpringMergeApplicationContext;
import org.tinygroup.springutil.ExtendsSpringBeanContainer;
import org.tinygroup.springutil.SpringBootBeanContainer;
import org.tinygroup.springutil.fileresolver.SpringBeansFileProcessor;
import org.tinygroup.vfs.VFS;
import org.tinygroup.weblayer.configmanager.TinyListenerConfigManagerHolder;
import org.tinygroup.weblayer.listener.ServletContextHolder;
import org.tinygroup.weblayer.listener.TinyServletContext;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;
import org.tinygroup.xstream.XStreamFactory;

public class ApplicationStartupListener implements ServletContextListener {
    public static final String TINY_SPRING_CONTAINER_KEY = "tinySpringContainer";// springmvc创建spring容器实例会
    private static Logger logger = LoggerFactory
            .getLogger(ApplicationStartupListener.class);
    private Application application = null;

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
            logger.logMessage(LogLevel.INFO, "WEB 应用停止中...");
            if (application != null) {
                application.stop();
            }
            destroyContextListener(servletContextEvent);
            ConfigurationUtil.clear();
            ServletContextHolder.getServletContext().removeAttribute(
                    TINY_SPRING_CONTAINER_KEY);
            ServletContextHolder.clear();
            logger.logMessage(LogLevel.INFO, "关闭spring容器开始");
            BeanContainer<?> container = BeanContainerFactory
                    .getBeanContainer(this.getClass().getClassLoader());
            if (container instanceof ExtendsSpringBeanContainer) {
                ((ExtendsSpringBeanContainer) container).clear();
            }
            logger.logMessage(LogLevel.INFO, "关闭spring容器结束");
            BeanContainerFactory.destroy();
            FileResolverFactory.destroyFileResolver();// 关闭容器的时候设置扫描spring配置文件的扫码器实例为null
            logger.logMessage(LogLevel.INFO, "WEB 远程配置停止中...");
            TinyConfigParamUtil.setReadClient(null);
            logger.logMessage(LogLevel.INFO, "WEB 远程配置停止完成。");
            VFS.clearCache();
            LoggerFactory.clearAllLoggers();
            LoaderManager.clear();
            XStreamFactory.clear();
            LogFactory.release(Thread.currentThread().getContextClassLoader());
            logger.logMessage(LogLevel.INFO, "WEB 应用停止完成。");
        } catch (Exception e) {
            logger.errorMessage("tiny应用关闭出错：", e);
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        TinyServletContext servletContext = new TinyServletContext(
                servletContextEvent.getServletContext());
        ServletContextHolder.setServletContext(servletContext);
        Enumeration<String> enumeration = servletContextEvent
                .getServletContext().getAttributeNames();
        logger.logMessage(LogLevel.INFO, "WEB环境属性开始");
        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            logger.logMessage(LogLevel.INFO, "{0}=[{1}]", key,
                    servletContextEvent.getServletContext().getAttribute(key));
        }
        logger.logMessage(LogLevel.INFO, "WEB 应用启动中...");

        logger.logMessage(LogLevel.INFO, "WEB 应用信息：[{0}]", servletContextEvent
                .getServletContext().getServerInfo());
        String webRootPath = servletContextEvent.getServletContext()
                .getRealPath("/");
        if (webRootPath == null) {
            try {
                webRootPath = servletContextEvent.getServletContext()
                        .getResource("/").getFile();
            } catch (MalformedURLException e) {
                logger.errorMessage("获取WEBROOT失败！", e);
            }
        }
        logger.logMessage(LogLevel.INFO, "TINY_WEBROOT：[{0}]", webRootPath);
        ConfigurationUtil.getConfigurationManager().setConfiguration(
                "TINY_WEBROOT", webRootPath);
        logger.logMessage(LogLevel.INFO, "应用参数<TINY_WEBROOT>=<{}>", webRootPath);

        logger.logMessage(LogLevel.INFO, "ServerContextName：[{0}]",
                servletContextEvent.getServletContext().getServletContextName());
        logger.logMessage(LogLevel.INFO, "WEB环境属性结束");

        InputStream inputStream = this.getClass().getResourceAsStream(
                "/application.xml");
        if (inputStream == null) {
            try {
                File file = new File(webRootPath + "/classes/application.xml");
                inputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                logger.errorMessage("获取配置文件失败，错误原因：！", e, e.getMessage());
            }
        }

        if (inputStream != null) {
            String applicationConfig = "";
            try {
                application = new ApplicationDefault();
                applicationConfig = StreamUtil.readText(inputStream, "UTF-8",
                        true);
                if (applicationConfig != null) {
                    ConfigurationManager c = ConfigurationUtil
                            .getConfigurationManager();
                    XmlNode applicationXml = new XmlStringParser().parse(
                            applicationConfig).getRoot();
                    c.setApplicationConfiguration(applicationXml);

                }
                loadProperties(applicationConfig);

                loadSpringBeans(applicationConfig, servletContext);

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
                logger.errorMessage("载入应用配置信息时出现异常，错误原因：{}！", e, e.getMessage());
                throw new RuntimeException(e);
            }
            try {
                logger.logMessage(LogLevel.INFO, "启动应用开始...");
                application.init();
                application.start();
                FullContextFileRepository fileRepository = BeanContainerFactory
                        .getBeanContainer(this.getClass().getClassLoader())
                        .getBean(
                                FullContextFileRepository.FILE_REPOSITORY_BEAN_NAME);
                servletContext.setFullContextFileRepository(fileRepository);// 设置上下文关联的全文搜索对象
                initContextListener(servletContextEvent);
                logger.logMessage(LogLevel.INFO, "WEB 应用启动完成。");
            } catch (Exception e) {
                logger.errorMessage("tiny WEB应用启动出错:", e);
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * 执行其他ContextListener
     */
    private void initContextListener(ServletContextEvent servletContextEvent) {
        List<ServletContextListener> contextListeners = TinyListenerConfigManagerHolder
                .getInstance().getContextListeners();
        for (ServletContextListener servletContextListener : contextListeners) {
            logger.logMessage(LogLevel.DEBUG,
                    "ServletContextListener:[{0}] will be Initialized",
                    servletContextListener);
            servletContextListener.contextInitialized(servletContextEvent);
            logger.logMessage(LogLevel.DEBUG,
                    "ServletContextListener:[{0}] Initialized",
                    servletContextListener);
        }
    }

    /**
     * 执行其他ContextListener
     */
    private void destroyContextListener(ServletContextEvent servletContextEvent) {
        List<ServletContextListener> contextListeners = TinyListenerConfigManagerHolder
                .getInstance().getContextListeners();
        for (ServletContextListener servletContextListener : contextListeners) {
            logger.logMessage(LogLevel.DEBUG,
                    "ServletContextListener:[{0}] will be Destroyed",
                    servletContextListener);
            servletContextListener.contextDestroyed(servletContextEvent);
            logger.logMessage(LogLevel.DEBUG,
                    "ServletContextListener:[{0}] Destroyed",
                    servletContextListener);
        }
    }

    private void loadSpringBeans(String applicationConfig, ServletContext servletContext) {

        logger.logMessage(LogLevel.INFO, "加载Spring Bean文件开始...");
        Object rootContext = servletContext.getAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        if (rootContext == null) {//之前未定义spring容器，则创建tiny框架相关的spring容器
            BeanContainerFactory.initBeanContainer(ExtendsSpringBeanContainer.class
                    .getName());
            ExtendsSpringBeanContainer beanContainer = (ExtendsSpringBeanContainer) BeanContainerFactory
                    .getBeanContainer(getClass().getClassLoader());
            beanContainer.setApplicationContext(createWebApplicationContext());
            FileResolver fileResolver = FileResolverFactory.getFileResolver();
            FileResolverUtil.addClassPathPattern(fileResolver);
            FileResolverLoadUtil.loadFileResolverConfig(fileResolver, applicationConfig);
            fileResolver.addFileProcessor(new SpringBeansFileProcessor());
            fileResolver.addFileProcessor(new ConfigurationFileProcessor());
            fileResolver.resolve();
        } else {
            BeanContainerFactory.initBeanContainer(SpringBootBeanContainer.class
                    .getName());
            SpringBootBeanContainer beanContainer = (SpringBootBeanContainer) BeanContainerFactory
                    .getBeanContainer(getClass().getClassLoader());
            beanContainer.setApplicationContext((ApplicationContext) rootContext);
            FileResolver fileResolver = FileResolverFactory.getFileResolver();
            FileResolverUtil.addClassPathPattern(fileResolver);
            FileResolverLoadUtil.loadFileResolverConfig(fileResolver, applicationConfig);
            fileResolver.addFileProcessor(new ConfigurationFileProcessor());
            fileResolver.resolve();
        }
        logger.logMessage(LogLevel.INFO, "加载Spring Bean文件结束。");
    }

    protected AbstractRefreshableConfigApplicationContext createWebApplicationContext() {
        BeanDefinitionRegistry mergedBeanDefinitionRegistry = loadMergeBeanDefinition();
        SpringMergeApplicationContext wac = new SpringMergeApplicationContext(
                mergedBeanDefinitionRegistry);
        // Assign the best possible id value.
        wac.setServletContext(ServletContextHolder.getServletContext());
        ServletContextHolder.getServletContext().setAttribute(
                ApplicationStartupListener.TINY_SPRING_CONTAINER_KEY, wac);
        return wac;
    }

    /**
     * @return
     */
    private BeanDefinitionRegistry loadMergeBeanDefinition() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("MERGE-INFO/merge.xml");
        BeanDefinitionRegistry registry = null;
        if (resource.exists()) {
            registry = new XmlBeanFactory(resource);
        } else {
            registry = new DefaultListableBeanFactory();
        }
        return registry;
    }


    /**
     * 加载配置，包含本地配置和远程配置 优先加载本地配置，然后加载远程配置 如果存在key一样的配置，以远程配置为主
     *
     * @param applicationConfig
     */
    private void loadProperties(String applicationConfig) {
        startI18nProcess(applicationConfig);
        // 加载本地
        logger.infoMessage("加载application常量配置...开始");
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
        if(ConfigManagerFactory.getManager().ifExistTargetMapItem(ConfigManagerFactory.TYPE_COMMANDLINE)){//springboot启动方式可能已经添加了命令行参数
        	ConfigManagerFactory.getManager().addBefore(ConfigManagerFactory.TYPE_COMMANDLINE, ConfigManagerFactory.TYPE_ENVVAR, System.getenv());
        }else{
        	ConfigManagerFactory.getManager().addLast(ConfigManagerFactory.TYPE_ENVVAR,System.getenv());//在配置里增加环境变量配置
        }
        // 仅替换配置
        ConfigurationUtil.getConfigurationManager().replace();
//        MergePropertiesFileProcessor mergeProcessor = new MergePropertiesFileProcessor();
//        mergeProcessor.start();
        logger.infoMessage("加载application常量配置...结束");
    }

    private void startI18nProcess(String applicationConfig) {
        FileResolver fileResolver = new FileResolverImpl();
        FileResolverUtil.addClassPathPattern(fileResolver);
        FileResolverLoadUtil.loadFileResolverConfig(fileResolver, applicationConfig);
        fileResolver.addFileProcessor(new I18nFileProcessor());
        fileResolver.resolve();
    }

}
