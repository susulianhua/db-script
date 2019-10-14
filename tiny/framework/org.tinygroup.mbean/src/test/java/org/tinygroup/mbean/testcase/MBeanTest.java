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
package org.tinygroup.mbean.testcase;


import org.tinygroup.mbean.Hello;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @description：MBean注册测试
 * @author: qiuqn
 * @version: 2016年5月13日 上午11:27:50
 */
public class MBeanTest {

    public static void main(String[] args) throws Exception {
        MBeanServer mb = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("TinyMBean:name=Hello");
        Hello testMBean = new Hello();
        mb.registerMBean(testMBean, name);
        mb.invoke(name, "print", new Object[]{"我叫小明"}, new String[]{"java.lang.String"});
    }
}
