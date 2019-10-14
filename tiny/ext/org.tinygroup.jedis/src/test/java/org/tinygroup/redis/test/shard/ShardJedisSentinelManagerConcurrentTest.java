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
package org.tinygroup.redis.test.shard;

import junit.framework.Assert;
import org.tinygroup.jedis.ShardJedisSentinelManager;
import org.tinygroup.jedis.impl.ShardJedisSentinelManagerFactory;
import org.tinygroup.jedis.shard.TinyShardJedis;
import org.tinygroup.tinyrunner.Runner;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;

public class ShardJedisSentinelManagerConcurrentTest {
    public static void main(String[] args) {
        Runner.init("application.xml", new ArrayList<String>());
//		JedisPoolConfig jedisConfig = BeanContainerFactory.getBeanContainer(
//				RedisTest.class.getClassLoader()).getBean("jedisConfig");
//		ShardJedisSentinelManager manager = BeanContainerFactory
//				.getBeanContainer(RedisTest.class.getClassLoader()).getBean(
//						"shardJedisSentinelManager");
//		manager.init(jedisConfig);
        ShardJedisSentinelManager manager = ShardJedisSentinelManagerFactory.getManager();
        for (int i = 0; i < 55; i++) {
            testReadAndWrite(manager, "WriteAndRead" + i + i + i);
        }
        System.out.println("||||||||||||||||||||||||||||||||||||||||||、");
        for (int i = 0; i < 11; i++) {
            try {
                Thread t = new Thread(new ThreadNew(manager, 100));
                t.start();
            } catch (Exception e) {
                System.out.println("=========================================================");
            }

        }
        try {
            Thread.sleep(15 * 1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        manager.destroy();
        System.out.println("======FINAL END=============");
    }

    private static void testReadAndWrite(ShardJedisSentinelManager manager,
                                         String key) {
        System.out.println("===================");
        TinyShardJedis shardedJedis = manager.getShardedJedis();
        // 只有这个种情况，是对读服务器进行服务器进行操作
        Jedis read = shardedJedis.getReadShard(key);
        // shardedJedis.getShard获得的链接操作数据和直接通过TinyShardJedis是一样
        Jedis write = shardedJedis.getShard(key);
        Jedis read2 = shardedJedis.getReadShard(key);
        System.out.println(write);
        System.out.println(read);
        Assert.assertNotSame(write, read);
        Assert.assertSame(write, read2);
        manager.returnResource(shardedJedis);
        System.out.println("===================");
    }


}
