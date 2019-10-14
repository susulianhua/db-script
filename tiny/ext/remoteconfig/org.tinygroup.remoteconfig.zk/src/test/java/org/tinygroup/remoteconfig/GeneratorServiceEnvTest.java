/**
 *
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package org.tinygroup.remoteconfig;

import java.util.ArrayList;
import java.util.Map;

import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.zk.manager.impl.ZKConfigClientImpl;
import org.tinygroup.tinyrunner.Runner;

/**
 * 
 * @author zhangliang08072
 * @version $Id: GeneratorServiceEnvTest.java, v 0.1 2017-12-13 上午8:34:27 zhangliang08072 Exp $
 */
public class GeneratorServiceEnvTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(GeneratorServiceEnvTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Runner.setRemoteConfigReadClient(new ZKConfigClientImpl());
        Runner.init("application_env.xml", new ArrayList<String>());

        testApplicationProEnv();
	}

	public static void testApplicationProEnv() {
        Map<String, String> propertyMap = ConfigurationUtil.getConfigurationManager().getConfiguration();
		for (String key : propertyMap.keySet()) {
			LOGGER.logMessage(LogLevel.INFO, "key:{},value:{}", key, propertyMap.get(key));
		}
    }
}
