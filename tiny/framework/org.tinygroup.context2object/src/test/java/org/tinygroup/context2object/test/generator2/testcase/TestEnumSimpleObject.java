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
import org.tinygroup.context2object.test.generator2.config.EnumSimpleObject;

/**
 * @author Administrator
 */
public class TestEnumSimpleObject extends BaseTestCast2 {

    public void testEnumSimple() {
        Context context = new ContextImpl();
        context.put("enumObject", EnumObject.MON);
        context.put("name", "a");

        EnumSimpleObject obj = (EnumSimpleObject) generator.getObject(null, null, EnumSimpleObject.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(EnumObject.MON, obj.getEnumObject());
        assertEquals("a", obj.getName());
    }

    public void testEnumSimple1() {
        Context context = new ContextImpl();
        context.put("enumSimpleObject.enumObject", EnumObject.MON);
        context.put("enumSimpleObject.name", "a");

        EnumSimpleObject obj = (EnumSimpleObject) generator.getObject(null, null, EnumSimpleObject.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(EnumObject.MON, obj.getEnumObject());
        assertEquals("a", obj.getName());
    }

    public void testEnumSimple2() {
        Context context = new ContextImpl();
        context.put("abc.enumObject", EnumObject.MON);
        context.put("abc.name", "a");

        EnumSimpleObject obj = (EnumSimpleObject) generator.getObject("abc", null, EnumSimpleObject.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(EnumObject.MON, obj.getEnumObject());
        assertEquals("a", obj.getName());
    }

}
