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
package org.tinygroup.remoteconfig;

import org.junit.Assert;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.config.ConfigManagerFactory;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.remoteconfig.zk.manager.impl.ZKConfigClientImpl;
import org.tinygroup.tinyrunner.Runner;

import java.util.ArrayList;
import java.util.Map;

public class GeneratorServiceTest {

    public static void main(String[] args) {
        Runner.setRemoteConfigReadClient(new ZKConfigClientImpl());
        Runner.init("application.xml", new ArrayList<String>());

        testApplicationPro();

        testApplicationProFile();

        testSpringPro();

    }

    public static void testApplicationPro() {
        Map<String, String> propertyMap = ConfigurationUtil.getConfigurationManager().getConfiguration();
        Assert.assertEquals("名字1", propertyMap.get("name11"));
        Assert.assertEquals("名字2", propertyMap.get("name22"));
    }

    /**
     * 验证application.xml里定义的配置文件
     */
    public static void testApplicationProFile() {
        Map<String, String> propertyMap = ConfigurationUtil.getConfigurationManager().getConfiguration();
        Assert.assertEquals("asda", propertyMap.get("name33"));
        //propertyMap.put("a", "asda阿斯顿");
        ConfigManagerFactory.getManager().addConfig("a", "asda阿斯顿");
        Assert.assertEquals("asda阿斯顿", propertyMap.get("a"));
    }

    /**
     * 验证spring中的property
     */
    public static void testSpringPro() {
        TestService testService = BeanContainerFactory.getBeanContainer(GeneratorServiceTest.class.getClassLoader()).getBean("testService");
        if (testService != null) {
            Assert.assertEquals("名字1", testService.getName11());
            Assert.assertEquals("名字2", testService.getName22());
            return;
        }
        Assert.fail();
    }
}
