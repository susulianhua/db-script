/**
 *
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package org.tinygroup.remoteconfig.zk.manager.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.ConfigPublishItem;
import org.tinygroup.remoteconfig.manager.PublishManager;
import org.tinygroup.remoteconfig.zk.client.ZKPublishManager;
import org.tinygroup.remoteconfig.zk.utils.PathHelper;

/**
 * 
 * @author zhangliang08072
 * @version $Id: PublishManagerImpl.java, v 0.1 2017-12-22 下午9:46:25 zhangliang08072 Exp $
 */
public class PublishManagerImpl implements PublishManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(PublishManagerImpl.class);

	/** 
	 * @see org.tinygroup.remoteconfig.manager.PublishManager#getConfigPublishItems(org.tinygroup.remoteconfig.config.ConfigPath)
	 */
	@Override
	public Map<String, ConfigPublishItem> getConfigPublishItems(ConfigPath configPath) {
		String node = PathHelper.createPublishPath("", configPath);
		Map<String, ConfigPublishItem> m = new HashMap<String, ConfigPublishItem>();
		LOGGER.logMessage(LogLevel.INFO, "远程配置，获取环境[envpath={0}]上的配置发布项", node);
        try {
        	m = ZKPublishManager.getConfigPublishItems(configPath);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，获取环境[envpath={0}]上的配置发布项失败", e, node);
        }
        return m;
	}

	/** 
	 * @see org.tinygroup.remoteconfig.manager.PublishManager#publishConfigToClient(java.lang.String, org.tinygroup.remoteconfig.config.ConfigPath)
	 */
	@Override
	public void publishConfigToClient(String clientId, ConfigPath envPath) {
		envPath.setModulePath(null);
		try{
			ConfigPublishItem item = ZKPublishManager.get(clientId, envPath);
			if(item == null){
				LOGGER.logMessage(LogLevel.WARN, "[clientId={0},envpath={1}]对应客户端不存在，发布配置到客户端失败。",clientId, envPath);
				return ;
			}
			item.setPublishStatus(true);
			item.setPublishTime(new Date());
			item.addVersion();
			ZKPublishManager.update(clientId, item, envPath);
		}catch (BaseRuntimeException e) {
            LOGGER.logMessage(LogLevel.ERROR, "[clientId={0},envpath={1}]发布配置到客户端失败", e,clientId, envPath);
            throw e;
        }
	}
	
	public void updateStatusOfPublishItem(String clientId,ConfigPublishItem item,ConfigPath envPath){
		envPath.setModulePath(null);
		try{
			ZKPublishManager.update(clientId, item, envPath);
		}catch (BaseRuntimeException e) {
            LOGGER.logMessage(LogLevel.ERROR, "[clientId={0},envpath={1}]更新配置发布项状态失败", e,clientId, envPath);
            throw e;
        }
	}
}
