/**
 *
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package org.tinygroup.remoteconfig.zk.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.config.ConfigPublishItem;
import org.tinygroup.remoteconfig.utils.TinyConfigParamUtil;

/**
 * 当session timeout时的处理。重新创建配置项监控节点
 * @author zhangliang08072
 * @version $Id: SessionConnectionListener.java, v 0.1 2017-12-25 下午1:52:24 zhangliang08072 Exp $
 */
public class SessionConnectionListener implements ConnectionStateListener {
	protected static final Logger LOGGER = LoggerFactory.getLogger(SessionConnectionListener.class);
	/** 
	 * @see org.apache.curator.framework.state.ConnectionStateListener#stateChanged(org.apache.curator.framework.CuratorFramework, org.apache.curator.framework.state.ConnectionState)
	 */
	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		if(newState == ConnectionState.RECONNECTED){
			LOGGER.infoMessage("====>zk session重连成功");
            try {
            	if(ZKPublishManager.getNodeCache()==null){
            		throw new RuntimeException("客户端初始启动时就未能向注册中心注册成功！");
            	}
            	ConfigPublishItem item = new ConfigPublishItem();
                item.setPublishStatus(true);
                item.setPublishTime(TinyConfigParamUtil.getLastUpdateConfigTime());
                item.setVersion(TinyConfigParamUtil.getVersion());
                ZKPublishManager.set(TinyConfigParamUtil.getClientId(), item, ZKPublishManager.getConfigPath());
            	
            	LOGGER.infoMessage("====>zk session重连成功，客户端恢复注册成功！");
            }catch (Exception e){
            	LOGGER.infoMessage("====>zk session重连成功，客户端恢复注册失败",e);
            }
        }
//		if(newState == ConnectionState.LOST){
//			LOGGER.infoMessage("====>zk session超时");
//            while(true){
//                try {
//                    if(client.getZookeeperClient().blockUntilConnectedOrTimedOut()){
//                    	ConfigPublishItem item = new ConfigPublishItem();
//                        item.setPublishStatus(true);
//                        item.setPublishTime(TinyConfigParamUtil.getLastUpdateConfigTime());
//                        ZKPublishManager.set(TinyConfigParamUtil.getClientId(), item, ZKPublishManager.getConfigPath());
//                    	
//                    	LOGGER.infoMessage("====>zk session重连成功，客户端恢复注册成功！");
//                        break;
//                    }
//                } catch (InterruptedException e) {
//                	LOGGER.infoMessage("====>zk session重连失败，客户端恢复注册失败",e);
//                    break;
//                } catch (Exception e){
//                	LOGGER.infoMessage("====>zk session重连失败，客户端恢复注册失败",e);
//                	break;
//                }
//            }
//        }
	}

}
