/**
 *
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package org.tinygroup.remoteconfig;

/**
 * 远程配置项变更监控
 * @author zhangliang08072
 * @version $Id: RemoteConfigPublishWatch.java, v 0.1 2017-12-20 下午2:51:18 zhangliang08072 Exp $
 */
public interface RemoteConfigPublishWatch {

	/**
	 * 应用启动，向配置中心发起监控注册
	 * @param clientId 客户端ID
	 */
	public void registerClientWatchPublish(final String clientId);
}
