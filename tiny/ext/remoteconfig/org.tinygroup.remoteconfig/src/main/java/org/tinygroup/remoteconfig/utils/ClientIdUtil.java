/**
 *
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package org.tinygroup.remoteconfig.utils;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.ConfigManagerFactory;
import org.tinygroup.config.util.IpUtil;
import org.tinygroup.config.util.TinyConfigConstants;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 生成客户端应用ID（ip+时间）
 * 
 * @author zhangliang08072
 * @version $Id: ClientIdUtil.java, v 0.1 2017-12-19 下午4:24:52 zhangliang08072
 *          Exp $
 */
public class ClientIdUtil {
	//private static final Logger LOGGER = LoggerFactory.getLogger(ClientIdUtil.class);
	
	/**
	 * 生成客户端ID ip+nodename+时间
	 * @param nodeName 客户端名称
	 * @return
	 */
	public static String getClientId(){
		String ip = IpUtil.getLocalIp();
		String nodeName = ConfigManagerFactory.getManager().getConfig(TinyConfigConstants.TINY_APP_NAME);
		if(StringUtil.isBlank(nodeName))
			throw new RuntimeException("app's nodename is null");
		else
			return ip+"_"+nodeName;
	}
}
