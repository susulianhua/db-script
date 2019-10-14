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
package org.tinygroup.cepcoreimpl.test.testcase;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcoreimpl.test.AsynchronousEventProcessorForTest;
import org.tinygroup.cepcoreimpl.test.EventProcessorForTest;
import org.tinygroup.cepcoreimpl.test.ServiceInfoForTest;
import org.tinygroup.cepcoreimpl.test.newcase.threadlocal.ThreadLocalServiceProcesor;
import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.event.Event;
import org.tinygroup.event.Parameter;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;

public abstract class CEPCoreBaseTestCase extends TestCase {
    protected static final String SERVICE_ID = "asynchronousService";
    private CEPCore core;


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

    protected ServiceInfoForTest initServiceInfo2(String serviceId) {
        ServiceInfoForTest sifft = new ServiceInfoForTest();
        sifft.setServiceId(serviceId);
        sifft.getParameters().add(new Parameter());
        return sifft;
    }

    public void setUp() {
        Runner.init("application.xml", new ArrayList<String>());
        core = BeanContainerFactory.getBeanContainer(
                this.getClass().getClassLoader())
                .getBean(CEPCore.CEP_CORE_BEAN);
        EventProcessor eventProcessor = new AsynchronousEventProcessorForTest();
        eventProcessor.getServiceInfos().add(initServiceInfo(SERVICE_ID));
        eventProcessor.getServiceInfos().add(initServiceInfo2("conflictService"));
        getCore().registerEventProcessor(eventProcessor);

        EventProcessor eventProcessor2 = new EventProcessorForTest();
        eventProcessor2.getServiceInfos().add(initServiceInfo("a"));
        eventProcessor2.getServiceInfos().add(initServiceInfo("b"));
        eventProcessor2.getServiceInfos().add(initServiceInfo("exception"));
        eventProcessor2.getServiceInfos().add(initServiceInfo("conflictService"));
        getCore().registerEventProcessor(eventProcessor2);

        EventProcessor eventProcessor3 = new ThreadLocalServiceProcesor();
        eventProcessor3.getServiceInfos().add(initServiceInfo("threadLocalService"));
        getCore().registerEventProcessor(eventProcessor3);


    }

}
