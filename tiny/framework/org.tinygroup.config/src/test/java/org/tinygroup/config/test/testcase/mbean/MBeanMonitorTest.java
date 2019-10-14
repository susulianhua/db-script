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
package org.tinygroup.config.test.testcase.mbean;

import junit.framework.TestCase;

import org.tinygroup.config.ConfigManagerFactory;
import org.tinygroup.config.mbean.ConfigMonitor;
import org.tinygroup.config.util.ConfigurationUtil;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Map;


/**
 * @description：
 * @author: qiucn
 * @version: 2016年6月29日 上午10:27:56
 */
public class MBeanMonitorTest extends TestCase {

    @SuppressWarnings("unchecked")
    public void testMBean() throws Exception {
        ConfigManagerFactory.getManager().addConfig("aa", "11");
        MBeanServer mb = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("TinyMBean:name=ConfigMonitor");
        ConfigMonitor cm = new ConfigMonitor();
        mb.registerMBean(cm, name);
        Map<String, String> map = (Map<String, String>) mb.getAttribute(name, "Configurations");
        assertEquals(1, map.size());
        Object o1 = mb.invoke(name, "getConfigration", new Object[]{"aa"}, new String[]{"java.lang.String"});
        assertEquals("11", String.valueOf(o1));
    }

}
