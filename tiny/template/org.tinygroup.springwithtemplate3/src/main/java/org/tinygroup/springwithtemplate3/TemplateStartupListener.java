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
package org.tinygroup.springwithtemplate3;

import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.tinygroup.application.Application;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.FileResolverFactory;
import org.tinygroup.fileresolver.FileResolverUtil;
import org.tinygroup.fileresolver.impl.ConfigurationFileProcessor;
import org.tinygroup.fileresolver.impl.LocalPropertiesFileProcessor;
import org.tinygroup.fileresolver.impl.MergePropertiesFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.springutil.ExtendsSpringBeanContainer;
import org.tinygroup.springutil.fileresolver.SpringBeansFileProcessor;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

/**
 * 模板引擎侦听器
 *
 * @author yancheng11334
 *
 */
public class TemplateStartupListener implements ServletContextListener {

    private static Logger logger = LoggerFactory
            .getLogger(TemplateStartupListener.class);

    private Application application;

    public void contextInitialized(ServletContextEvent sce) {
        logger.logMessage(LogLevel.INFO, "初始化WEB应用开始...");
        ServletContext servletContext = sce.getServletContext();
        ServletContextUtil.setServletContext(servletContext);
        loadApplicationConfig(servletContext);
        loadSpring(servletContext);
        startApplication();
        logger.logMessage(LogLevel.INFO, "初始化WEB应用完成.");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        logger.logMessage(LogLevel.INFO, "清理WEB应用开始...");
        stopApplication();
        ServletContextUtil.setServletContext(null);
        logger.logMessage(LogLevel.INFO, "清理WEB应用完成.");
    }

    protected void loadApplicationConfig(ServletContext servletContext) {
        logger.logMessage(LogLevel.INFO, "加载application.xml文件开始...");
        InputStream inputStream = null;
        try {
            String webRootPath = servletContext.getRealPath("/");
            if (webRootPath == null) {
                try {
                    webRootPath = servletContext.getResource("/").getFile();
                } catch (MalformedURLException e) {
                    logger.errorMessage("获取WEBROOT失败！", e);
                }
            }
            inputStream = this.getClass().getResourceAsStream("/application.xml");
            if (inputStream == null) {
                inputStream = new FileInputStream(new File(webRootPath + "/classes/application.xml"));
            }

            String applicationConfig = StreamUtil.readText(inputStream, "UTF-8", true);
            if (applicationConfig != null) {

                ConfigurationManager c = ConfigurationUtil
                        .getConfigurationManager();
                XmlNode applicationXml = new XmlStringParser().parse(
                        applicationConfig).getRoot();
                c.setApplicationConfiguration(applicationXml);

                loadProperties(applicationConfig);
            }
        } catch (Exception e) {
            logger.errorMessage("加载application.xml文件发生异常:", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }
        logger.logMessage(LogLevel.INFO, "加载application.xml文件结束.");
    }

    private void loadProperties(String applicationConfig) {
        // 加载本地
        logger.infoMessage("加载application常量配置...开始");
        LocalPropertiesFileProcessor localFileProcessor = new LocalPropertiesFileProcessor(
                applicationConfig);
        localFileProcessor.start();

        // 合并替换配置
        MergePropertiesFileProcessor mergeProcessor = new MergePropertiesFileProcessor();
        mergeProcessor.start();
        logger.infoMessage("加载application常量配置...结束");
    }

    protected void loadSpring(ServletContext servletContext) {
        logger.logMessage(LogLevel.INFO, "加载Spring Bean文件开始...");
        try {
            BeanContainerFactory
                    .initBeanContainer(ExtendsSpringBeanContainer.class
                            .getName());
            ExtendsSpringBeanContainer beanContainer = (ExtendsSpringBeanContainer) BeanContainerFactory
                    .getBeanContainer(getClass().getClassLoader());
            beanContainer
                    .setApplicationContext(createWebApplicationContext(servletContext));
            servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, beanContainer.getBeanContainerPrototype());
            FileResolver fileResolver = createFileResolver();
            fileResolver.resolve();
        } catch (Exception e) {
            logger.errorMessage("加载Spring Bean文件发生异常:", e);
        }
        logger.logMessage(LogLevel.INFO, "加载Spring Bean文件结束.");
    }

    protected FileResolver createFileResolver() {
        FileResolver fileResolver = FileResolverFactory.getFileResolver();
        FileResolverUtil.addClassPathPattern(fileResolver);
        List<String> webClasses = FileResolverUtil.getWebClasses();
        fileResolver.addResolvePath(webClasses);
        logger.logMessage(LogLevel.INFO, "getWebClasses:{0}", webClasses);
        try {
            List<String> webLibJars = FileResolverUtil
                    .getWebLibJars(fileResolver);
            fileResolver.addResolvePath(webLibJars);
            logger.logMessage(LogLevel.INFO, "getWebLibJars:{0}", webLibJars);
        } catch (Exception e) {
            logger.errorMessage("为文件扫描器添加webLibJars时出现异常", e);
        }
        fileResolver.addFileProcessor(new SpringBeansFileProcessor());
        fileResolver.addFileProcessor(new ConfigurationFileProcessor());
        return fileResolver;
    }

    protected void startApplication() {
        application = BeanContainerFactory.getBeanContainer(
                getClass().getClassLoader()).getBean("springApplication");
        application.init();
        application.start();
    }

    protected void stopApplication() {
        application.stop();
    }

    protected AbstractRefreshableConfigApplicationContext createWebApplicationContext(
            ServletContext servletContext) {
        XmlWebApplicationContext applicationContext = new XmlWebApplicationContext();
        applicationContext.setServletContext(servletContext);
        return applicationContext;
    }

}
