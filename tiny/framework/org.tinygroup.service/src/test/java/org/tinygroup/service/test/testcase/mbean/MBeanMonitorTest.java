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
package org.tinygroup.service.test.testcase.mbean;

import junit.framework.TestCase;
import org.tinygroup.event.Parameter;
import org.tinygroup.service.PrintContextService;
import org.tinygroup.service.mbean.ServiceMonitor;
import org.tinygroup.service.registry.ServiceRegistry;
import org.tinygroup.service.registry.ServiceRegistryItem;
import org.tinygroup.service.registry.impl.ServiceRegistryImpl;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @description：
 * @author: qiucn
 * @version: 2016年6月29日 上午10:27:56
 */
public class MBeanMonitorTest extends TestCase {

    public void testmbean() throws Exception {
        ServiceRegistry serviceRegistry = new ServiceRegistryImpl();
        serviceRegistry.clear();
        PrintContextService printContextService = new PrintContextService();
        ServiceRegistryItem item = new ServiceRegistryItem();
        item.setServiceId("aaaa");
        Parameter parameterDescriptor = new Parameter();
        parameterDescriptor.setArray(false);
        parameterDescriptor.setName("aa");
        parameterDescriptor.setType("java.lang.String");
        parameterDescriptor.setRequired(true);
        item.setService(printContextService);
        List<Parameter> parameterDescriptors = new ArrayList<Parameter>();
        parameterDescriptors.add(parameterDescriptor);
        item.setParameters(parameterDescriptors);
        item.setResults(parameterDescriptors);
        serviceRegistry.registerService(item);

        MBeanServer mb = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("TinyMBean:name=ServiceMonitor");
        ServiceMonitor sm = new ServiceMonitor();
        sm.setServiceRegistry(serviceRegistry);
        mb.registerMBean(sm, name);
        Integer o = (Integer) mb.getAttribute(name, "ServiceTotal");
        assertEquals(1, o.intValue());
        boolean o1 = (Boolean) mb.invoke(name, "isExistLocalService", new Object[]{"aaaa"}, new String[]{"java.lang.String"});
        assertEquals(true, o1);
    }
}
