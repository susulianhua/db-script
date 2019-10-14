/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *
 */
package org.tinygroup.remoteconfig.zk.config;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.tinygroup.exception.BaseRuntimeException;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.zk.client.IRemoteConfigZKConstant;

/**
 * @author yanwj
 */
public class RemoteEnvironment {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(RemoteEnvironment.class);

    private static RemoteConfig config;

    private RemoteEnvironment() {
        LOGGER.logMessage(LogLevel.INFO, "读取本地配置信息...");
        URL url = getClass().getResource(IRemoteConfigZKConstant.REMOTE_CONFIG_NAME);
        if (url != null) {
            LOGGER.logMessage(LogLevel.INFO, String.format("开始解析[%s]", IRemoteConfigZKConstant.REMOTE_CONFIG_NAME));
            Properties pro = new Properties();
            try {
                pro.load(url.openStream());
                config = transObj(pro);
                checkRootPath(config.getZookeeperRootPath());
                IRemoteConfigZKConstant.REMOTE_ROOT_PATH = config.getZookeeperRootPath().trim();
                LOGGER.logMessage(LogLevel.INFO, "============配置信息================");
                LOGGER.logMessage(LogLevel.INFO, config.toString());
                LOGGER.logMessage(LogLevel.INFO, "====================================");
                LOGGER.logMessage(LogLevel.INFO, String.format("解析完成[%s]", IRemoteConfigZKConstant.REMOTE_CONFIG_NAME));
                LOGGER.logMessage(LogLevel.INFO, "本地配置信息读取完毕");
                return;
            } catch (IOException e) {
                throw new RuntimeException("ZK配置初始化失败。。。", e);
            }
        } else {
            throw new BaseRuntimeException("0TE120119001", "配置中心");
        }
    }
    
    private void checkRootPath(String rootPath){
    	if(StringUtils.isBlank(rootPath)){
    		return;
    	}
    	String regexStr = "^/[\\w|/]*[^/]$";
    	Pattern p = Pattern.compile(regexStr);
    	Matcher matcher = p.matcher(rootPath);
    	boolean b = matcher.matches();
    	if(!b){
    		throw new RuntimeException(String.format("%s的值:%s,格式不正确",IRemoteConfigZKConstant.REMOTE_ZOOKEEPER_ROOT,rootPath));
    	}
    }

    public static RemoteConfig load() {
        new RemoteEnvironment();
        return config;
    }

    public static RemoteConfig getConfig() {
        return config;
    }

    private RemoteConfig transObj(Properties pro) {
        String urlStr = pro.get(IRemoteConfigZKConstant.REMOTE_URLS) == null ? "" : pro.get(IRemoteConfigZKConstant.REMOTE_URLS).toString();
        String app = pro.get(IRemoteConfigZKConstant.REMOTE_APP) == null ? "" : pro.get(IRemoteConfigZKConstant.REMOTE_APP).toString();
        String env = pro.get(IRemoteConfigZKConstant.REMOTE_ENV) == null ? "" : pro.get(IRemoteConfigZKConstant.REMOTE_ENV).toString();
        String version = pro.get(IRemoteConfigZKConstant.REMOTE_VERSION) == null ? "" : pro.get(IRemoteConfigZKConstant.REMOTE_VERSION).toString();
        String username = pro.get(IRemoteConfigZKConstant.REMOTE_USERNAME) == null ? "" : pro.get(IRemoteConfigZKConstant.REMOTE_USERNAME).toString();
        String password = pro.get(IRemoteConfigZKConstant.REMOTE_PASSWORD) == null ? "" : pro.get(IRemoteConfigZKConstant.REMOTE_PASSWORD).toString();
        String newEnvAble = pro.get(IRemoteConfigZKConstant.REMOTE_NEW_ENV_ABLE) == null ? "" : pro.get(IRemoteConfigZKConstant.REMOTE_NEW_ENV_ABLE).toString();
        String rootPath = pro.get(IRemoteConfigZKConstant.REMOTE_ZOOKEEPER_ROOT) == null ? "" : pro.get(IRemoteConfigZKConstant.REMOTE_ZOOKEEPER_ROOT).toString();
        boolean status = BooleanUtils.toBoolean(newEnvAble);
        RemoteConfig remoteConfig = new RemoteConfig(urlStr, app, env, version);
        remoteConfig.setNewEnvAble(status);
        remoteConfig.setUsername(username);
        remoteConfig.setPassword(password);
        remoteConfig.setZookeeperRootPath(rootPath);
        return remoteConfig;
    }

}
