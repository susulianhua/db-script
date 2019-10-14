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
package org.tinygroup.redis.test.shard.method;

import org.tinygroup.jedis.ShardJedisSentinelManager;
import org.tinygroup.jedis.impl.ShardJedisSentinelManagerFactory;
import org.tinygroup.jedis.shard.TinyShardJedis;
import org.tinygroup.tinyrunner.Runner;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;

public class TestSingleThreadTime {
    public static void main(String[] args) {
        Runner.init("application.xml", new ArrayList<String>());
        ShardJedisSentinelManager manager = ShardJedisSentinelManagerFactory
                .getManager();
        TinyShardJedis shardedJedis = manager.getShardedJedis();
        shardedJedis.flushAll();
        // 这里直接操作jedis
        Jedis write = shardedJedis.getShard("a");
        long pre = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            write.set("a" + i, "a" + i);
        }
        System.out.println("jedis set" + (System.currentTimeMillis() - pre));
        // =====================================
        pre = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            write.get("a" + i);
        }
        System.out.println("jedis get" + (System.currentTimeMillis() - pre));
        // =====================================
        manager.returnResource(shardedJedis);
        // =====================================
        shardedJedis = manager.getShardedJedis();
        shardedJedis.flushAll();
        // =====================================
        pre = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            shardedJedis.set("a" + i, "a" + i);
        }
        System.out.println("shardedJedis set"
                + (System.currentTimeMillis() - pre));
        manager.returnResource(shardedJedis);
        // =====================================
        shardedJedis = manager.getShardedJedis();
        pre = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            shardedJedis.get("a" + i);
        }
        System.out.println("shardedJedis get"
                + (System.currentTimeMillis() - pre));
        manager.returnResource(shardedJedis);
        // =====================================
        manager.destroy();
    }
}
