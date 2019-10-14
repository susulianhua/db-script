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

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.jedis.ShardJedisSentinelManager;
import org.tinygroup.jedis.config.ShardJedisSentinelConfig;
import org.tinygroup.jedis.config.ShardJedisSentinelConfigs;
import org.tinygroup.jedis.config.ShardSentinelConfig;
import org.tinygroup.jedis.config.ShardSentinelConfigs;
import org.tinygroup.jedis.shard.TinyShardJedis;
import org.tinygroup.jedis.shard.TinyShardedJedisSentinelPool;
import org.tinygroup.jedis.util.JedisUtil;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShardJedisSentinelManagerImpl implements ShardJedisSentinelManager {

    private Map<String, Map<String, ShardSentinelConfig>> map = new HashMap<String, Map<String, ShardSentinelConfig>>();
    private TinyShardedJedisSentinelPool pool;

    public ShardJedisSentinelManagerImpl(ShardJedisSentinelConfigs configs) {
        addJedisSentinelConfigs(configs);
        String pool = configs.getPool();
        JedisPoolConfig jedisConfig = BeanContainerFactory.getBeanContainer(
                ShardJedisSentinelManagerImpl.class.getClassLoader())
                .getBean(pool);
        init(jedisConfig);
    }

    private void addJedisSentinelConfigs(ShardJedisSentinelConfigs configs) {
        for (ShardJedisSentinelConfig config : configs
                .getJedisShardSentinelConfigsList()) {
            addJedisSentinelConfig(config);
        }

    }

    private void addJedisSentinelConfig(ShardJedisSentinelConfig config) {
        for (ShardSentinelConfigs shardSentinelConfigs : config
                .getShardSentinelConfigs()) {
            addShardSentinelConfigs(shardSentinelConfigs);
        }

    }

    private void addShardSentinelConfigs(ShardSentinelConfigs config) {
        String sentinels = config.getSentinels();
        List<ShardSentinelConfig> shardSentinelConfigLists = config
                .getShardSentinelConfigLists();
        if (shardSentinelConfigLists.size() == 0) {
            return;
        }
        Map<String, ShardSentinelConfig> sentinelMaster = map.get(sentinels);
        if (sentinelMaster == null) {
            sentinelMaster = new HashMap<String, ShardSentinelConfig>();
            map.put(sentinels, sentinelMaster);
        }
        for (ShardSentinelConfig shardSentinelConfig : config.getShardSentinelConfigLists()) {
            String masterName = shardSentinelConfig.getMasterName();
            if (!sentinelMaster.containsKey(sentinelMaster)) {
                sentinelMaster.put(masterName, shardSentinelConfig);
            }
        }
    }

    private void init(GenericObjectPoolConfig poolConfig) {
        if (poolConfig == null) {
            throw new RuntimeException("初始化连接池不可为空");
        }
        destroy();
        pool = new TinyShardedJedisSentinelPool(map, poolConfig);
    }

    public void destroy() {
        if (pool != null) {
            pool.destroy();
        }
    }


    public TinyShardJedis getShardedJedis() {
        return pool.getResource();
    }

    public void returnResourceObject(TinyShardJedis resource) {
        resource.resetWriteState();
        pool.returnResourceObject(resource);
    }

    public void returnResource(TinyShardJedis resource) {
        resource.resetWriteState();
        pool.returnResource(resource);
    }

    public void returnBrokenResource(TinyShardJedis resource) {
        resource.resetWriteState();
        pool.returnBrokenResource(resource);
    }

    public void setFailOverTime(int failOverTime) {
        JedisUtil.setFailOverTime(failOverTime);
    }

}
