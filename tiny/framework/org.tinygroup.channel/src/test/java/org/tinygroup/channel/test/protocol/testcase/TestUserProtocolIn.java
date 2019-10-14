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
package org.tinygroup.channel.test.protocol.testcase;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.channel.protocol.impl.BaseProtocolIn;
import org.tinygroup.channel.test.protocol.pojo.User;
import org.tinygroup.tinyrunner.Runner;

public class TestUserProtocolIn extends TestCase {
    BaseProtocolIn<String, String> userProtocol;

    protected void setUp() throws Exception {
        Runner.init(null, null);
        userProtocol = BeanContainerFactory.getBeanContainer(
                TestUserProtocolIn.class.getClassLoader()).getBean(
                "userProtocol");
    }

    public void test1() {
        User u = new User("d1", 10, 0);
        String r1String = userProtocol.received(u.toString());
        User r1 = User.parse(r1String);
        // assertEquals(1, r1.getGrade());
        assertEquals("d1", r1.getName());
        assertEquals(0, r1.getWeight());
        User u2 = new User("a1", 11, 0);
        r1String = userProtocol.received(u2.toString());
        r1 = User.parse(r1String);
        // assertEquals(2, r1.getGrade());
        assertEquals("a1", r1.getName());
        assertEquals(0, r1.getWeight());

    }
}
