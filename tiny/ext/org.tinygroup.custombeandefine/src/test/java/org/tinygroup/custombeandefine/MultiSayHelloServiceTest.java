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
package org.tinygroup.custombeandefine;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tinygroup.custombeandefine.action.SayHelloAction;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:primary-true.beans.xml")
public class MultiSayHelloServiceTest {

    @Autowired
    private SayHelloAction sayHelloAction;

    @Test
    public void testMultiImpl() {
        sayHelloAction.sayHello();
        Assert.assertEquals("ProxySayHelloService", VariableHolder
                .getInstance().getVariable());
    }

}
