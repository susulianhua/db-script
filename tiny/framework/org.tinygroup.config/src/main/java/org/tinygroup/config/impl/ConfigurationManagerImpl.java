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
package org.tinygroup.config.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.tinygroup.commons.tools.ValueUtil;
import org.tinygroup.config.ConfigManagerFactory;
import org.tinygroup.config.Configuration;
import org.tinygroup.config.ConfigurationLoader;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.filter.PathFilter;
import org.tinygroup.xmlparser.node.XmlNode;
import org.tinygroup.xmlparser.parser.XmlStringParser;

/**
 * Created by luoguo on 2014/5/14.
 */
public class ConfigurationManagerImpl implements org.tinygroup.config.ConfigurationManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationManagerImpl.class);
    private ConfigurationLoader configurationLoader;
    private XmlNode applicationConfiguration;
    private Map<String, XmlNode> componentConfigurationMap = new HashMap<String, XmlNode>();
    private Collection<Configuration> configurationList;

    public void setConfigurationLoader(ConfigurationLoader configurationLoader) {
        this.configurationLoader = configurationLoader;
    }

    public void setComponentConfiguration(String key, XmlNode componentConfiguration) {
        componentConfigurationMap.put(key, componentConfiguration);
    }

    public XmlNode getApplicationConfiguration() {
        return applicationConfiguration;
    }

    public void setApplicationConfiguration(XmlNode applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public Map<String, XmlNode> getComponentConfigurationMap() {
        return componentConfigurationMap;
    }

    public void setComponentConfigurationMap(Map<String, XmlNode> componentConfigurationMap) {
        this.componentConfigurationMap = componentConfigurationMap;
    }

    public XmlNode getComponentConfiguration(String key) {
        return componentConfigurationMap.get(key);
    }

    public void distributeConfiguration() {
        if (configurationList != null) {
            LOGGER.logMessage(LogLevel.INFO, "正在分发应用配置信息...");
            PathFilter<XmlNode> pathFilter = new PathFilter<XmlNode>(applicationConfiguration);
            for (Configuration configurationItem : configurationList) {
                XmlNode componentConfig = componentConfigurationMap.get(configurationItem.getComponentConfigPath());
                XmlNode appConfig = null;
                if (configurationItem.getApplicationNodePath() != null) {
                    appConfig = pathFilter.findNode(configurationItem.getApplicationNodePath());
                }
                configurationItem.config(appConfig, componentConfig);
            }
            LOGGER.logMessage(LogLevel.INFO, "应用配置信息分发完毕");
        }

    }

    public void setConfigurationList(Collection<Configuration> configurationList) {
        this.configurationList = configurationList;
    }

    public void setConfiguration(String key, String value) {
    	ConfigManagerFactory.getManager().addConfig(key, value);
    }

    public <T> T getConfiguration(Class<T> type, String key, T defaultValue) {
        String value = ConfigManagerFactory.getManager().getConfig(key);
        if (value == null || "".equals(value)) {
            return defaultValue;
        }
        return (T) ValueUtil.getValue(value, type.getName());
    }

    public void loadConfiguration() {
        if (configurationLoader != null) {
            setApplicationConfiguration(configurationLoader.loadApplicationConfiguration());
            setComponentConfigurationMap(configurationLoader.loadComponentConfiguration());
        }
    }

    @Deprecated
    public Map<String, String> getConfiguration() {
        return ConfigManagerFactory.getManager().getConfigMap();
    }

    public String getConfiguration(String key) {
        return ConfigManagerFactory.getManager().getConfig(key);
    }

    public void replace() {
        String applicationInfo = getApplicationConfiguration().toString();
        String newInfo = ConfigurationUtil.replace(applicationInfo, getConfiguration());
        XmlNode newXml = new XmlStringParser().parse(newInfo).getRoot();
        setApplicationConfiguration(newXml);
    }

    public void clear() {
        if (configurationList != null) {
            configurationList.clear();
        }
        componentConfigurationMap.clear();
        ConfigManagerFactory.getManager().clear();
        applicationConfiguration = null;
    }
}
