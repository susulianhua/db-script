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

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.jedis.JedisManager;
import org.tinygroup.jedis.config.JedisConfig;
import org.tinygroup.tinyrunner.Runner;

/**
 * 增加默认jedis配置的设置
 *
 * @author yancheng11334
 */
public class JedisConfigTest extends TestCase {

    private JedisManager jedisManager;

    protected void setUp() throws Exception {
        Runner.init("application.xml", null);
        jedisManager = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean("jedisManager");
    }

    public void testJedisConfig() {

        JedisConfig j1 = jedisManager.getJedisConfig("server01");
        assertEquals("testdb", j1.getHost());

        //测试默认配置
        JedisConfig j2 = jedisManager.getJedisConfig(null);
        assertEquals("testdb1", j2.getHost());

        j2 = jedisManager.getJedisConfig("XXXXX");
        assertEquals(63000, j2.getPort());
    }
}
