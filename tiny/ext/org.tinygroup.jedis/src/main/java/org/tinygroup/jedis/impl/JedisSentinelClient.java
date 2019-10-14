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
package org.tinygroup.jedis.impl;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.jedis.config.JedisConfig;
import org.tinygroup.jedis.config.JedisSentinelConfig;
import org.tinygroup.jedis.util.JedisUtil;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.util.Pool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisSentinelClient {
    private static final Logger logger = LoggerFactory
            .getLogger(JedisSentinelClient2.class);
    Map<String, HostAndPort> jedisPoolsHostAndPortMap = new HashMap<String, HostAndPort>();
    // private List<JedisPool> jedisPools = new ArrayList<JedisPool>();
    // private List<String> poolKeys = new ArrayList<String>();
    private Map<String, JedisPool> jedisPoolsMap = new HashMap<String, JedisPool>();
    private JedisSentinelPool jedisSentinelPool;
    private String masterName;

    public JedisSentinelClient(JedisSentinelConfig config) {
        masterName = config.getMasterName();
        initJedisSentinelPool(config);
        initJedisPools(config);
    }

    private void initJedisPools(JedisSentinelConfig config) {
        List<JedisConfig> configs = config.getJedisConfigList();
        for (JedisConfig jedisConfig : configs) {
            if (!jedisPoolsMap.containsKey(jedisConfig.getId())) {
                JedisPool pool = JedisUtil.createJedisPool(jedisConfig, this
                        .getClass().getClassLoader());
                jedisPoolsMap.put(jedisConfig.getId(), pool);

                jedisPoolsHostAndPortMap.put(jedisConfig.getId(),
                        getHostAndPort(jedisConfig));
            }

        }
    }

    private HostAndPort getHostAndPort(JedisConfig config) {
        return new HostAndPort(config.getHost(), config.getPort());
    }

    private void initJedisSentinelPool(JedisSentinelConfig config) {
        String masterName = config.getMasterName();
        if (StringUtil.isBlank(masterName)) {
            throw new RuntimeException("JedisSentinelPool配置的masterName不可为空."
                    + config.toString());
        }
        String sentinels = config.getSentinels();
        if (StringUtil.isBlank(sentinels)) {
            throw new RuntimeException("JedisSentinelPool配置的sentinels不可为空."
                    + config.toString());
        }
        Set<String> sentinelSet = getSentinelSet(sentinels);
        int database = config.getDatabase();
        int timeout = config.getTimeout();
        String password = config.getPassword();
        String poolConfig = config.getPoolConfig();
        JedisPoolConfig jedisPoolConfig = JedisUtil.getJedisPoolConfig(
                poolConfig, this.getClass().getClassLoader());
        jedisSentinelPool = new JedisSentinelPool(masterName, sentinelSet,
                jedisPoolConfig, timeout, password, database);
    }

    private Set<String> getSentinelSet(String sentinels) {
        String[] sentinelArray = sentinels.split(",");
        Map<String, String> map = new HashMap<String, String>();
        for (String sentinel : sentinelArray) {
            map.put(sentinel, sentinel);
        }
        return map.keySet();
    }

    public void destroy() {
        destroy(jedisSentinelPool);
        for (JedisPool pool : jedisPoolsMap.values()) {
            destroy(pool);
        }
        jedisPoolsMap.clear();
    }

    private void destroy(Pool<Jedis> pool) {
        try {
            pool.destroy();
        } catch (Exception e) {
            logger.errorMessage("销毁{}的连接池{}时出错", e, masterName,
                    pool.toString());
        }

    }

    public JedisSentinelPool getJedisSentinelPool() {
        return jedisSentinelPool;
    }

    public Jedis getWriteJedis() {
        return getJedisSentinelPool().getResource();
    }

    public Jedis getReadJedis() {
        return getReadJedisPool().getResource();
    }

    public JedisPool getReadJedisPool() {
        // get from jedisPoolsMap.values() except
        // jedisSentinelPool.getCurrentHostMaster()
        return getReadJedisPool(jedisSentinelPool.getCurrentHostMaster());
    }

    private JedisPool getReadJedisPool(HostAndPort except) {
        String key = getJedisPool(jedisPoolsMap);
        if (except.toString().equals(jedisPoolsHostAndPortMap.get(key).toString())) {
            return getReadJedisPool(except);//这里要考虑读写共用一个的情况，即判断列表可用性是否只有1个
        }

        return jedisPoolsMap.get(key);
    }

    private String getJedisPool(Map<String, JedisPool> pools) {
        for (String s : pools.keySet()) {
            return s;
        }
        throw new RuntimeException("未找到合适的jedisPool");
    }

}
