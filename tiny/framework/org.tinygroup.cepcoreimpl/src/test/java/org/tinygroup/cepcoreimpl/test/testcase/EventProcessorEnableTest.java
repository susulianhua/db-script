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

import junit.framework.Assert;
import org.tinygroup.cepcore.EventProcessor;
import org.tinygroup.cepcoreimpl.test.EventProcessorForTestEnable;

import java.util.List;

public class EventProcessorEnableTest extends CEPCoreBaseTestCase {
    public void setUp() {
        super.setUp();
        EventProcessorForTestEnable e1 = new EventProcessorForTestEnable();
        e1.getServiceInfos().add(initServiceInfo("helloEnableTrue"));
        e1.getServiceInfos().add(initServiceInfo("helloEnablefalse"));
        getCore().registerEventProcessor(e1);
    }

    public void testCase() {
        getCore().process(getEvent("helloEnableTrue"));
        List<EventProcessor> ps = getCore().getEventProcessors();
        for (EventProcessor p : ps) {
            if (EventProcessorForTestEnable.class.getSimpleName().equals(p.getId())) {
                p.setEnable(false);
            }
        }
        try {
            getCore().process(getEvent("helloEnablefalse"));
            Assert.fail();
        } catch (Exception e) {

        }
        for (EventProcessor p : ps) {
            if (EventProcessorForTestEnable.class.getSimpleName().equals(p.getId())) {
                p.setEnable(true);
            }
        }
        getCore().process(getEvent("helloEnableTrue"));
    }
}
