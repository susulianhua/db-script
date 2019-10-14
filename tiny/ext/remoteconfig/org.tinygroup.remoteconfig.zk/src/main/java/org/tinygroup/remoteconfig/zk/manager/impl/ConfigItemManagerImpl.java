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
package org.tinygroup.remoteconfig.zk.manager.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.RemoteConfigManageClient;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.ConfigValue;
import org.tinygroup.remoteconfig.config.Environment;
import org.tinygroup.remoteconfig.manager.ConfigItemManager;
import org.tinygroup.remoteconfig.zk.client.BaseManager;
import org.tinygroup.remoteconfig.zk.client.IRemoteConfigZKConstant;
import org.tinygroup.remoteconfig.zk.client.ZKDefaultEnvManager;
import org.tinygroup.remoteconfig.zk.client.ZKManager;
import org.tinygroup.remoteconfig.zk.config.RemoteConfig;
import org.tinygroup.remoteconfig.zk.config.RemoteEnvironment;
import org.tinygroup.remoteconfig.zk.exception.ConfigItemNotExistException;
import org.tinygroup.remoteconfig.zk.utils.PathHelper;


public class ConfigItemManagerImpl implements ConfigItemManager, RemoteConfigManageClient {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ConfigItemManagerImpl.class);

    public boolean exists(String key, ConfigPath configPath) {
        String path = PathHelper.createPath(configPath);
        LOGGER.logMessage(LogLevel.INFO, "远程配置，判断节点是否存在[key={0} ,path={1}]", key, path);
        try {
            boolean status = ZKManager.exists(key, configPath);
            LOGGER.logMessage(LogLevel.INFO, status ? "存在" : "不存在");
            return status;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，判断节点是否存在[key={0} ,path={1}]", e, key, path);
        }
        return false;
    }

    public ConfigValue get(String key, ConfigPath configPath) {
        String path = PathHelper.createPath(configPath);
        LOGGER.logMessage(LogLevel.INFO, "远程配置，获取节点[key={0} ,path={1}]", key, path);
        try {
            ConfigValue value = ZKManager.get(key, configPath);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，节点信息[key={0} ,title={1} ,value={2} ,desc={3}]", key, value.getTitle(), value.getValue(), value.getDesc());
            return value;
        } catch (ConfigItemNotExistException e) {
        	LOGGER.logMessage(LogLevel.WARN,"远程配置，获取节点[key={0} ,path={1}],节点不存在。",key, path);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，获取节点失败[key={0} ,path={1}]", e, key, path);
        }
        return null;
    }

    public Map<String, ConfigValue> getAll(ConfigPath configPath) {
        String path = PathHelper.createPath(configPath);
        LOGGER.logMessage(LogLevel.INFO, "远程配置，批量获取节点[path={0}]", path);
        try {
            Map<String, ConfigValue> valueMap = ZKManager.getAll(configPath);
            if (LOGGER.isEnabled(LogLevel.INFO)) {
                for (String key : valueMap.keySet()) {
                    ConfigValue value = valueMap.get(key);
                    if (value != null) {
                        LOGGER.logMessage(LogLevel.INFO, "远程配置，节点信息[key={0} ,title={1} ,value={2} ,desc={3}]", key, value.getTitle(), value.getValue(), value.getDesc());
                    }
                }
            }
            return valueMap;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，批量获取节点失败[path={0}]", e, path);
        }
        return null;
    }

    public void start() {
        RemoteConfig config = RemoteEnvironment.getConfig();
        if (config == null) {
            config = RemoteEnvironment.load();
        }
        if (StringUtils.isBlank(config.getUrls())) {
            throw new RuntimeException("配置信息不合法，远程配置拉取失败...");
        }
        BaseManager.setConfig(config);
        BaseManager.start();
        // 创建项目基础节点,不用监听
        LOGGER.logMessage(LogLevel.INFO, "初始化ZK根节点");
        ZKManager.set("", new ConfigValue(IRemoteConfigZKConstant.REMOTE_ROOT_PATH + 
                IRemoteConfigZKConstant.REMOTE_ENVIRONMENT_BASE_DIR), null);
        Environment env = new Environment();
        env.setEnvironment(IRemoteConfigZKConstant.REMOTE_ROOT_PATH + IRemoteConfigZKConstant.REMOTE_ENVIRONMENT_BASE_DIR);
        ZKDefaultEnvManager.set("", env);
        LOGGER.logMessage(LogLevel.INFO, "根节点创建完毕");
        LOGGER.logMessage(LogLevel.INFO, "客户端远程配置初始化完成");
    }

    public void stop() {
        LOGGER.logMessage(LogLevel.DEBUG, "--------------------------------------");
        LOGGER.logMessage(LogLevel.DEBUG, "远程配置，停止服务...");
        LOGGER.logMessage(LogLevel.DEBUG, "远程配置，停止服务...");
        LOGGER.logMessage(LogLevel.DEBUG, "远程配置，停止服务...");
        LOGGER.logMessage(LogLevel.DEBUG, "--------------------------------------");
        ZKManager.stop();
    }

    public void delete(String key, ConfigPath configPath) {
        String path = PathHelper.createPath(configPath);
        LOGGER.logMessage(LogLevel.INFO, "远程配置，删除节点[path={0}]", path);
        try {
            ZKManager.delete(key, configPath);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，删除节点失败[path={0}]", e, path);
        }
    }

    public void set(String key, ConfigValue value, ConfigPath configPath) {
        String path = PathHelper.createPath(configPath);
        LOGGER.logMessage(LogLevel.INFO, "远程配置，节点设值[{0}={1} ,path={2}]", key, value, path);
        try {
            ZKManager.set(key, value, configPath);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，节点设值成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，节点设值失败[{0}={1} ,path={2}]", e, key, value, path);
        }
    }

}
