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
/**
 *
 */
package org.tinygroup.context2object.test.generator2.testcase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.test.generator2.config.ObjectContainSimpleObjectArray;

/**
 * @author Administrator
 */
public class TestObjectContainSimpleObjectArray extends BaseTestCast2 {

    public void testArray() {
        Context context = new ContextImpl();
        String[] names = new String[]{"a", "b", "c", "d"};
        context.put("simpleObjectArray.name", names);
        context.put("simpleObjectArray.flag", new String[]{"true", "false",
                "true", "false"});

        ObjectContainSimpleObjectArray object = (ObjectContainSimpleObjectArray) generator
                .getObject(null, null, ObjectContainSimpleObjectArray.class
                        .getName(), this.getClass().getClassLoader(), context);

        assertNotNull(object);
        assertTrue(object.getSimpleObjectArray().length == 4);
        assertEquals("a", object.getSimpleObjectArray()[0].getName());
        assertEquals("b", object.getSimpleObjectArray()[1].getName());
        assertEquals("c", object.getSimpleObjectArray()[2].getName());
        assertEquals("d", object.getSimpleObjectArray()[3].getName());

        assertEquals(Boolean.TRUE, object.getSimpleObjectArray()[0].getFlag());
        assertEquals(Boolean.FALSE, object.getSimpleObjectArray()[1].getFlag());
        assertEquals(Boolean.TRUE, object.getSimpleObjectArray()[2].getFlag());
        assertEquals(Boolean.FALSE, object.getSimpleObjectArray()[3].getFlag());
    }

}
