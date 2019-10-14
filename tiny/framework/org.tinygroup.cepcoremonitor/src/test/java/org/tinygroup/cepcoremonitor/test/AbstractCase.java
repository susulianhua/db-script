package org.tinygroup.cepcoremonitor.test;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;

public abstract class AbstractCase extends TestCase {

    private CEPCore core;

    public void setUp() {
        Runner.init(getConfig(), new ArrayList<String>());
        core = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean(CEPCore.CEP_CORE_BEAN);
        EventProcessor eventProcessor2 = new EventProcessorForTest();
        eventProcessor2.getServiceInfos().add(initServiceInfo("a"));
        eventProcessor2.getServiceInfos().add(initServiceInfo("exception"));
        getCore().registerEventProcessor(eventProcessor2);

    }

    public String getConfig() {
        return "applicationmonitor.xml";
    }

    public CEPCore getCore() {
        return core;
    }

    protected Event getEvent(String serviceId) {
        Context context = new ContextImpl();
        Event e = Event.createEvent(serviceId, context);
        return e;
    }

    protected ServiceInfoForTest initServiceInfo(String serviceId) {
        ServiceInfoForTest sifft = new ServiceInfoForTest();
        sifft.setServiceId(serviceId);
        return sifft;
    }
}
