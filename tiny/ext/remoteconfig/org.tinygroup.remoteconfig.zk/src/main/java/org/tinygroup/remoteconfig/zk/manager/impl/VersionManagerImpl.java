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

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.Version;
import org.tinygroup.remoteconfig.manager.EnvironmentManager;
import org.tinygroup.remoteconfig.manager.VersionManager;
import org.tinygroup.remoteconfig.zk.client.ZKVersionManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author yanwj06282
 */
public class VersionManagerImpl implements VersionManager {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(VersionManagerImpl.class);

    EnvironmentManager environmentManager;

    public EnvironmentManager getEnvironmentManager() {
        if (environmentManager == null) {
            environmentManager = new EnvironmentManagerImpl();
        }
        return environmentManager;
    }

    public void setEnvironmentManager(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    public Version add(Version version, String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，增加版本[项目={0} ,版本={1}]", productId, version.getVersion());
        ConfigPath configPath = new ConfigPath();
        configPath.setProductName(productId);
        try {
            ZKVersionManager.set(version.getName(), version, configPath);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，增加版本成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，增加版本失败[项目={0} ,版本={1}]", e, productId, version.getName());
            return null;
        }
        return version;
    }

    public void update(Version version, String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，更新版本[项目={0} ,版本={1}]", productId, version.getName());
        ConfigPath configPath = new ConfigPath();
        configPath.setProductName(productId);
        try {
            ZKVersionManager.set(version.getName(), version, configPath);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，更新版本成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，更新版本失败[项目={0} ,版本={1}]", e, productId, version.getName());
        }
    }

    public void delete(String versionId, String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，删除版本[项目={0} ,版本={1}]", productId, versionId);
        ConfigPath configPath = new ConfigPath();
        configPath.setProductName(productId);
        try {
            ZKVersionManager.delete(versionId, configPath);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，删除版本失败[项目={0} ,版本={1}]", e, productId, versionId);
        }
    }

    public Version get(String versionId, String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，获取版本[项目={0} ,版本={1}]", productId, versionId);
        ConfigPath configPath = new ConfigPath();
        configPath.setProductName(productId);
        try {
            Version version = ZKVersionManager.get(versionId, configPath);
            if (version == null) {
                return null;
            }
            version.setEnvironment(getEnvironmentManager().query(versionId, productId));

            LOGGER.logMessage(LogLevel.INFO, "远程配置，获取版本成功");
            return version;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，获取版本失败[项目={0} ,版本={1}]", e, productId, versionId);
        }
        return null;
    }

    public List<Version> query(String productId) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，批量获取版本[项目={0}]", productId);
        ConfigPath configPath = new ConfigPath();
        configPath.setProductName(productId);
        List<Version> versions = new ArrayList<Version>();
        try {
            Map<String, Version> versionMap = ZKVersionManager.getAll(configPath);
            for (Iterator<String> iterator = versionMap.keySet().iterator(); iterator.hasNext(); ) {
                String versionId = iterator.next();
                Version version = get(versionId, productId);
                if (version != null) {
                    LOGGER.logMessage(LogLevel.INFO, "版本[{0}]", version.getName());
                    versions.add(version);
                }
            }
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，批量获取版本失败[项目={0}]", e, productId);
        }
        return versions;
    }

}
