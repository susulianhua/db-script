package org.tinygroup.service.test.classpublisher.apptest;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.Context;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.service.test.classpublisher.TestUser;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiucn on 2018/3/19.
 */
public class ApplicationTest extends TestCase{
    public void testApp(){
        Runner.init("application.xml", null);
        CEPCore cep = BeanContainerFactory.getBeanContainer(ApplicationTest.class.getClassLoader()).getBean(CEPCore.CEP_CORE_BEAN);

        Context context = ContextFactory.getContext();
        context.put("name", "haha");
        callService(cep, "org.tinygroup.service.test.classpublisher.service.SameNameService.sameName", context);
        assertNull(context.get("result"));

        callService(cep, "org.tinygroup.service.test.classpublisher.service.SameNameService.sameName1", context);
        assertNull(context.get("result"));

        callService(cep, "org.tinygroup.service.test.classpublisher.service.TestService.stringService", context);
        assertEquals("haha",context.get("result"));

        context.clear();
        context.put("list", new ArrayList<String>());
        callService(cep, "org.tinygroup.service.test.classpublisher.service.TestService.listService", context);
        List<String> listService = context.get("result");
        assertEquals(2,listService.size());

        context.clear();
        context.put("strs", new String[]{"1", "2"});
        callService(cep, "org.tinygroup.service.test.classpublisher.service.TestService.arrayService", context);
        String[] arrayService = context.get("result");
        assertEquals(2,arrayService.length);

        context.clear();
        context.put("user", new TestUser());
        callService(cep, "org.tinygroup.service.test.classpublisher.service.TestService.userService", context);
        TestUser userService = context.get("result");
        assertEquals("张三",userService.getName());
        assertEquals(18,userService.getAge());

        context.clear();
        context.put("userList", new ArrayList<TestUser>());
        callService(cep, "org.tinygroup.service.test.classpublisher.service.TestService.listUserService", context);
        List<TestUser> listUserService = context.get("result");
        assertEquals(1,listUserService.size());

        context.clear();
        context.put("users", new TestUser[]{new TestUser()});
        callService(cep, "org.tinygroup.service.test.classpublisher.service.TestService.arrayUserService", context);
        TestUser[] arrayUserService = context.get("result");
        assertEquals(1,arrayUserService.length);

        context.clear();
        context.put("name", "李四");
        callService(cep, "org.tinygroup.service.test.classpublisher.service.InheritService.paraent", context);
        assertEquals("李四", context.get("result"));

        Runner.stop();
    }

    private static void callService(CEPCore cep, String serviceId, Context context) {
        Event e = Event.createEvent(serviceId, context);
        cep.process(e);
    }
}
