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
package org.tinygroup.jedis;

import org.tinygroup.jedis.config.JedisConfig;
import org.tinygroup.jedis.config.JedisConfigs;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JedisManager
 *
 * @author yancheng11334
 */
public interface JedisManager {

    /**
     * 添加jedis配置
     *
     * @param configs
     */
    void addJedisConfigs(JedisConfigs configs);

    /**
     * 添加jedis配置
     *
     * @param config
     */
    void addJedisConfig(JedisConfig config);

    /**
     * 移除jedis配置
     *
     * @param configs
     */
    void removeJedisConfigs(JedisConfigs configs);

    /**
     * 移除jedis配置,并移除相关的连接池
     *
     * @param config
     */
    void removeJedisConfig(JedisConfig config);

    /**
     * 获取jedis配置
     *
     * @param jedisId jedis配置id
     * @return
     */
    JedisConfig getJedisConfig(String jedisId);

    /**
     * 获取jedis连接池配置
     *
     * @param jedisId jedis配置id
     * @return
     */
    JedisPoolConfig getJedisPoolConfig(String jedisId);

    /**
     * 获取jedis连接池配置
     *
     * @param jedisId jedis配置id
     * @return
     */
    JedisPool getJedisPool(String jedisId);

    /**
     * 移除jedis连接池
     *
     * @param jedisId jedis配置id
     * @return
     */
    JedisPool removeJedisPool(String jedisId);

    /**
     * 移除jedis连接池
     *
     * @param pool jedis连接池
     * @return
     */
    String removeJedisPool(JedisPool pool);

}
