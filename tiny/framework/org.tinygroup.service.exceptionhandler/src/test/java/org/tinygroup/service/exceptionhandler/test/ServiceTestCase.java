package org.tinygroup.service.exceptionhandler.test;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.tinyrunner.Runner;

public class ServiceTestCase extends TestCase{
    public void test(){
        Runner.init(null,null);
        CEPCore core = BeanContainerFactory.getBeanContainer(ServiceTestCase.class.getClassLoader()).getBean(CEPCore.CEP_CORE_BEAN);
        Event e = Event.createEvent(Service.class.getName()+".hello", ContextFactory.getContext());
        try {
            core.process(e);
            fail();
        }catch(RuntimeException e1){
            assertEquals("123",e1.getMessage());
        }


    }
}
