/**
 *
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package org.tinygroup.remoteconfig;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Assert;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.zk.manager.impl.ZKConfigClientImpl;
import org.tinygroup.tinyrunner.Runner;

/**
 * 本测试用来测试自定义zookeeper数据存储路径设置
 * @author zhangliang08072
 * @version $Id: GeneratorServiceEnvTest.java, v 0.1 2017-12-13 上午8:34:27 zhangliang08072 Exp $
 * 
 * 1、在remoteconfig.properties文件中如下设置，测试完毕恢复原来remoteconfig.properties文件中设置。
 * urls=10.20.27.247:2181
 * app=project2
 * version=ver-1
 * env=test_env
 * zookeeper_rootpath=/hello/boy
 * 
 * 
 * 
 * 2、在管理台的remoteconfig.properties文件中设置
 * zookeeper_rootpath=/hello/boy
 * 
 * 3、管理台导入remoteconfig.txt
 */
public class ZookeeperRootPathTest {
	private final static Logger LOGGER = LoggerFactory.getLogger(ZookeeperRootPathTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Runner.setRemoteConfigReadClient(new ZKConfigClientImpl());
        Runner.init("application_rootpath.xml", new ArrayList<String>());

        testApplicationProEnv();
	}

	public static void testApplicationProEnv() {
        Map<String, String> propertyMap = ConfigurationUtil.getConfigurationManager().getConfiguration();
		for (String key : propertyMap.keySet()) {
			LOGGER.logMessage(LogLevel.INFO, "key:{},value:{}", key, propertyMap.get(key));
		}
		Assert.assertEquals("whaha", propertyMap.get("whaha"));
    }
}
