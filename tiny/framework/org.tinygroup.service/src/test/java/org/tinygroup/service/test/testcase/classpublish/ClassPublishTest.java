package org.tinygroup.service.test.testcase.classpublish;

import junit.framework.TestCase;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.service.test.base.ServiceUser;
import org.tinygroup.service.test.service.classpublish.*;
import org.tinygroup.service.util.ServiceTestUtil;

public class ClassPublishTest extends TestCase {
    public void testBase() {
        assertServiceExit(BaseInterface.class.getName() + ".baseMethod");

    }

    public void testSecond() {
        assertServiceExit(SecondInterface.class.getName() + ".baseMethod");
        assertServiceExit(SecondInterface.class.getName() + ".secondMethod");
    }

    public void testBaseService() {
        assertServiceExit(ClassPublishService.class.getName() + ".hello");
    }

    public void assertServiceExit(String serviceId) {
        assertNotNull(ServiceTestUtil.getProvider().getService(serviceId));
    }

    public void testSameNameService() {
        assertServiceExit(SameNameService.class.getName() + ".sameName_String");
        assertServiceExit(SameNameService.class.getName() + ".sameName");
    }

    public void testGenericTypeService() {
        assertServiceExit(GenericTypeService.class.getName() + ".returnService");
        assertServiceExit(GenericTypeService.class.getName() + ".returnService2");
        assertServiceExit(GenericTypeService.class.getName() + ".returnService3");
        assertServiceExit(GenericTypeService.class.getName() + ".paramService");
        assertServiceExit(GenericTypeService.class.getName() + ".paramService2");
        assertServiceExit(GenericTypeService.class.getName() + ".paramService3");
    }

    public void testTestService() {
        assertServiceExit(TestService.class.getName() + ".stringService");
        assertServiceExit(TestService.class.getName() + ".listService");
        assertServiceExit(TestService.class.getName() + ".arrayService");
        assertServiceExit(TestService.class.getName() + ".userService");
        assertServiceExit(TestService.class.getName() + ".listUserService");
        assertServiceExit(TestService.class.getName() + ".arrayUserService");

    }
}
