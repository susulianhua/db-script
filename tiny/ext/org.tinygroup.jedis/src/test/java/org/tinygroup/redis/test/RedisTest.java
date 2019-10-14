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
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.jedis.JedisManager;
import org.tinygroup.tinyrunner.Runner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;

/**
 * 验证基本的Cache接口<br>
 * 具体的redis配置地址请修改sample.jedisconfig.xml
 */
public class RedisTest {
    public static void main(String[] args) {
        Runner.init(null, new ArrayList<String>());
        JedisManager manager = BeanContainerFactory.getBeanContainer(RedisTest.class.getClassLoader())
                .getBean("jedisManager");
        JedisPool pool = manager.getJedisPool("server01");
        Jedis jedis = pool.getResource();
        jedis.set("name", "aaa");
        String value = jedis.get("name");
        Assert.assertEquals("aaa", value);
        pool.destroy();
    }

}
