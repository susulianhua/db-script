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
package org.tinygroup.config;

import org.tinygroup.xmlparser.node.XmlNode;

import java.util.Collection;
import java.util.Map;

/**
 * 应用配置管理器
 *
 * @author luoguo
 */
public interface ConfigurationManager {

    /**
     * 设置配置加载器
     *
     * @param configurationLoader 配置加载器
     */
    void setConfigurationLoader(ConfigurationLoader configurationLoader);

    /**
     * 设置单个组件配置
     *
     * @param key                    组件配置映射的key
     * @param componentConfiguration 组件配置
     */
    void setComponentConfiguration(String key, XmlNode componentConfiguration);

    /**
     * 返回管理器的应用配置
     *
     * @return 管理器的应用配置对象
     */
    XmlNode getApplicationConfiguration();

    /**
     * 给配置管理器设置应用配置信息
     *
     * @param applicationConfiguration 应用配置信息，指的是application.xml文件
     */
    void setApplicationConfiguration(XmlNode applicationConfiguration);

    /**
     * 以map数据结构返回配置管理器关联的所有组件配置信息
     *
     * @return
     */
    Map<String, XmlNode> getComponentConfigurationMap();

    /**
     * 设置组件配置信息
     *
     * @param componentConfigurationMap 所有组件配置组成的map
     */
    void setComponentConfigurationMap(Map<String, XmlNode> componentConfigurationMap);

    /**
     * 返回单个组件配置信息
     *
     * @param key 组件配置映射的key
     * @return 组件配置
     */
    XmlNode getComponentConfiguration(String key);

    /**
     * 分发应用配置<br>
     * 应用配置会促使配置管理器把配置信息推送到配置订阅者
     */
    void distributeConfiguration();

    /**
     * 重新加载应用配置信息，进行变量替换，然后返回变量替换后的配置信息作为新的应用配置信息
     */
    void replace();

    /**
     * 设置配置管理器内部管理的所有配置对象
     *
     * @param configurationList 配置对象组成的列表
     */
    void setConfigurationList(Collection<Configuration> configurationList);

    /**
     * 设置KeyValue形式的值
     *
     * @param key   配置名称
     * @param value 配置名称对应的值
     */
    void setConfiguration(String key, String value);

    /**
     * 返回配置管理器内部注册的所有配置信息，请不要使用返回的map进行put操作
     *
     * @return
     */
    Map<String, String> getConfiguration();

    /**
     * 返回key对应的配置信息
     *
     * @param key 配置名称
     * @return 配置信息
     */
    String getConfiguration(String key);

    /**
     * 返回指定类型的配置信息，内部会进行类型转换
     *
     * @param type         类型
     * @param key          配置名称
     * @param defaultValue 默认值
     * @param <T>
     * @return 返回type类型的配置信息
     */
    <T> T getConfiguration(Class<T> type, String key, T defaultValue);

    void clear();
}
