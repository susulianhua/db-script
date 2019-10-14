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

import org.tinygroup.jedis.util.JedisUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JedisCheck {
    private final static Logger LOGGER = LoggerFactory
            .getLogger(JedisCheck.class);
    private static Map<String, List<Jedis>> failReadMap = new HashMap<String, List<Jedis>>();
    private static FailOverThread failTestThread = new FailOverThread();
    private static boolean flag = false;

    public static synchronized void start() {
        if (flag) {
            return;
        }
        flag = true;
        failTestThread.setDaemon(true);
        failTestThread.start();
    }

    public static List<Jedis> getFailList(JedisShardInfo shardInfo) {
        List<Jedis> failList = new ArrayList<Jedis>();
        String infoString = getDesString(shardInfo);
        if (failReadMap.containsKey(infoString)) {
            failList = failReadMap.get(infoString);
        } else {
            failReadMap.put(infoString, failList);
        }
        return failList;
    }

    private static String getDesString(JedisShardInfo shardInfo) {
        return shardInfo.getName() + "_" + shardInfo.getHost() + "_" + shardInfo.getPort() + "_" + shardInfo.getDb();
    }

    private static void testFailJedisMap() {
        LOGGER.logMessage(LogLevel.DEBUG, "开始轮询连接失败的服务器列表");
        for (List<Jedis> list : failReadMap.values()) {
            List<Jedis> newList = JedisUtil.copy(list);
            for (Jedis j : newList) {
                LOGGER.logMessage(LogLevel.DEBUG, "开始尝试连接的服务器:{}:{}", j
                        .getClient().getHost(), j.getClient().getPort());
                boolean sucess = testFailJedis(j);
                if (sucess) {
                    LOGGER.logMessage(LogLevel.DEBUG, "连接成功,从fail列表中删除");
                    list.remove(j);
                }
                LOGGER.logMessage(LogLevel.DEBUG, "连接服务器:{}:{}结束", j
                        .getClient().getHost(), j.getClient().getPort());
            }
        }
        LOGGER.logMessage(LogLevel.DEBUG, "轮询连接失败的服务器列表结束");
    }

    private static boolean testFailJedis(Jedis j) {

        try {
            j.disconnect();
            j.connect();
            if (j.ping().equals("PONG")) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static void stop() {
        failTestThread.stopTry();
    }

    /**
     * 轮询失败的服务器
     */
    static class FailOverThread extends Thread {
        boolean stop = false;

        public void stopTry() {
            stop = true;
        }

        public void run() {
            while (!stop) {
                try {
                    sleep(JedisUtil.getFailOverTime());
                } catch (Exception e) {
                }
                testFailJedisMap();
            }
        }
    }
}
