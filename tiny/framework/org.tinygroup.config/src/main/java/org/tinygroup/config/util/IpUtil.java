/**
 *
 * Copyright (c) 2014-2018 All Rights Reserved.
 */
package org.tinygroup.config.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 
 * @author zhangliang08072
 * @version $Id: IpUtil.java, v 0.1 2018-1-2 下午5:13:37 zhangliang08072 Exp $
 */
public class IpUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(IpUtil.class);

	public static InetAddress getLocalHostLanAddress() throws Exception {
		try {
			InetAddress candidateAddress = null;
			// 遍历所有的网络接口
			for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces
					.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces
						.nextElement();
				// 在所有的接口下再遍历IP
				for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs
						.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs
							.nextElement();
					if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
						if (inetAddr.isSiteLocalAddress()) {
							// 如果是site-local地址，就是它了
							return inetAddr;
						} else if (candidateAddress == null) {
							// site-local类型的地址未被发现，先记录候选地址
							candidateAddress = inetAddr;
						}
					}
				}
			}
			if (candidateAddress != null) {
				return candidateAddress;
			}
			// 如果没有发现 non-loopback地址.只能用最次选的方案
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			return jdkSuppliedAddress;
		} catch (Exception e) {
			LOGGER.logMessage(LogLevel.ERROR, "客户端获取InetAddress对象失败", e);
			throw e;
		}
	}
	
	public static String getLocalIp(){
		try {
			return getLocalHostLanAddress().getHostAddress();
		} catch (Exception e) {
			LOGGER.logMessage(LogLevel.ERROR, "获取本机IP失败", e);
			return "unkonwn_ip";
		}
	}
	
	public static String getHostName(){
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			LOGGER.logMessage(LogLevel.ERROR, "获取本机主机名失败", e);
			return "unkonwn_hostname";
		}
	}
}
