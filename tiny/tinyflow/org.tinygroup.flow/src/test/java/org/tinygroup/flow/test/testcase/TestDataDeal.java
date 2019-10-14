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
package org.tinygroup.flow.test.testcase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.flow.component.AbstractFlowComponent;

public class TestDataDeal extends AbstractFlowComponent {

    public void setUp() throws Exception {
        super.setUp();
        DataUtil.reset();
    }

    public void testTestDataDeal() {
        Context context = new ContextImpl();
        flowExecutor.execute("testDataDeal", "begin", context);
        assertEquals(DataUtil.defaultValue, DataUtil.getData());

    }

    public void testTestDataDeal2() {
        Context context = new ContextImpl();
        flowExecutor.execute("testDataDeal2", "begin", context);
        assertEquals(DataUtil.defaultValue + 1 - 2 + 3, DataUtil.getData());
    }

    public void testOtherFlowNode() {
        Context context = new ContextImpl();
        flowExecutor.execute("testOtherFlowNode", "begin", context);
        assertEquals(3, DataUtil.getData());
    }
}


