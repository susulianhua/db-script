/**
 *
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package org.tinygroup.remoteconfig.manager;

import java.util.Map;

import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.ConfigPublishItem;

/**
 * 
 * @author zhangliang08072
 * @version $Id: PublishManager.java, v 0.1 2017-12-22 下午9:43:54 zhangliang08072 Exp $
 */
public interface PublishManager {

	/**
	 * 获取环境下的所有配置发布项
	 * @param configPath
	 * @return
	 */
	public Map<String,ConfigPublishItem> getConfigPublishItems(ConfigPath configPath);
	
	/**
	 * 发布环境下的某个配置发布项
	 * @param clientId
	 * @param envPath
	 */
	public void publishConfigToClient(String clientId,ConfigPath envPath);
	
	/**
	 * 更新某个配置发布项的发布状态
	 * @param clientId
	 * @param item
	 * @param envPath
	 */
	public void updateStatusOfPublishItem(String clientId,ConfigPublishItem item,ConfigPath envPath);
}
