package org.tinygroup.mergeproperties.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.tinygroup.config.ConfigManagerFactory;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class MergeProSpringApplicationRunListener implements SpringApplicationRunListener {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MergeProSpringApplicationRunListener.class);

    public MergeProSpringApplicationRunListener(SpringApplication application, String[] args) {
    }

    @Override
    public void starting() {
        LOGGER.infoMessage("开始MergeProSpringApplicationRunListener...");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
    	if(ConfigManagerFactory.getManager().ifExistTargetMapItem(ConfigManagerFactory.TYPE_COMMANDLINE)){//springboot启动方式可能已经添加了命令行参数
        	ConfigManagerFactory.getManager().addBefore(ConfigManagerFactory.TYPE_COMMANDLINE, ConfigManagerFactory.TYPE_ENVVAR, System.getenv());
        }else{
        	ConfigManagerFactory.getManager().addLast(ConfigManagerFactory.TYPE_ENVVAR,System.getenv());//在配置里增加环境变量配置
        }
    	// 仅替换配置
        ConfigurationUtil.getConfigurationManager().replace();
        // 合并替换配置
//        MergePropertiesFileProcessor mergeProcessor = new MergePropertiesFileProcessor();
//        mergeProcessor.start();
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
