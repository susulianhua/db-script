/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * <p>
 *       http://www.gnu.org/licenses/gpl.html
 * <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 *
 */
package org.tinygroup.remoteconfig.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.tinygroup.config.ConfigManagerFactory;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.remoteconfig.RemoteConfigReadClient;
import org.tinygroup.remoteconfig.config.ConfigValue;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

/**
 * @author yanwj
 */
public class RemoteConfigHandler {
    public static final String REMOTE_CONFIG_PATH = "/application/application-properties/remoteconfig";
    public static final String REMOTE_CONFIG_PATH_ATTRIBUTE = "enable";
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteConfigHandler.class);
    RemoteConfigReadClient remoteConfigReadClient;
    String applicationXML;

    public RemoteConfigHandler(String applicationXML, RemoteConfigReadClient remoteConfigReadClient) {
        this.applicationXML = applicationXML;
        this.remoteConfigReadClient = remoteConfigReadClient;
    }

    public void start() {
        if (StringUtils.isNotBlank(applicationXML)) {
            XmlNode xmlNode = loadRemoteConfig(applicationXML);
            if (xmlNode == null) {
                return;
            }
            //如果不存在全局配置
            if (TinyConfigParamUtil.getEnable() == null) {
            	String enable = xmlNode.getAttribute(REMOTE_CONFIG_PATH_ATTRIBUTE);
            	LOGGER.logMessage(LogLevel.INFO, "配置中心配置项本地启用状态[{0}]" ,enable);
            	if (!StringUtils.equalsIgnoreCase(enable, "true")) {
            		return;
            	}
			}else {//如果存在全局配置
				//配置开关false
				if (!TinyConfigParamUtil.getEnable()) {
					return;
				}
			}
        }
        LOGGER.logMessage(LogLevel.INFO, "开始启动远程配置处理器");
        remoteConfigReadClient.start();
        LOGGER.logMessage(LogLevel.INFO, "远程配置处理器启动完成");
        LOGGER.logMessage(LogLevel.INFO, "开始载入远程配置信息");
        Map<String,String> remoteMap = new HashMap<String,String>();
        remoteConfigReadClient.retrieveRemoteConfig(remoteMap);
        if(remoteMap.size()>0){
        	ConfigManagerFactory.getManager().addLast(ConfigManagerFactory.TYPE_REMOTE, remoteMap);
        }
        LOGGER.logMessage(LogLevel.INFO, "载入远程配置信息完成");
        TinyConfigParamUtil.setClientId(ClientIdUtil.getClientId());//设置客户端ID
        TinyConfigParamUtil.setLastUpdateConfigTime(new Date());
        TinyConfigParamUtil.setVersion(1);
        //向配置中心发起注册
        remoteConfigReadClient.registerClientWatchPublish(TinyConfigParamUtil.getClientId());
    }

    private XmlNode loadRemoteConfig(String applicationConfig) {
        XmlStringParser parser = new XmlStringParser();
        XmlNode root = parser.parse(applicationConfig).getRoot();
        PathFilter<XmlNode> filter = new PathFilter<XmlNode>(root);
        XmlNode appConfig = filter
                .findNode(RemoteConfigHandler.REMOTE_CONFIG_PATH);
        return appConfig;
    }

}
