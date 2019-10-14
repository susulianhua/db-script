package org.tinygroup.localproperties.listener;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.config.ConfigurationManager;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.FileResolver;
import org.tinygroup.fileresolver.FileResolverUtil;
import org.tinygroup.fileresolver.impl.FileResolverImpl;
import org.tinygroup.fileresolver.impl.I18nFileProcessor;
import org.tinygroup.fileresolver.impl.LocalPropertiesFileProcessor;
import org.tinygroup.fileresolver.util.FileResolverLoadUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

public class LocalProSpringApplicationRunListener implements SpringApplicationRunListener {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(LocalProSpringApplicationRunListener.class);
    String applicationConfig;

    public LocalProSpringApplicationRunListener(SpringApplication application, String[] args) {
    }

    @Override
    public void starting() {
        LOGGER.infoMessage("开始LocalProSpringApplicationRunListener...");
        InputStream inputStream = this.getClass().getResourceAsStream("/application.xml");
        try {
            applicationConfig = StreamUtil.readText(inputStream, "UTF-8", true);
            startI18nProcess();
            ConfigurationManager c = ConfigurationUtil
                    .getConfigurationManager();
            XmlNode applicationXml = new XmlStringParser().parse(
                    applicationConfig).getRoot();
            c.setApplicationConfiguration(applicationXml);

        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        LocalPropertiesFileProcessor localFileProcessor = new LocalPropertiesFileProcessor(
                applicationConfig);
        localFileProcessor.start();
    }

    /**
     * 初始化国际化
     */
    private void startI18nProcess() {
        //FileResolver fileResolver = FileResolverFactory.getFileResolver();
    	FileResolver fileResolver = new FileResolverImpl();
        FileResolverUtil.addClassPathPattern(fileResolver);
        FileResolverLoadUtil.loadFileResolverConfig(fileResolver, applicationConfig);
        fileResolver.addFileProcessor(new I18nFileProcessor());
        fileResolver.resolve();
    }


    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {

    }
}
