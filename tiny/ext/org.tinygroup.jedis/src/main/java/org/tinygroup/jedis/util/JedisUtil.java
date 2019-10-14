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
package org.tinygroup.jedis.util;

import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.jedis.config.JedisConfig;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.List;

public class JedisUtil {
    // 若有服务器fail，定时轮询间隔时间
    private static int FAILOVERTIME = 10000;

    private JedisUtil() {
    }

    public static int getFailOverTime() {
        return FAILOVERTIME;
    }

    public static void setFailOverTime(int failOverTime) {
        JedisUtil.FAILOVERTIME = failOverTime;
    }

    public static JedisPool createJedisPool(JedisConfig jedisConfig,
                                            ClassLoader loader) {
        // 设置默认参数
        String host = StringUtil.isBlank(jedisConfig.getHost()) ? Protocol.DEFAULT_HOST
                : jedisConfig.getHost();
        int port = jedisConfig.getPort() <= 0 ? Protocol.DEFAULT_PORT
                : jedisConfig.getPort();
        int timeout = jedisConfig.getTimeout() < 0 ? Protocol.DEFAULT_TIMEOUT
                : jedisConfig.getTimeout();
        int database = jedisConfig.getDatabase() < 0 ? Protocol.DEFAULT_DATABASE
                : jedisConfig.getDatabase();
        // 实例化jedis连接池
        JedisPool jedisPool = new JedisPool(getJedisPoolConfig(
                jedisConfig.getPoolConfig(), loader), host, port, timeout,
                jedisConfig.getPassword(), database,
                jedisConfig.getClientName());

        return jedisPool;
    }

    public static JedisPoolConfig getJedisPoolConfig(String poolConfig,
                                                     ClassLoader loader) {
        if (StringUtil.isBlank(poolConfig)) {
            return new JedisPoolConfig();
        }
        BeanContainer<?> container = BeanContainerFactory
                .getBeanContainer(loader);
        JedisPoolConfig jedisPoolConfig = container
                .getBean(poolConfig);
        return jedisPoolConfig;
    }

    public static JedisShardInfo createJedisShardInfo(JedisConfig jedisConfig) {
        JedisShardInfo info = new JedisShardInfo(jedisConfig.getHost(),
                jedisConfig.getPort(), jedisConfig.getTimeout());
        info.setPassword(jedisConfig.getPassword());
        return info;
    }

    public static String toSimpleString(String host, int port) {
        return host + ":" + port;
    }

    // public static Jedis choose(List<Jedis> currentlist,List<Jedis> skipList)
    // {
    // List<Jedis> list = newList(currentlist, skipList);
    // if (list.size() == 0) {
    // return null;
    // }
    // int totalWeight = list.size();
    // int random = (int) (Math.random() * totalWeight);
    // Jedis j = list.get(random);
    // try {
    // j.connect();
    // return j;
    // } catch (JedisConnectionException ex) {
    // skipList.add(j);
    // List<Jedis> newList = newList(list, j);
    // return chooseWithRemove(newList,skipList);
    // }
    // }

    public static Jedis choose(List<Jedis> list, List<Jedis> skipList) {
        if (list.size() == 0) {
            return null;
        }
        int totalWeight = list.size();
        int random = (int) (Math.random() * totalWeight);
        Jedis j = list.get(random);
        try {
            j.ping();
            if (j.ping().equals("PONG")) {
                return j;
            }
            return chooseAgain(list, skipList, j);
        } catch (JedisConnectionException ex) {
            return chooseAgain(list, skipList, j);
        }
    }

    private static Jedis chooseAgain(List<Jedis> list, List<Jedis> skipList,
                                     Jedis j) {
        skipList.add(j);
        list.remove(j);
        return choose(list, skipList);
    }

    // public static Jedis choose(List<Jedis> list) {
    // if (list.size() == 0) {
    // return null;
    // }
    // int totalWeight = list.size();
    // int random = (int) (Math.random() * totalWeight);
    // Jedis j = list.get(random);
    // try {
    // j.connect();
    // return j;
    // } catch (JedisConnectionException ex) {
    // List<Jedis> newList = newList(list, j);
    // return choose(newList);
    // }
    // }
    //
    // private static List<Jedis> newList(List<Jedis> jedises, Jedis jedis) {
    // List<Jedis> list = new ArrayList<Jedis>();
    // for (Jedis j : jedises) {
    // if (j == jedis) {
    // continue;
    // }
    // list.add(j);
    // }
    // return list;
    // }

    public static List<Jedis> newList(List<Jedis> jedises, List<Jedis> skipJedis) {
        List<Jedis> list = new ArrayList<Jedis>();
        for (Jedis j : jedises) {
            if (skipJedis.contains(j)) {
                continue;
            }
            list.add(j);
        }
        return list;
    }

    public static List<Jedis> copy(List<Jedis> jedises) {
        List<Jedis> list = new ArrayList<Jedis>();
        for (Jedis j : jedises) {
            list.add(j);
        }
        return list;
    }
}
