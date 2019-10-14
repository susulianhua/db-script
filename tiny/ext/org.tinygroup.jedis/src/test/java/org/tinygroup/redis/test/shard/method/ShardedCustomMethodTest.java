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

import junit.framework.Assert;
import org.tinygroup.jedis.ShardJedisSentinelManager;
import org.tinygroup.jedis.impl.ShardJedisSentinelManagerFactory;
import org.tinygroup.jedis.shard.TinyShardJedis;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;
import java.util.Set;

public class ShardedCustomMethodTest {
    public static void main(String[] args) {
        Runner.init("application.xml", new ArrayList<String>());
        ShardJedisSentinelManager manager = ShardJedisSentinelManagerFactory.getManager();
        TinyShardJedis jedis = manager.getShardedJedis();
        //==============================
        jedis.flushAll();
        //==============================
        for (int i = 0; i < 20; i++) {
            jedis.set("aaa" + i, "aaa" + i);
        }
        jedis.set("b1", "b1");
        manager.returnResource(jedis);
        jedis = manager.getShardedJedis();
        Assert.assertEquals(jedis.get("b1"), "b1");
        Set<String> value = jedis.keys("aaa?");
        Set<String> value2 = jedis.keys("aaa*");
        Assert.assertEquals(10, value.size());
        Assert.assertEquals(20, value2.size());
        //==============================
        int num = jedis.deleteMatchKey("aaa?");
        int num1 = jedis.deleteMatchKey("aaa*");
        Assert.assertEquals(10, num);
        Assert.assertEquals(10, num1);
        //==============================
        int num2 = jedis.deleteMatchKey("b?");
        Assert.assertEquals(1, num2);
        jedis.flushAll();
        num2 = jedis.deleteMatchKey("b?");
        Assert.assertEquals(0, num2);
        //==============================
        manager.destroy();
    }
}
