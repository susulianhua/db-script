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
import org.tinygroup.context2object.test.generator2.config.SimpleListArrayObject;

public class TestSimpleListArrayObject extends BaseTestCast2 {

    public void testSimpleListArray() {
        Context context = new ContextImpl();
        String[] names = new String[]{"a", "b"};
        String[] nameLists = new String[]{"a", "b"};
        String[] ints = new String[]{"1", "2"};
        String[] flagArray = new String[]{"true", "false"};

        context.put("nameArray", names);
        context.put("nameList", nameLists);
        context.put("lengthArray", ints);
        context.put("lengthList", ints);
        context.put("flagArray", flagArray);
        context.put("flagList", flagArray);
        SimpleListArrayObject simpleObjects = (SimpleListArrayObject) generator
                .getObject(null, null, SimpleListArrayObject.class.getName(),
                        this.getClass().getClassLoader(), context);

        assertMethod(simpleObjects);
    }

    public void testSimpleListArrayWithVarName() {
        Context context = new ContextImpl();
        String[] names = new String[]{"a", "b"};
        String[] nameLists = new String[]{"a", "b"};
        String[] ints = new String[]{"1", "2"};
        String[] flagArray = new String[]{"true", "false"};

        context.put("value.nameArray", names);
        context.put("value.nameList", nameLists);
        context.put("value.lengthArray", ints);
        context.put("value.lengthList", ints);
        context.put("value.flagArray", flagArray);
        context.put("value.flagList", flagArray);

        SimpleListArrayObject simpleObjects = (SimpleListArrayObject) generator
                .getObject("value", null,
                        SimpleListArrayObject.class.getName(), this.getClass()
                                .getClassLoader(), context);

        assertMethod(simpleObjects);
    }

    public void testSimpleListArrayWithDefaultName() {
        Context context = new ContextImpl();
        String[] names = new String[]{"a", "b"};
        String[] nameLists = new String[]{"a", "b"};
        String[] ints = new String[]{"1", "2"};
        String[] flagArray = new String[]{"true", "false"};

        context.put("simpleListArrayObject.nameArray", names);
        context.put("simpleListArrayObject.nameList", nameLists);
        context.put("simpleListArrayObject.lengthArray", ints);
        context.put("simpleListArrayObject.lengthList", ints);
        context.put("simpleListArrayObject.flagArray", flagArray);
        context.put("simpleListArrayObject.flagList", flagArray);
        SimpleListArrayObject simpleObjects = (SimpleListArrayObject) generator
                .getObject(null, null, SimpleListArrayObject.class.getName(),
                        this.getClass().getClassLoader(), context);
        assertMethod(simpleObjects);
    }

    private void assertMethod(SimpleListArrayObject simpleObjects) {
        assertNotNull(simpleObjects);

        assertTrue(simpleObjects.getNameArray().length == 2);
        assertTrue(simpleObjects.getNameList().size() == 2);
        assertTrue(simpleObjects.getLengthArray().length == 2);
        assertTrue(simpleObjects.getLengthList().size() == 2);
        assertTrue(simpleObjects.getFlagArray().length == 2);
        assertTrue(simpleObjects.getFlagList().size() == 2);

        assertEquals("a", simpleObjects.getNameArray()[0]);
        assertEquals("b", simpleObjects.getNameArray()[1]);
        assertEquals("a", simpleObjects.getNameList().get(0));
        assertEquals("b", simpleObjects.getNameList().get(1));

        assertEquals(new Integer(1), simpleObjects.getLengthArray()[0]);
        assertEquals(new Integer(2), simpleObjects.getLengthArray()[1]);
        assertEquals(new Integer(1), simpleObjects.getLengthList().get(0));
        assertEquals(new Integer(2), simpleObjects.getLengthList().get(1));

        assertEquals(Boolean.TRUE, simpleObjects.getFlagArray()[0]);
        assertEquals(Boolean.FALSE, simpleObjects.getFlagArray()[1]);
        assertEquals(Boolean.TRUE, simpleObjects.getFlagList().get(0));
        assertEquals(Boolean.FALSE, simpleObjects.getFlagList().get(1));
    }
}
