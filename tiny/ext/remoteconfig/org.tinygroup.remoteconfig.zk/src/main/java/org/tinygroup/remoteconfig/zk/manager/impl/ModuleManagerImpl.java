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

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.Module;
import org.tinygroup.remoteconfig.manager.ModuleManager;
import org.tinygroup.remoteconfig.zk.client.ZKManager;
import org.tinygroup.remoteconfig.zk.client.ZKModuleManager;
import org.tinygroup.remoteconfig.zk.utils.PathHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModuleManagerImpl implements ModuleManager {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ModuleManagerImpl.class);

    public Module add(Module module, ConfigPath entity) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，模块设值[{0}] ,路径[{1}]", module.getName(), entity);
        try {
            ZKModuleManager.set(module.getName(), module, entity);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，模块设值成功");
            return module;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，模块设值失败[{0}],路径[{1}]", e, module.getName(), entity);
        }
        return null;
    }

    public void update(Module module, ConfigPath entity) {
        LOGGER.logMessage(LogLevel.INFO, "远程配置，更新模块[{0}],路径[{1}]", module.getName(), entity);
        try {
            ZKModuleManager.set(module.getName(), module, entity);
            LOGGER.logMessage(LogLevel.INFO, "远程配置，更新模块成功");
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，更新模块失败[{0}],路径[{1}]", e, module.getName(), entity);
        }
    }

    public void delete(ConfigPath entity) {
        String path = PathHelper.createPath(entity);
        LOGGER.logMessage(LogLevel.INFO, "远程配置，删除模块[{0}]", path);
        try {
            ZKModuleManager.delete("", entity);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，删除模块[{0}]", e, path);
        }
    }

    private ConfigPath copyConfigPath(ConfigPath src) {
        ConfigPath cy = new ConfigPath();
        cy.setProductName(src.getProductName());
        cy.setVersionName(src.getVersionName());
        cy.setEnvironmentName(src.getEnvironmentName());
        cy.setModulePath(src.getModulePath());
        return cy;
    }

    public Module get(ConfigPath entity) {
        String path = PathHelper.createPath(entity);
        LOGGER.logMessage(LogLevel.INFO, "远程配置，获取模块[{0}]", path);
        try {
            ConfigPath tempPath = copyConfigPath(entity);
            Module module = ZKModuleManager.get("", tempPath);
            module.setConfigItemMap(ZKManager.getAll(tempPath));
            if (module != null) {
                module.setSubModules(querySubModules(tempPath));
            }
            LOGGER.logMessage(LogLevel.INFO, "远程配置，获取模块成功");
            return module;
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，获取模块失败[{0}]", e, path);
        }
        return null;
    }

    public List<Module> querySubModules(ConfigPath entity) {
        String path = PathHelper.createPath(entity);
        LOGGER.logMessage(LogLevel.INFO, "远程配置，获取子模块[{0}]", path);
        List<Module> modules = new ArrayList<Module>();
        try {
            Map<String, Module> moduleMap = ZKModuleManager.getAll(entity);
            if (moduleMap == null) {
                return modules;
            }
            for (String moduleKey : moduleMap.keySet()) {
                ConfigPath tempPath = copyConfigPath(entity);
                tempPath.setModulePath(PathHelper.getConfigPath(entity.getModulePath(), moduleKey));
                Module parentModule = get(tempPath);
                if (parentModule == null) {
                    continue;
                }
                modules.add(parentModule);
                getSubModule(parentModule, tempPath);
            }
            LOGGER.logMessage(LogLevel.INFO, "远程配置，获取子模块成功[{0}]", path);
        } catch (Exception e) {
            LOGGER.logMessage(LogLevel.ERROR, "远程配置，获取子模块失败[{0}]", e, path);
        }
        return modules;
    }

    private void getSubModule(Module parentModule, ConfigPath entity) {
        if (parentModule == null) {
            return;
        }
        List<Module> modules = new ArrayList<Module>();
        parentModule.setSubModules(modules);
        Map<String, Module> sunModuleMap = null;
        try {
            sunModuleMap = ZKModuleManager.getAll(entity);
        } catch (Exception e) {
        }
        if (sunModuleMap == null) {
            return;
        }
        for (String subModuleKey : sunModuleMap.keySet()) {
            Module module = sunModuleMap.get(subModuleKey);
            ConfigPath tempConfigPath = copyConfigPath(entity);
            tempConfigPath.setModulePath(PathHelper.getConfigPath(entity.getModulePath(), subModuleKey));
            module.setConfigItemMap(ZKManager.getAll(tempConfigPath));
            modules.add(module);
            tempConfigPath = copyConfigPath(entity);
            tempConfigPath.setModulePath(PathHelper.getConfigPath(entity.getModulePath(), subModuleKey));
            getSubModule(module, tempConfigPath);
        }
    }

}
