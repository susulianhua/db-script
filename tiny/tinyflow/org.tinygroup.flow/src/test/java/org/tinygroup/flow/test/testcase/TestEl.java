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

public class TestEl extends AbstractFlowComponent {

    public void setUp() throws Exception {
        super.setUp();
        DataUtil.reset();
    }

    // c = aa+bb-5 = 2
    // c<a
    public void testEL() {
        Context context = new ContextImpl();
        context.put("aa", 3);
        context.put("bb", 4);
        flowExecutor.execute("testEl", "begin", context);
        assertEquals(0, DataUtil.getData());
    }

    public void testEL1() {
        Context context = new ContextImpl();
        context.put("aa", "3");
        context.put("bb", 4);
        flowExecutor.execute("testEl", "begin", context);
        assertEquals(0, DataUtil.getData());
    }

    // c = aa+bb-5 = 4
    // c>a
    public void testEL2() {
        Context context = new ContextImpl();
        context.put("aa", 3);
        context.put("bb", 6);
        flowExecutor.execute("testEl", "begin", context);
        assertEquals(10, DataUtil.getData());
    }
}