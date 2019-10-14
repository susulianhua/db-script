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

import java.util.ArrayList;

public class ShardedFailOverTest {
    public static void main(String[] args) {
        Runner.init("application.xml", new ArrayList<String>());
        ShardJedisSentinelManager manager = ShardJedisSentinelManagerFactory.getManager();
        for (int i = 0; i < 1000; i++) {
            try {
                write(manager);
                System.out.println("Ok:" + System.currentTimeMillis());
            } catch (Exception e) {
                System.out.println("Exception:" + System.currentTimeMillis());

            }
        }

    }

    //测试主备切换影响
    //启动后关闭其所访问的主备服务器中的主服务器
    //看是否会对写产生影响
    //aaa分片是分布到了4xxxx端口系列的服务器上
    public static void write(ShardJedisSentinelManager manager) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TinyShardJedis jedis2 = manager.getShardedJedis();

        jedis2.set("aaa", "aaa");
        manager.returnResource(jedis2);
    }
}
