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
package org.tinygroup.remoteconfig.manager;

import org.tinygroup.remoteconfig.config.ConfigValue;

import java.util.Map;

public interface ConfigItemReader {

    /**
     * 配置项获取
     *
     * @param key 配置中心，配置项变量名
     * @return 配置项变量值
     */
    ConfigValue get(String key);

    /**
     * 验证配置项是否存在
     *
     * @param key 配置中心，配置项变量名
     * @return true:存在;false:不存在
     */
    boolean exists(String key);

    /**
     * 获取指定环境配置项合集
     *
     * @return key:配置项变量名;value:变量值
     */
    Map<String, ConfigValue> getAll();
    
    /**
     * 重新获取远程配置信息
     * @param m 存放远程配置的map
     */
    public void retrieveRemoteConfig(Map<String, String> m);
}
