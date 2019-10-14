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

import com.thoughtworks.xstream.XStream;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.jedis.ShardJedisSentinelManager;
import org.tinygroup.jedis.config.ShardJedisSentinelConfigs;
import org.tinygroup.jedis.fileresolver.JedisShardSentinelConfigsFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.xstream.XStreamFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ShardJedisSentinelManagerFactory {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ShardJedisSentinelManagerFactory.class);
    private static final String CONFIG = "jedis.jedisshardsentrinelconfig.xml";
    private static final Map<String, ShardJedisSentinelManager> managerMap = new HashMap<String, ShardJedisSentinelManager>();

    static {
        XStream stream = XStreamFactory
                .getXStream(JedisShardSentinelConfigsFileProcessor.JEDIS_SHARD_SENTINEL_XSTREAM_NAME);
        ClassLoader loader = ShardJedisSentinelManagerFactory.class
                .getClassLoader() == null ? ClassLoader.getSystemClassLoader()
                : ShardJedisSentinelManagerFactory.class.getClassLoader();
        Enumeration<URL> urls = null;
        try {
            urls = loader.getResources(CONFIG);
        } catch (IOException e) {
            LOGGER.errorMessage(
                    "查找jedis集群配置:jedis.jedisshardsentrinelconfig.xml出现异常", e);
            throw new RuntimeException(e);
        }
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            LOGGER.logMessage(LogLevel.INFO, "找到Jedis集群配置文件：{0}",
                    url.toString());
            ShardJedisSentinelConfigs shardJedisSentinelConfigs = (ShardJedisSentinelConfigs) stream
                    .fromXML(url);
            add(shardJedisSentinelConfigs);

        }
    }

    private ShardJedisSentinelManagerFactory() {

    }

    private static void add(ShardJedisSentinelConfigs shardJedisSentinelConfigs) {
        String id = shardJedisSentinelConfigs.getId();
        String pool = shardJedisSentinelConfigs.getPool();
        if (managerMap.containsKey(id)) {
            LOGGER.logMessage(LogLevel.WARN, "Jedis集群配置文件冲突,集群id:{}", id);
        }
        if (StringUtil.isBlank(pool)) {
            throw new RuntimeException("Jedis集群:" + id + "配置的连接池为空");
        }
        ShardJedisSentinelManager manager = new ShardJedisSentinelManagerImpl(
                shardJedisSentinelConfigs);
        managerMap.put(id, manager);
    }

    public static ShardJedisSentinelManager getManager() {
        if (managerMap.containsKey("")) {
            return managerMap.get("");
        } else {
            throw new RuntimeException("不存在默认的ShardJedisSentinelManager");
        }
    }

    public static ShardJedisSentinelManager getManager(String key) {
        if (managerMap.containsKey(key)) {
            return managerMap.get(key);
        } else {
            throw new RuntimeException("不存在id：" + key
                    + "的ShardJedisSentinelManager");
        }
    }

    public static void addResource(String filePath) {
        XStream stream = XStreamFactory
                .getXStream(JedisShardSentinelConfigsFileProcessor.JEDIS_SHARD_SENTINEL_XSTREAM_NAME);
        ShardJedisSentinelConfigs shardJedisSentinelConfigs = (ShardJedisSentinelConfigs) stream
                .fromXML(ShardJedisSentinelManagerFactory.class
                        .getResourceAsStream(filePath));
        add(shardJedisSentinelConfigs);

    }

}
