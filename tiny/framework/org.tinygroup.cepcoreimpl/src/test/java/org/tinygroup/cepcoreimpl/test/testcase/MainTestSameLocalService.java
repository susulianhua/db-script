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
import org.tinygroup.cepcore.CEPCore;
import org.tinygroup.cepcoreimpl.CEPCoreImpl;
import org.tinygroup.cepcoreimpl.test.EventProcessorForTestSameLocalService;
import org.tinygroup.cepcoreimpl.test.ServiceInfoForTest;
import org.tinygroup.event.Parameter;

public class MainTestSameLocalService extends TestCase {

    private static void dealSame() {
        CEPCore core = new CEPCoreImpl();
        EventProcessorForTestSameLocalService e1 = new EventProcessorForTestSameLocalService();
        e1.setId("e1");
        e1.getServiceInfos().add(getServiceInfo("a", false));

        EventProcessorForTestSameLocalService e2 = new EventProcessorForTestSameLocalService();
        e2.setId("e2");
        e2.getServiceInfos().add(getServiceInfo("a", true));

        core.registerEventProcessor(e1);
        core.registerEventProcessor(e2);
    }

    public static void main(String[] args) {
        dealSame();
    }

    private static ServiceInfoForTest getServiceInfo(String id, boolean param) {
        ServiceInfoForTest info = new ServiceInfoForTest();
        info.setServiceId(id);
        if (param) {
            info.getParameters().add(new Parameter());
        }
        return info;

    }

    public void testCase1() {
        dealSame();
    }

}
