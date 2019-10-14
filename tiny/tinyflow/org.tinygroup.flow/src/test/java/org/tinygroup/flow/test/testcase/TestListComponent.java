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

import java.util.ArrayList;

public class TestListComponent extends AbstractFlowComponent {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testList() {
        Context context = new ContextImpl();
        context.put("ii", 1);
        ArrayList<String> object = new ArrayList<String>();
        object.add("a");
        object.add("b");
        context.put("list", object);
        context.put("str", "aa");
        //sum = a + b
        //sum = a + sum
        flowExecutor.execute("ListFlow", "begin", context);
        assertEquals(context.get("ii1"), 1);
        ArrayList<String> object1 = context.get("list1");
        assertEquals(object.size(), object1.size());
        assertEquals("aa", context.get("str1"));

    }

    public void testList2() {
        Context context = new ContextImpl();
        ArrayList<String> object = new ArrayList<String>();
        object.add("a");
        object.add("b");
        context.put("list", object);
        //sum = a + b
        //sum = a + sum
        flowExecutor.execute("ListFlow2", "begin", context);
        assertEquals(1, context.get("ii1"));
        ArrayList<String> object1 = context.get("list1");
        assertEquals(object.size(), object1.size());
        assertEquals("s12", context.get("str1"));

    }
}
