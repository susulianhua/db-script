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
package org.tinygroup.cache.redis;

import org.tinygroup.jedis.config.JedisConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClient {

    private JedisConfig jedisConfig;

    private RedisCacheManager redisCacheManager;

    private String region;

    public JedisClient(String region, RedisCacheManager redisCacheManager) {
        this.region = region;
        this.redisCacheManager = redisCacheManager;
    }

    public synchronized JedisConfig getJedisConfig() {
        //为了解决aopcache初始化优先于框架加载配置的顺序问题，将真正的初始化放到调用时触发
        if (jedisConfig == null) {
            jedisConfig = redisCacheManager.getJedisManager()
                    .getJedisConfig(region);
        }
        return jedisConfig;
    }

    public RedisCacheManager getRedisCacheManager() {
        return redisCacheManager;
    }

    public void setRedisCacheManager(RedisCacheManager redisCacheManager) {
        this.redisCacheManager = redisCacheManager;
    }

    public JedisPoolConfig getJedisPoolConfig(JedisConfig jedisConfig) {
        return redisCacheManager.getJedisManager().getJedisPoolConfig(
                jedisConfig.getId());
    }

    public String getCharset() {
        return getJedisConfig().getCharset() == null ? "utf-8" : getJedisConfig()
                .getCharset();
    }

    public JedisPool getJedisPool() {
        return redisCacheManager.getJedisManager().getJedisPool(region);
    }
}
