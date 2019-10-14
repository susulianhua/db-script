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
package org.tinygroup.flow.test.testcase.mbean;

import org.tinygroup.flow.component.AbstractFlowComponent;
import org.tinygroup.flow.mbean.FlowMonitor;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @description：
 * @author: qiucn
 * @version: 2016年6月29日 上午10:27:56
 */
public class MBeanMonitorTest extends AbstractFlowComponent {

    /**
     * @description：逻辑流程监控
     * @author: qiucn
     * @version: 2016年6月30日下午4:27:54
     */
    public void testFlowMBean() throws Exception {

        MBeanServer mb = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("TinyMBean:name=FlowMonitor");
        FlowMonitor sm = new FlowMonitor();
        sm.setFlowExecutor(flowExecutor);
        mb.registerMBean(sm, name);
        Object o = mb.invoke(name, "isExistFlowService",
                new Object[]{"flowExceptionTest"},
                new String[]{"java.lang.String"});
        assertEquals(true, o);
        Object o1 = mb.invoke(name, "isExistComponent",
                new Object[]{"exceptionSwitchComponent"},
                new String[]{"java.lang.String"});
        assertEquals(true, o1);
        Integer o2 = (Integer) mb.getAttribute(name, "FlowServiceTotal");
        assertEquals(flowExecutor.getFlowIdMap().size(), o2.intValue());
        Integer o3 = (Integer) mb.getAttribute(name, "ComponentTotal");
        assertEquals(flowExecutor.getComponentDefines().size(), o3.intValue());
    }

    /**
     * @description：页面流程监控
     * @author: qiucn
     * @version: 2016年6月30日下午4:28:02
     */
    public void testPageflowMBean() throws Exception {

        MBeanServer mb = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("TinyMBean:name=PageFlowMonitor");
        FlowMonitor sm = new FlowMonitor();
        sm.setFlowExecutor(pageFlowExecutor);
        mb.registerMBean(sm, name);
        Object o = mb.invoke(name, "isExistFlowService",
                new Object[]{"pageflowreleaseFlow"},
                new String[]{"java.lang.String"});
        assertEquals(true, o);
        Object o1 = mb.invoke(name, "isExistComponent",
                new Object[]{"sumComponentPageFlow"},
                new String[]{"java.lang.String"});
        assertEquals(true, o1);
        Integer o2 = (Integer) mb.getAttribute(name, "FlowServiceTotal");
        assertEquals(pageFlowExecutor.getFlowIdMap().size(), o2.intValue());
        Integer o3 = (Integer) mb.getAttribute(name, "ComponentTotal");
        assertEquals(pageFlowExecutor.getComponentDefines().size(), o3.intValue());
    }
}
