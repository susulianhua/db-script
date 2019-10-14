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
import org.tinygroup.context2object.test.convert.EnumObject;
import org.tinygroup.context2object.test.generator2.config.EnumSimpleObjectArray;

/**
 * @author Administrator
 */
public class TestEnumSimpleObjectArray extends BaseTestCast2 {

    public void testArray() {
        Context context = new ContextImpl();
        EnumObject[] enums = new EnumObject[]{EnumObject.MON, EnumObject.FRI};
        context.put("enumObjectArray", enums);
        context.put("name", "a");

        EnumSimpleObjectArray obj = (EnumSimpleObjectArray) generator
                .getObject(null, null, EnumSimpleObjectArray.class.getName(),
                        this.getClass().getClassLoader(), context);

        assertTrue(2 == obj.getEnumObjectArray().length);
        assertEquals(EnumObject.MON, obj.getEnumObjectArray()[0]);
        assertEquals(EnumObject.FRI, obj.getEnumObjectArray()[1]);
        assertEquals("a", obj.getName());
    }

    public void testArray2() {
        Context context = new ContextImpl();
        context.put("enumObjectArray", new String[]{"MON", "OTH"});
        context.put("name", "a");

        EnumSimpleObjectArray obj = (EnumSimpleObjectArray) generator
                .getObject(null, null, EnumSimpleObjectArray.class.getName(),
                        this.getClass().getClassLoader(), context);

        assertTrue(2 == obj.getEnumObjectArray().length);
        assertEquals(EnumObject.MON, obj.getEnumObjectArray()[0]);
        assertEquals(EnumObject.FRI, obj.getEnumObjectArray()[1]);
        assertEquals("a", obj.getName());
    }

}
