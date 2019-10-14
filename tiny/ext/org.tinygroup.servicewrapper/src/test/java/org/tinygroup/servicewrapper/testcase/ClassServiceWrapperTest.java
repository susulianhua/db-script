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
import org.tinygroup.servicewrapper.*;
import org.tinygroup.tinyrunner.Runner;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ClassServiceWrapperTest {

    private static ClassPublishService classPublishService;

    @BeforeClass
    public static void setUp() throws Exception {
        Runner.init("application.xml", new ArrayList<String>());
        classPublishService = BeanContainerFactory.getBeanContainer(ClassServiceWrapperTest.class.getClassLoader())
                .getBean("classPublishServiceProxy");
    }

    @Test
    public void testHello() {
        String hello = classPublishService.hello("xuanxuan");
        assertEquals(hello,"hello:xuanxuan");
    }

}
