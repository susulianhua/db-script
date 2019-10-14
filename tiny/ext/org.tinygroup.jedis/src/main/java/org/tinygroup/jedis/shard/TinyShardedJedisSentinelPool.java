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
package org.tinygroup.jedis.shard;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.jedis.config.JedisConfig;
import org.tinygroup.jedis.config.ShardSentinelConfig;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Hashing;
import redis.clients.util.Pool;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class TinyShardedJedisSentinelPool extends Pool<TinyShardJedis> {

    public static final int MAX_RETRY_SENTINEL = 10;

    protected final Logger LOGGER = LoggerFactory.getLogger(TinyShardedJedisSentinelPool.class);

    protected GenericObjectPoolConfig poolConfig;
    protected Set<MasterListener> masterListeners = new HashSet<MasterListener>();
    Map<String, ShardSentinelConfig> totalMasterConfig = new HashMap<String, ShardSentinelConfig>();
    private int sentinelRetry = 0;
    // <hostAndPort.toString,masterName>
    private Map<String, String> masterInfoMap = new HashMap<String, String>();


    private volatile List<HostAndPort> currentHostMasters;

    public TinyShardedJedisSentinelPool(
            Map<String, Map<String, ShardSentinelConfig>> map,
            final GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        if (map.size() == 0) {
            throw new RuntimeException("redis配置为空");
        }
        List<HostAndPort> masterList = new ArrayList<HostAndPort>();
        for (String sentinels : map.keySet()) {
            Map<String, ShardSentinelConfig> shardSentinelConfigConfig = map
                    .get(sentinels);
            totalMasterConfig.putAll(shardSentinelConfigConfig);
            masterList.addAll(initSentinels(sentinels,
                    shardSentinelConfigConfig));
        }
        initPool(masterList);
        JedisCheck.start();
    }

    private List<HostAndPort> initSentinels(String sentinels,
                                            Map<String, ShardSentinelConfig> map) {

        String[] sentinelArray = sentinels.split(",");
        Map<String, String> sentinelMap = new HashMap<String, String>();
        for (String sentinel : sentinelArray) {
            sentinelMap.put(sentinel, sentinel);
        }
        List<String> masters = new ArrayList<String>();
        for (ShardSentinelConfig shardSentinelConfig : map.values()) {
            String masterName = shardSentinelConfig.getMasterName();
            masters.add(masterName);
        }
        return initSentinels(sentinelMap.keySet(), masters, map);
    }

    public void destroy() {
        JedisCheck.stop();
        for (MasterListener m : masterListeners) {
            m.shutdown();
        }

        super.destroy();
    }

    public List<HostAndPort> getCurrentHostMaster() {
        return currentHostMasters;
    }

    private void initPool(List<HostAndPort> masters) {
        if (!equals(currentHostMasters, masters)) {
            StringBuffer sb = new StringBuffer();
            for (HostAndPort master : masters) {
                sb.append(master.toString());
                sb.append(" ");
            }
            LOGGER.logMessage(LogLevel.INFO, "Created ShardedJedisPool to master at [" + sb.toString()
                    + "]");
            List<JedisShardInfo> shardMasters = makeShardInfoList(masters);
            initPool(poolConfig, new ShardedJedisFactory(shardMasters,
                    Hashing.MURMUR_HASH, null));
            currentHostMasters = masters;
        }
    }

    private boolean equals(List<HostAndPort> currentShardMasters,
                           List<HostAndPort> shardMasters) {
        if (currentShardMasters != null && shardMasters != null) {
            if (currentShardMasters.size() == shardMasters.size()) {
                for (int i = 0; i < currentShardMasters.size(); i++) {
                    if (!currentShardMasters.get(i).equals(shardMasters.get(i)))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    private List<JedisShardInfo> makeShardInfoList(List<HostAndPort> masters) {
        List<JedisShardInfo> shardMasters = new ArrayList<JedisShardInfo>();

        for (HostAndPort master : masters) {
            String masterName = masterInfoMap.get(master.toString());
            if (StringUtil.isBlank(masterName)) {
                throw new RuntimeException("没有找到" + master.toString()
                        + "对应的主备masterName");
            }
            ShardSentinelConfig shardSentinelConfig = totalMasterConfig
                    .get(masterName);
            //读出主服务器对应的配置项
            JedisConfig jedisConfig = getJedisConfig(shardSentinelConfig, master.getHost(), master.getPort());
            //根据主服务器的配置项和主从所有服务器列表创建
            TinyJedisShardInfo jedisShardInfo = new TinyJedisShardInfo(
                    jedisConfig.getHost(), jedisConfig.getPort(),
                    jedisConfig.getTimeout(),
                    shardSentinelConfig.getJedisConfigList());
            //设置主服务器密码
            jedisShardInfo.setPassword(jedisConfig.getPassword());
            shardMasters.add(jedisShardInfo);
        }
        return shardMasters;
    }

    private JedisConfig getJedisConfig(ShardSentinelConfig config, String ip, int port) {
        for (JedisConfig jedisConfig : config.getJedisConfigList()) {
            if (jedisConfig.getPort() == port && jedisConfig.getHost().equals(ip)) {
                return jedisConfig;
            }
        }
        throw new RuntimeException("找不到" + ip + ":" + port + "对应的JedisConfig配置信息");
    }

    private List<HostAndPort> initSentinels(Set<String> sentinels,
                                            final List<String> masters, Map<String, ShardSentinelConfig> map) {

        Map<String, HostAndPort> masterMap = new HashMap<String, HostAndPort>();
        List<HostAndPort> shardMasters = new ArrayList<HostAndPort>();

        LOGGER.logMessage(LogLevel.INFO, "Trying to find all master from available Sentinels...");

        for (String masterName : masters) {
            HostAndPort master = null;
            boolean fetched = false;

            while (!fetched && sentinelRetry < MAX_RETRY_SENTINEL) {
                for (String sentinel : sentinels) {
                    final HostAndPort hap = toHostAndPort(Arrays
                            .asList(sentinel.split(":")));

                    LOGGER.logMessage(LogLevel.INFO, "Connecting to Sentinel " + hap);

                    try {
                        //此处连接哨兵服务器
                        Jedis jedis = new Jedis(hap.getHost(), hap.getPort());
                        master = masterMap.get(masterName);
                        if (master == null) {
                            List<String> hostAndPort = jedis
                                    .sentinelGetMasterAddrByName(masterName);
                            if (hostAndPort != null && hostAndPort.size() > 0) {
                                master = toHostAndPort(hostAndPort);
                                LOGGER.logMessage(LogLevel.INFO, "Found Redis master at " + master);
                                shardMasters.add(master);
                                masterMap.put(masterName, master);
                                masterInfoMap
                                        .put(master.toString(), masterName);
                                fetched = true;
                                jedis.disconnect();
                                break;
                            }
                        }
                    } catch (JedisConnectionException e) {
                        LOGGER.logMessage(LogLevel.WARN, "Cannot connect to sentinel running @ "
                                + hap + ". Trying next one.");
                    }
                }

                if (null == master) {
                    try {
                        LOGGER.logMessage(LogLevel.WARN, "All sentinels down, cannot determine where is "
                                + masterName
                                + " master is running... sleeping 1000ms, Will try again.");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    fetched = false;
                    sentinelRetry++;
                }
            }

            // Try MAX_RETRY_SENTINEL times.
            if (!fetched && sentinelRetry >= MAX_RETRY_SENTINEL) {
                LOGGER.logMessage(LogLevel.WARN, "All sentinels down and try " + MAX_RETRY_SENTINEL
                        + " times, Abort.");
                throw new JedisConnectionException(
                        "Cannot connect all sentinels, Abort.");
            }
        }

        // All shards master must been accessed.
        if (masters.size() != 0 && masters.size() == shardMasters.size()) {

            LOGGER.logMessage(LogLevel.INFO, "Starting Sentinel listeners...");
            for (String sentinel : sentinels) {
                final HostAndPort hap = toHostAndPort(Arrays.asList(sentinel
                        .split(":")));
                MasterListener masterListener = new MasterListener(masters,
                        hap.getHost(), hap.getPort());
                masterListeners.add(masterListener);
                masterListener.start();
            }
        }

        return shardMasters;
    }

    private HostAndPort toHostAndPort(List<String> getMasterAddrByNameResult) {
        String host = getMasterAddrByNameResult.get(0);
        int port = Integer.parseInt(getMasterAddrByNameResult.get(1));

        return new HostAndPort(host, port);
    }

    /**
     * PoolableObjectFactory custom impl.
     */
    protected static class ShardedJedisFactory implements
            PooledObjectFactory<TinyShardJedis> {
        protected final Logger LOGGER2 = LoggerFactory.getLogger(ShardedJedisFactory.class);
        private List<JedisShardInfo> shards;
        private Hashing algo;
        private Pattern keyTagPattern;

        public ShardedJedisFactory(List<JedisShardInfo> shards, Hashing algo,
                                   Pattern keyTagPattern) {
            this.shards = shards;
            this.algo = algo;
            this.keyTagPattern = keyTagPattern;
        }

        public PooledObject<TinyShardJedis> makeObject() throws Exception {
            TinyShardJedis jedis = new TinyShardJedis(shards, algo,
                    keyTagPattern);
            return new DefaultPooledObject<TinyShardJedis>(jedis);
        }

        public void destroyObject(
                PooledObject<TinyShardJedis> pooledShardedJedis)
                throws Exception {
            final TinyShardJedis shardedJedis = pooledShardedJedis.getObject();
            for (Jedis jedis : shardedJedis.getAllShards()) {
                try {
                    try {
                        jedis.quit();
                    } catch (Exception e) {

                    }
                    jedis.disconnect();
                } catch (Exception e) {

                }
            }
            for (Jedis jedis : shardedJedis.getAllReadShards()) {
                try {
                    try {
                        jedis.quit();
                    } catch (Exception e) {

                    }
                    jedis.disconnect();
                } catch (Exception e) {

                }
            }
            shardedJedis.close();
        }

        public boolean validateObject(
                PooledObject<TinyShardJedis> pooledShardedJedis) {
            try {
                TinyShardJedis jedis = pooledShardedJedis.getObject();
                for (Jedis shard : jedis.getAllShards()) {
                    if (!shard.ping().equals("PONG")) {
                        return false;
                    }
                }
                return true;
            } catch (Exception ex) {
                LOGGER2.errorMessage("ShardedJedisFactory.validateObject发生异常", ex);
                return false;
            }
        }

        public void activateObject(PooledObject<TinyShardJedis> p)
                throws Exception {

        }

        public void passivateObject(PooledObject<TinyShardJedis> p)
                throws Exception {

        }
    }

    protected class JedisPubSubAdapter extends JedisPubSub {
        @Override
        public void onMessage(String channel, String message) {
        }

        @Override
        public void onPMessage(String pattern, String channel, String message) {
        }

        @Override
        public void onPSubscribe(String pattern, int subscribedChannels) {
        }

        @Override
        public void onPUnsubscribe(String pattern, int subscribedChannels) {
        }

        @Override
        public void onSubscribe(String channel, int subscribedChannels) {
        }

        @Override
        public void onUnsubscribe(String channel, int subscribedChannels) {
        }
    }

    protected class MasterListener extends Thread {

        protected List<String> masters;
        protected String host;
        protected int port;
        protected long subscribeRetryWaitTimeMillis = 5000;
        protected Jedis jedis;
        protected AtomicBoolean running = new AtomicBoolean(false);

        protected MasterListener() {
        }

        public MasterListener(List<String> masters, String host, int port) {
            this.masters = masters;
            this.host = host;
            this.port = port;
        }

        public MasterListener(List<String> masters, String host, int port,
                              long subscribeRetryWaitTimeMillis) {
            this(masters, host, port);
            this.subscribeRetryWaitTimeMillis = subscribeRetryWaitTimeMillis;
        }

        public void run() {

            running.set(true);

            while (running.get()) {

                jedis = new Jedis(host, port);

                try {
                    jedis.subscribe(new JedisPubSubAdapter() {
                        @Override
                        public void onMessage(String channel, String message) {
                            LOGGER.logMessage(LogLevel.INFO, "Sentinel " + host + ":" + port
                                    + " published: " + message + ".");

                            String[] switchMasterMsg = message.split(" ");

                            if (switchMasterMsg.length > 3) {

                                int index = masters.indexOf(switchMasterMsg[0]);
                                if (index >= 0) {
                                    HostAndPort newHostMaster = toHostAndPort(Arrays
                                            .asList(switchMasterMsg[3],
                                                    switchMasterMsg[4]));
                                    HostAndPort oldHostMaster = toHostAndPort(Arrays
                                            .asList(switchMasterMsg[1],
                                                    switchMasterMsg[2]));
                                    List<HostAndPort> newHostMasters = new ArrayList<HostAndPort>();
                                    for (int i = 0; i < currentHostMasters
                                            .size(); i++) {
                                        newHostMasters.add(null);
                                    }
                                    Collections.copy(newHostMasters,
                                            currentHostMasters);
                                    int oldIndex = 0;
                                    for (int i = 0; i < currentHostMasters
                                            .size(); i++) {
                                        HostAndPort hp = currentHostMasters
                                                .get(i);
                                        if (hp.toString().equals(
                                                oldHostMaster.toString())) {
                                            oldIndex = i;
                                            break;
                                        }
                                    }
                                    newHostMasters.set(oldIndex, newHostMaster);
                                    masterInfoMap.put(newHostMaster.toString(),
                                            switchMasterMsg[0]);

                                    initPool(newHostMasters);
                                } else {
                                    StringBuffer sb = new StringBuffer();
                                    for (String masterName : masters) {
                                        sb.append(masterName);
                                        sb.append(",");
                                    }
                                    LOGGER.logMessage(LogLevel.INFO, "Ignoring message on +switch-master for master name "
                                            + switchMasterMsg[0]
                                            + ", our monitor master name are ["
                                            + sb + "]");
                                }

                            } else {
                                LOGGER.logMessage(LogLevel.INFO, "Invalid message received on Sentinel "
                                        + host
                                        + ":"
                                        + port
                                        + " on channel +switch-master: "
                                        + message);
                            }
                        }
                    }, "+switch-master");

                } catch (JedisConnectionException e) {

                    if (running.get()) {
                        LOGGER.logMessage(LogLevel.WARN, "Lost connection to Sentinel at " + host
                                + ":" + port
                                + ". Sleeping 5000ms and retrying.");
                        try {
                            Thread.sleep(subscribeRetryWaitTimeMillis);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        LOGGER.logMessage(LogLevel.WARN, "Unsubscribing from Sentinel at " + host + ":"
                                + port);
                    }
                }
            }
        }

        public void shutdown() {
            try {
                LOGGER.logMessage(LogLevel.INFO, "Shutting down listener on " + host + ":" + port);
                running.set(false);
                // This isn't good, the Jedis object is not thread safe
                jedis.disconnect();
            } catch (Exception e) {
                LOGGER.logMessage(LogLevel.INFO, "Caught exception while shutting down: "
                        + e.getMessage());
            }
        }
    }

}
