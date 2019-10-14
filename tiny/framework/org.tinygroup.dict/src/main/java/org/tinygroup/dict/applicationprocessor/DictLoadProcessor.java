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
package org.tinygroup.dict.applicationprocessor;

import org.tinygroup.application.Application;
import org.tinygroup.application.ApplicationProcessor;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cache.Cache;
import org.tinygroup.cache.CacheInitConfig;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.impl.AbstractConfiguration;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.dict.DictManager;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 功能说明:字典项加载启动插件
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-11-18 <br>
 * <br>
 */
public class DictLoadProcessor extends AbstractConfiguration implements
        ApplicationProcessor {

    //	private static final String DEFAULT_CACHE_NAME = "jcsCache";
    private static final String DICT_NODE_PATH = "/application/dict-load-config";
    private static final String BEAN_NAME = "bean_name";
    private static final String STOP_CACHE_CLEAR = "stop_cache_clear";
    private String cacheBeanName;
    private DictManager manager;
    private CacheInitConfig config;
    private boolean canClear;

    public CacheInitConfig getConfig() {
        return config;
    }

    public void setConfig(CacheInitConfig config) {
        this.config = config;
    }

    public DictManager getManager() {
        return manager;
    }

    public void setManager(DictManager manager) {
        this.manager = manager;
    }

    public boolean isCanClear() {
        return canClear;
    }

    public void setCanClear(boolean canClear) {
        this.canClear = canClear;
    }

    public String getApplicationNodePath() {
        return DICT_NODE_PATH;
    }

    public String getComponentConfigPath() {
        return null;
    }

    public void start() {
        Cache cache = manager.getCache();
        if (cache == null) {
            cache = BeanContainerFactory.getBeanContainer(
                    getClass().getClassLoader()).getBean(cacheBeanName);
        }
        cache.init(config.getRegion());
        manager.setCache(cache);
        manager.load();
    }

    public void init() {
        //do nothing
    }

    public void stop() {
        if (canClear) {
            LOGGER.logMessage(LogLevel.INFO, "开始清除字典缓存");
            manager.clear();
            LOGGER.logMessage(LogLevel.INFO, "清除字典缓存结束");
        }
    }

    public void setApplication(Application application) {
        //do nothing
    }

    public void config(XmlNode applicationConfig, XmlNode componentConfig) {
        super.config(applicationConfig, componentConfig);
        cacheBeanName = ConfigurationUtil.getPropertyName(applicationConfig,
                componentConfig, BEAN_NAME);
        if (StringUtil.isBlank(cacheBeanName)) {
            throw new RuntimeException("未配置必须的Cache Bean");
        }
        String clearMark = ConfigurationUtil.getPropertyName(applicationConfig,
                componentConfig, STOP_CACHE_CLEAR);
        if (!StringUtil.isBlank(clearMark)) {
            canClear = Boolean.parseBoolean(clearMark);
        }
    }

    public int getOrder() {
        return DEFAULT_PRECEDENCE;
    }
}
