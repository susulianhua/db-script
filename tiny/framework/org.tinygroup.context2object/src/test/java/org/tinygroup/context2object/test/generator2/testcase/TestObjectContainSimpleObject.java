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
package org.tinygroup.context2object.test.generator2.testcase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.test.generator2.config.ObjectContainSimpleObject;

public class TestObjectContainSimpleObject extends BaseTestCast2 {

    public void testObjectContainSimple() {
        Context context = new ContextImpl();
        context.put("name", "name");
        context.put("simpleObject.name", "1");
        context.put("simpleObject.length", new Integer(5));
        context.put("simpleObject.length2", "2");
        context.put("simpleObject.flag", true);
        ObjectContainSimpleObject object = (ObjectContainSimpleObject) generator.getObject(null, null,
                ObjectContainSimpleObject.class.getName(), this.getClass().getClassLoader(),
                context);
        assertEquals("name", object.getName());
        assertTrue(2 == object.getSimpleObject().getLength2());
        assertEquals(Integer.valueOf(5), object.getSimpleObject().getLength());
        assertEquals(Boolean.TRUE, object.getSimpleObject().getFlag());

    }
}
