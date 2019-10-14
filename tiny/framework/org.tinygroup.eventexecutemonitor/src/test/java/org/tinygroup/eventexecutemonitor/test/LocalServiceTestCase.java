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
package org.tinygroup.eventexecutemonitor.test;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.eventexecutemonitor.container.EventExecuteTimeInfo;
import org.tinygroup.eventexecutemonitor.counter.EventExecuteMonitor;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;

public class LocalServiceTestCase extends TestCase {
    private CEPCore core;

    public void setUp() {
        Runner.init("application.xml", new ArrayList<String>());
        core = BeanContainerFactory.getBeanContainer(
                LocalServiceTestCase.class.getClassLoader()).getBean(
                CEPCore.CEP_CORE_BEAN);
    }

    public void testServiceTime() throws Exception {
        for (int i = 0; i < 1000; i++) {
            executeService("exceptionService");
            executeService("localService");
        }

        Thread.sleep(2000);
        assertNull(EventExecuteMonitor.getRemoteServiceExecuteTimeInfo("exceptionService"));
        assertNull(EventExecuteMonitor.getRemoteServiceExecuteTimeInfo("localService"));

        assertNull(EventExecuteMonitor.getRemoteTotalTimes("exceptionService"));
        assertNull(EventExecuteMonitor.getRemoteTotalTimes("localService"));


        assertEquals(1000, EventExecuteMonitor.getLocalTotalTimes("exceptionService").longValue());
        assertEquals(1000, EventExecuteMonitor.getLocalTotalTimes("localService").longValue());

        EventExecuteTimeInfo localInfoException = EventExecuteMonitor.getLocalServiceExecuteTimeInfo("exceptionService");
        assertNull(localInfoException);

        EventExecuteTimeInfo localInfo = EventExecuteMonitor.getLocalServiceExecuteTimeInfo("localService");
        assertEquals(1000, localInfo.getTimes());


        System.out.println("===>times:" + localInfo.getTimes());
        System.out.println("===>totaltime:" + localInfo.getTotalTime());
        System.out.println("===>0ms-10ms:" + localInfo.getTimes0ms_10ms());
        System.out.println("===>10ms-100ms:" + localInfo.getTimes10ms_100ms());
        System.out.println("===>100ms-1s:" + localInfo.getTimes100ms_1s());
        System.out.println("===>1s-10s:" + localInfo.getTimes1s_10s());
        System.out.println("===>10s以上:" + localInfo.getTimes10s_infinity());
    }

    private void executeService(String serviceId) {
        try {
            core.process(getEvent(serviceId));
        } catch (Exception e) {
        }

    }

    private Event getEvent(String serviceId) {
        return Event.createEvent(serviceId, ContextFactory.getContext());
    }
}
