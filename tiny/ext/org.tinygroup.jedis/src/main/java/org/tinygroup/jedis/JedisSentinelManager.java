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

import org.tinygroup.jedis.config.JedisSentinelConfig;
import org.tinygroup.jedis.config.JedisSentinelConfigs;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;

/**
 * JedisManager
 *
 * @author yancheng11334
 */
public interface JedisSentinelManager {

    /**
     * 添加JedisSentinel配置
     *
     * @param configs
     */
    void addJedisSentinelConfigs(JedisSentinelConfigs configs);

    /**
     * 添加JedisSentinel配置
     *
     * @param config
     */
    void addJedisSentinelConfig(JedisSentinelConfig config);

    /**
     * 移除JedisSentinel配置
     *
     * @param configs
     */
    void removeJedisSentinelConfigs(JedisSentinelConfigs configs);

    /**
     * 移除JedisSentinel配置,并移除、销毁相关的连接池
     *
     * @param config
     */
    void removeJedisSentinelConfig(JedisSentinelConfig config);


    /**
     * 移除JedisSentinel连接池
     *
     * @param masterName
     * @param destory    是否销毁，true为销毁
     * @return
     */
    JedisSentinelPool removeJedisSentinelPool(String masterName, boolean destory);

    /**
     * 获取JedisSentinel连接池，也是写连接池
     *
     * @param masterName masterName
     * @return
     */
    JedisSentinelPool getJedisSentinelPool(String masterName);

    /**
     * 获取jedis写连接
     *
     * @param masterName masterName
     * @return
     */
    Jedis getWriteJedis(String masterName);

    /**
     * 获取jedis读连接
     *
     * @param masterName masterName
     * @return
     */
    Jedis getReadJedis(String masterName);

    /**
     * 获取jedis读连接池
     *
     * @param masterName masterName
     * @return
     */
    JedisPool getReadJedisPool(String masterName);


}
