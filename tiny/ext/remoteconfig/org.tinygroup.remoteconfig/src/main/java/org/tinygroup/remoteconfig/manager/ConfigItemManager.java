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

import org.tinygroup.remoteconfig.config.ConfigPath;
import org.tinygroup.remoteconfig.config.ConfigValue;

import java.util.Map;

/**
 * 此方法中，所有的configPath指向的module都必须是叶子module
 *
 * @author chenjiao
 */
public interface ConfigItemManager {

    /**
     * 写入配置项
     * 该接口，包含写入和修改操作
     *
     * @param key        变量名
     * @param value      变量值
     * @param configPath 配置项写入位置，此参数根据自身属性，指向唯一一份远程配置
     */
    void set(String key, ConfigValue value, ConfigPath configPath);

    /**
     * 删除配置项
     *
     * @param key        变量名
     * @param configPath 配置项位置
     */
    void delete(String key, ConfigPath configPath);

    /**
     * 获取配置项
     *
     * @param key        变量名
     * @param configPath 配置项位置
     * @return 变量值
     */
    ConfigValue get(String key, ConfigPath configPath);

    /**
     * 验证配置项是否存在
     *
     * @param key        变量名
     * @param configPath 配置项位置
     * @return true:存在;false:不存在
     */
    boolean exists(String key, ConfigPath configPath);

    /**
     * 获取指定环境配置项合集
     *
     * @param 需要获取的配置项位置
     * @return key:配置项变量名;value:变量值
     */
    Map<String, ConfigValue> getAll(ConfigPath configPath);

}
