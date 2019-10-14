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
package org.tinygroup.servicewrapper.testcase;

import org.junit.BeforeClass;
import org.junit.Test;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.service.exception.ServiceRunException;
import org.tinygroup.servicewrapper.GeneratorServiceIn;
import org.tinygroup.servicewrapper.InvokeNodeSetter;
import org.tinygroup.servicewrapper.ServiceOrg;
import org.tinygroup.servicewrapper.ServiceUser;
import org.tinygroup.tinyrunner.Runner;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ServiceWrapperTest {

    private static GeneratorServiceIn bean;

    @BeforeClass
    public static void setUp() throws Exception {
        Runner.init("application.xml", new ArrayList<String>());
        bean = BeanContainerFactory.getBeanContainer(ServiceWrapperTest.class.getClassLoader())
                .getBean("busiServiceProxy");
    }

    @Test
    public void testInterceptor() {
        ServiceUser user = new ServiceUser();
        user.setName("username");
        user.setAge(11);
        ServiceOrg org = new ServiceOrg();
        org.setName("hundsun");
        ServiceUser serviceUser = bean.userObject(user, org);
        assertEquals("changerName", serviceUser.getName());

        // xml
        List<ServiceUser> users = new ArrayList<ServiceUser>();
        ServiceUser xmluser = new ServiceUser();
        xmluser.setAge(100);
        xmluser.setName("lilei");
        xmluser.setMale(true);
        users.add(xmluser);
        List<ServiceUser> users2 = bean.userList(user, users);
        assertEquals(users2.get(0).getAge(), 100);

    }

    @Test
    public void testValid() {
        ServiceUser user = new ServiceUser();
        user.setName("username");
        user.setAge(11);
        ServiceOrg org = new ServiceOrg();
        org.setName("hundsun");
        try {
            ServiceUser serviceUser = bean.userObject2(user, org);
            fail();
        } catch (ServiceRunException e) {
            InvocationTargetException i = (InvocationTargetException) e.getCause();
            ConstraintViolationException e2 = (ConstraintViolationException) i.getTargetException();
            for (ConstraintViolation v : e2.getConstraintViolations()) {
                System.out.println(v.getMessage());
            }
        }

    }

    @Test(expected = Exception.class)
    public void testNodeName() {
        ServiceUser user = new ServiceUser();
        user.setName("username");
        user.setAge(11);
        ServiceOrg org = new ServiceOrg();
        org.setName("hundsun");
        InvokeNodeSetter.setNodeName("as");
        bean.userObject(user, org);
    }
}
