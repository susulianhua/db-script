/**
 *
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package org.tinygroup.remoteconfig.zk.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.ConfigPublishItem;
import org.tinygroup.remoteconfig.zk.utils.PathHelper;
import org.tinygroup.remoteconfig.zk.utils.SerializeUtil;

/**
 * 发布监控manager
 * 
 * @author zhangliang08072
 * @version $Id: ZKPublishManager.java, v 0.1 2017-12-19 下午8:46:29
 *          zhangliang08072 Exp $
 */
public class ZKPublishManager extends BaseManager {
	private static final Logger LOGGER = LoggerFactory
            .getLogger(ZKPublishManager.class);

	private static NodeCache nodeCache = null;
	private static ConfigPath configPath = null;
	private static boolean watchFlag = false;
	private static boolean sessionTimeoutHandlerFlag = false;

	/**
	 * 生成或修改配置项变更的监控节点
	 * 
	 * @param clientId
	 *            客户端Id
	 * @param item
	 *            节点存储数据
	 * @param configPath
	 *            客户端对应的环境
	 */
	public static void set(String clientId, ConfigPublishItem item,
			ConfigPath configPath) {
		String node = PathHelper.createPublishPath(clientId, configPath);
		try {
			Stat stat = curator.checkExists().forPath(node);
			if (stat == null) {
				curator.create().creatingParentsIfNeeded()
						.withMode(CreateMode.EPHEMERAL)
						.forPath(node, SerializeUtil.serialize(item));
				return;
			}
			curator.setData().forPath(node, SerializeUtil.serialize(item));
		} catch (Exception e) {
			throw new BaseRuntimeException("0TE120119015", e, node, item);
		}
	}

	/**
	 * 仅修改配置项变更的监控节点
	 * 
	 * @param clientId
	 * @param item
	 * @param configPath
	 */
	public static void update(String clientId, ConfigPublishItem item,
			ConfigPath configPath) {
		String node = PathHelper.createPublishPath(clientId, configPath);
		try {
			curator.setData().forPath(node, SerializeUtil.serialize(item));
		} catch (Exception e) {
			throw new BaseRuntimeException("0TE120119018", e, node, item);
		}
	}

	public static ConfigPublishItem get(String clientId, ConfigPath configPath) {
		String node = null;
		try {
			node = PathHelper.createPublishPath(clientId, configPath);
			return getSimple(node);
		} catch (Exception e) {
			throw new BaseRuntimeException("0TE120119003", e, node);
		}
	}

	public static Map<String, ConfigPublishItem> getConfigPublishItems(
			ConfigPath configPath) {
		Map<String, ConfigPublishItem> m = new HashMap<String, ConfigPublishItem>();
		String node = PathHelper.createPublishPath("", configPath);
		try {
			List<String> subNodes = curator.getChildren().forPath(node);
			if (subNodes != null && !subNodes.isEmpty()) {
				for (String subNode : subNodes) {
					ConfigPublishItem item = getSimple(node.concat("/").concat(
							subNode));
					if (item != null) {
						m.put(subNode, item);
					}
				}
			}
		} catch (Exception e) {
			// 当没有找到子项时，会抛出此异常，我们不做任何处理
		}
		return m;
	}

	/**
	 * 发起监控
	 * 
	 * @param clientId
	 * @param listener
	 */
	public static void startWatch(String clientId, NodeCacheListener listener) {
		String node = PathHelper.createPublishPath(clientId, getConfigPath());
		if (nodeCache == null) {
			nodeCache = new NodeCache(curator, node);
		}
		nodeCache.getListenable().clear();
		nodeCache.getListenable().addListener(listener);

		if (watchFlag == false) {
			try {
				nodeCache.start();
				watchFlag = true;
			} catch (Exception e) {
				throw new BaseRuntimeException("0TE120119016", e, node);
			}
		}else{
			LOGGER.warnMessage("远程配置，请不要重复发起针对节点[{}]的监听", node);
		}
	}

	/**
	 * 停止监控配置项发布
	 */
	public static void stopWatch() {
		if (nodeCache != null) {
			try {
				nodeCache.close();
			} catch (IOException e) {
				throw new BaseRuntimeException("0TE120119017", e,
						nodeCache.getCurrentData() == null ? "" : nodeCache
								.getCurrentData().getPath());
			} finally {
				nodeCache = null;
			}
		}
	}

	private static ConfigPublishItem getSimple(String key) throws Exception {
		Stat stat = curator.checkExists().forPath(key);
		if (stat != null) {
			byte[] resultData = curator.getData().forPath(key);
			if (resultData != null) {
				Object obj = SerializeUtil.unserialize(resultData);
				if (obj instanceof ConfigPublishItem) {
					return (ConfigPublishItem) obj;
				}
			}
		}
		return null;
	}

	public static void addSessionTimeoutHandler(ConnectionStateListener listener) {
		if(sessionTimeoutHandlerFlag == false){
			curator.getConnectionStateListenable().addListener(listener);
			sessionTimeoutHandlerFlag = true;
		}else{
			LOGGER.warnMessage("远程配置，请不要重复发起zookeeper session失效的补偿处理");
		}
	}

	public static void setConfigPath(ConfigPath configPath) {
		ZKPublishManager.configPath = configPath;
	}

	public static ConfigPath getConfigPath() {
		return configPath;
	}

	public static NodeCache getNodeCache() {
		return nodeCache;
	}
}
