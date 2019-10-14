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
package org.tinygroup.cepcoregovernance.test.testcase;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcoregovernance.CommonServiceExecuteContainer;
import org.tinygroup.cepcoregovernance.container.ExecuteTimeInfo;
import org.tinygroup.cepcoregovernance.mbean.GovernanceMonitor;
import org.tinygroup.context.util.ContextFactory;
import org.tinygroup.event.Event;
import org.tinygroup.tinyrunner.Runner;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
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
        assertEquals(2000, CommonServiceExecuteContainer.getLocalTotalTimes().longValue());
        assertEquals(1000, CommonServiceExecuteContainer.getLocalExceptionTimes().longValue());
        assertEquals(1000, CommonServiceExecuteContainer.getLocalSuccessTimes().longValue());
        ExecuteTimeInfo info = CommonServiceExecuteContainer.getLocalServiceExecuteTimeInfo("localService");
        System.out.println("maxTime:" + info.getMaxTime());
        System.out.println("minTime:" + info.getMinTime());
        System.out.println("times:" + info.getTimes());
        System.out.println("totalTime:" + info.getTotalTime());
        forMBean();
    }

    public void forMBean() throws Exception {
        MBeanServer mb = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("TinyMBean:name=GovernanceMonitor");
        GovernanceMonitor gm = new GovernanceMonitor();
        mb.registerMBean(gm, name);
        Long o = (Long) mb.getAttribute(name, "ExceptionTotal");
        assertEquals(1000, o.intValue());
        Long o1 = (Long) mb.getAttribute(name, "LocalTotalTimes");
        assertEquals(2000, o1.intValue());
        Long o2 = (Long) mb.getAttribute(name, "LocalSuccessTimes");
        assertEquals(1000, o2.intValue());
        Long o3 = (Long) mb.getAttribute(name, "LocalExceptionTimes");
        assertEquals(1000, o3.intValue());
        ExecuteTimeInfo o4 = (ExecuteTimeInfo) mb.invoke(name, "getLocalServiceExecuteTimeInfo", new Object[]{"localService"}, new String[]{"java.lang.String"});
        assertEquals(1000, o4.getTimes());
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
