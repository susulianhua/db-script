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
import org.tinygroup.context2object.test.generator2.config.EnumSimpleObjectList;

/**
 * @author Administrator
 */
public class TestEnumSimpleObjectList extends BaseTestCast2 {

    public void testList() {
        Context context = new ContextImpl();
        EnumObject[] enums = new EnumObject[]{EnumObject.MON, EnumObject.FRI};
        context.put("enumObjectList", enums);
        context.put("name", "a");
        EnumSimpleObjectList obj = (EnumSimpleObjectList) generator.getObject(null, null, EnumSimpleObjectList.class.getName(), this.getClass().getClassLoader(), context);
        assertTrue(2 == obj.getEnumObjectList().size());
        assertEquals(EnumObject.MON, obj.getEnumObjectList().get(0));
        assertEquals(EnumObject.FRI, obj.getEnumObjectList().get(1));
        assertEquals("a", obj.getName());
    }

    public void testList2() {
        Context context = new ContextImpl();
        context.put("enumObjectList", new String[]{"MON", "OTH"});
        context.put("name", "a");
        EnumSimpleObjectList obj = (EnumSimpleObjectList) generator.getObject(null, null, EnumSimpleObjectList.class.getName(), this.getClass().getClassLoader(), context);
        assertTrue(2 == obj.getEnumObjectList().size());
        assertEquals(EnumObject.MON, obj.getEnumObjectList().get(0));
        assertEquals(EnumObject.FRI, obj.getEnumObjectList().get(1));
        assertEquals("a", obj.getName());
    }

}
