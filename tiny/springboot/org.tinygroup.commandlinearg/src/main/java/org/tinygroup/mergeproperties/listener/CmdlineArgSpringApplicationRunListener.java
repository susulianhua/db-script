package org.tinygroup.mergeproperties.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.tinygroup.config.ConfigManagerFactory;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class CmdlineArgSpringApplicationRunListener implements SpringApplicationRunListener {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CmdlineArgSpringApplicationRunListener.class);
    String[] args;

    public CmdlineArgSpringApplicationRunListener(SpringApplication application, String[] args) {
    	this.args = args;
    }

    @Override
    public void starting() {
        LOGGER.infoMessage("开始CmdlineArgSpringApplicationRunListener...");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
    	Map<String,String> commandArgsMap = parseCommandArgsMap(args);
    	ConfigManagerFactory.getManager().addLast(ConfigManagerFactory.TYPE_COMMANDLINE,commandArgsMap);//在配置里增加命令行参数配置
    }
    
    private Map<String,String> parseCommandArgsMap(String[] args){
    	LOGGER.infoMessage("CmdlineArgSpringApplicationRunListener开始处理命令行参数：{}",Arrays.toString(args));
    	Map<String,String> result = new HashMap<String,String>();//--param=xxx这样的标准springboot命令行参数
    	for (String arg : args) {
    		 if (arg.startsWith("--")) {
    				String optionText = arg.substring(2, arg.length());
    				String optionName;
    				String optionValue = null;
    				 if (optionText.contains("=")) {
    					 optionName = optionText.substring(0, optionText.indexOf("="));
    					 optionValue = optionText.substring(optionText.indexOf("=")+1, optionText.length());
    				 }else{
    					 optionName = optionText;
    					 optionValue = "";
    				 }
    				 result.put(optionName, optionValue);
    		 }
    	}
    	return result;
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
