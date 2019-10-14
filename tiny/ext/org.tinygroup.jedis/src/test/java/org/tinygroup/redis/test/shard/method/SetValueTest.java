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
import org.tinygroup.jedis.shard.TinyShardJedis;

public class SetValueTest extends Thread {

    private String id;
    private ShardJedisSentinelManager manager;

    public SetValueTest(String id, ShardJedisSentinelManager manager) {
        this.id = id;
        this.manager = manager;
    }

    @Override
    public void run() {
        System.out.println("[" + id + "]");
        TinyShardJedis jedis = getJedis();
        long preTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            jedis.set(id + i, id + "_" + i);
        }
        System.out.println("[" + id + "]耗时："
                + (System.currentTimeMillis() - preTime));
        returnJedis(jedis);
    }

    private TinyShardJedis getJedis() {
        //long preTime = System.currentTimeMillis();
        TinyShardJedis jedis = manager.getShardedJedis();
        // System.out.println("[" + id + "]获取链接耗时：" +
        // (System.currentTimeMillis() - preTime));
        return jedis;
    }

    private void returnJedis(TinyShardJedis jedis) {
        //long preTime = System.currentTimeMillis();
        manager.returnResource(jedis);
        // System.out.println("[" + id + "]返回链接耗时：" +
        // (System.currentTimeMillis() - preTime));
    }
}
