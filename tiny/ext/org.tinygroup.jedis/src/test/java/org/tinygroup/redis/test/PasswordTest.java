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
import org.tinygroup.commons.cryptor.Cryptor;
import org.tinygroup.commons.cryptor.DefaultCryptor;
import org.tinygroup.jedis.JedisManager;
import org.tinygroup.jedis.config.JedisConfig;
import org.tinygroup.tinyrunner.Runner;

public class PasswordTest extends TestCase {

    private JedisManager jedisManager;
    private Cryptor cryptor = new DefaultCryptor();

    protected void setUp() throws Exception {
        Runner.init("application.xml", null);
        jedisManager = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean("jedisManager");
    }

    public void testJedisConfig() throws Exception {
        //测试空密码
        JedisConfig j1 = jedisManager.getJedisConfig("server01");
        assertEquals(null, j1.getPassword());

        String stext = "D2C7D032959143E81B6BD60A80268220";
        assertEquals(stext, cryptor.encrypt("password123", "tiny123"));

        //测试密文密码
        JedisConfig j2 = jedisManager.getJedisConfig("server02");
        assertEquals("password123", j2.getPassword());

        //测试明文密码
        JedisConfig j3 = jedisManager.getJedisConfig("server03");
        assertEquals("dog456", j3.getPassword());
    }
}
