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
package org.tinygroup.context2object.test.testcase.generator2;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.test.bean.People2;
import org.tinygroup.context2object.test.generator2.testcase.BaseTestCast2;

import java.util.List;

public class TestNull extends BaseTestCast2 {
    public void testNullObject() {
        Context context = new ContextImpl();
        Object people = generator.getObject(null, null, People2.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(null, people);

    }

    public void testNullList() {
        Context context = new ContextImpl();
        Object peoples = generator.getObjectCollection(null, List.class.getName(), People2.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(null, peoples);

    }

    public void testNullArray() {
        Context context = new ContextImpl();
        Object peoples = generator.getObjectArray(null, People2.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(null, peoples);

    }

    public void testNullProperty() {
        Context context = new ContextImpl();
        context.put("people2.type", "type1");
        People2 people = (People2) generator.getObject(null, null, People2.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(null, people.getNames());
        assertEquals("type1", people.getType());
    }
}
