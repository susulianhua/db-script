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
package org.tinygroup.redis.test;

import junit.framework.Assert;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Protocol;

import java.util.HashMap;
import java.util.Map;

public class JedisSentinelPoolTestFailOver {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>();
        String sentinel = "testdb:33333";
        map.put(sentinel, sentinel);
        JedisSentinelPool pool = new JedisSentinelPool("master1", map.keySet(), IJedisConstant.PASSWOPD);
        Jedis jedis = pool.getResource();
        jedis.set("a", "aaaaaaaaaaaaaaaaaaa");
        pool.destroy();
        String host = "testdb";
        JedisPool jPool = new JedisPool(new GenericObjectPoolConfig(), host, 11111, Protocol.DEFAULT_TIMEOUT, IJedisConstant.PASSWOPD);
        System.out.println(jPool.getResource().get("a"));
        Assert.assertEquals("aaaaaaaaaaaaaaaaaaa", jPool.getResource().get("a"));
        jPool.destroy();
    }
}
