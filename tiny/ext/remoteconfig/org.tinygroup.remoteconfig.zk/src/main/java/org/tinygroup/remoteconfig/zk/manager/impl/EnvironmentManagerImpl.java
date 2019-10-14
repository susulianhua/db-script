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
package org.tinygroup.remoteconfig.zk.manager.impl;

import org.apache.commons.lang.StringUtils;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.Environment;
import org.tinygroup.remoteconfig.manager.EnvironmentManager;
import org.tinygroup.remoteconfig.manager.ModuleManager;
import org.tinygroup.remoteconfig.zk.client.ZKDefaultEnvManager;
import org.tinygroup.remoteconfig.zk.client.ZKEnvManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author yanwj06282
 */
public class EnvironmentManagerImpl implements EnvironmentManager {

    protected static final Logger LOGGER = LoggerFactory
            .getLogger(EnvironmentManagerImpl.class);

    ModuleManager moduleManager;

    public ModuleManager getModuleManager() {
        if (moduleManager == null) {
            moduleManager = new ModuleManagerImpl();
        }
        return moduleManager;
    }

    public void setModuleManager(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public Environment add(Environment env, String versionId, String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，增加环境[{0} ,版本={1} ,项目={2}]", env.getName(), versionId, productId);
        ConfigPath configPath = new ConfigPath();
        configPath.setProductName(productId);
        configPath.setVersionName(versionId);
        try {
            ZKEnvManager.set(env.getName(), env, configPath);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，增加环境成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，增加环境失败[{0} ,版本={1} ,项目={2}]", e, env.getName(), versionId, productId);
        }
        return env;
    }

    public void update(Environment env, String versionId, String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，更新环境[{0} ,版本={1} ,项目={2}]", env.getName(), versionId, productId);
        ConfigPath configPath = new ConfigPath();
        configPath.setProductName(productId);
        configPath.setVersionName(versionId);
        try {
            ZKEnvManager.set(env.getName(), env, configPath);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，更新环境成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，更新环境失败[{0} ,版本={1} ,项目={2}]", e, env.getName(), versionId, productId);
        }
    }

    public void delete(String envId, String versionId, String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，删除环境[{0} ,版本={1} ,项目={2}]", envId, versionId, productId);
        ConfigPath configPath = new ConfigPath();
        configPath.setProductName(productId);
        configPath.setVersionName(versionId);
        try {
            ZKEnvManager.delete(envId, configPath);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，删除环境失败[{0} ,版本={1} ,项目={2}]", e, envId, versionId, productId);
        }

    }

    public Environment get(String envId, String versionId, String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，获取环境[{0} ,版本={1} ,项目={2}]", envId, versionId, productId);
        ConfigPath configPath = new ConfigPath();
        configPath.setProductName(productId);
        configPath.setVersionName(versionId);
        try {
            Environment environment = ZKEnvManager.get(envId, configPath);
            if (environment == null) {
                return null;
            }
            configPath.setEnvironmentName(envId);
            environment.setModules(getModuleManager().querySubModules(configPath));
            LOGGER.logMessage(LogLevel.INFO, "远程配置，获取环境成功");
            return environment;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，获取环境失败[{0} ,版本={1} ,项目={2}]", e, envId, versionId, productId);
        }
        return null;
    }

    public List<Environment> query(String versionId, String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，批量获取环境[版本={0} ,项目={1}]", versionId, productId);
        if (StringUtils.isBlank(versionId) || StringUtils.isBlank(productId)) {
            LOGGER.logMessage(LogLevel.INFO, "远程配置，批量获取默认环境");
            try {
                List<Environment> envList = new ArrayList<Environment>(ZKDefaultEnvManager.getAll().values());
                if (LOGGER.isEnabled(LogLevel.INFO)) {
                    for (Environment env : envList) {
                        LOGGER.logMessage(LogLevel.INFO, "环境[{0}]", env.getName());
                    }
                }
                return envList;
            } catch (Exception e) {
                LOGGER.logMessage(LogLevel.ERROR, "远程配置，批量获取默认环境失败", e);
            }
        }
        ConfigPath configPath = new ConfigPath();
        configPath.setProductName(productId);
        configPath.setVersionName(versionId);
        List<Environment> envs = new ArrayList<Environment>();
        try {
            Map<String, Environment> sunModuleMap = ZKEnvManager.getAll(configPath);
            for (Iterator<String> iterator = sunModuleMap.keySet().iterator(); iterator.hasNext(); ) {
                String envId = iterator.next();
                Environment environment = get(envId, versionId, productId);
                LOGGER.logMessage(LogLevel.INFO, "环境[{0}]", environment.getName());
                if (environment != null) {
                    envs.add(environment);
                }
            }
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，批量获取环境失败[版本={0} ,项目={1}]", e, versionId, productId);
        }
        return envs;
    }

    public Environment add(Environment env) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，增加默认环境[{0}]", env.getName());
        try {
            ZKDefaultEnvManager.set(env.getName(), env);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，增加默认环境成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，增加默认环境失败[{0}]", e, env.getName());
            return null;
        }
        return env;
    }

    public void update(Environment env) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，更新默认环境[{0}]", env.getName());
        try {
            ZKDefaultEnvManager.set(env.getName(), env);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，更新默认环境成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，更新默认环境失败[{0}]", e, env.getName());
        }
    }

    public void delete(String envId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，删除默认环境[{0}]", envId);
        try {
            ZKDefaultEnvManager.delete(envId);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，删除默认环境成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，删除默认环境[{0}]", e, envId);
        }
    }

    public Environment get(String envId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，获取默认环境[{0}]", envId);
        Environment environment = null;
        try {
            environment = ZKDefaultEnvManager.get(envId);
            LOGGER.logMessage(LogLevel.INFO, "获取默认环境成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，获取默认环境失败[{0}]", e, envId);
        }
        return environment;
    }

}
