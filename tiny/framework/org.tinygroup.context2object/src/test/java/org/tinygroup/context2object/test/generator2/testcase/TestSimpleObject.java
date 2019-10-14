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
import org.tinygroup.context2object.test.generator2.config.SimpleObject;

public class TestSimpleObject extends BaseTestCast2 {

    public void testSimplePropertyNumberFormatException() {
        Context context = new ContextImpl();
        context.put("bean.length", "11111111111111111111");
        context.put("bean.length2", "22222222222222222");
        try {
            SimpleObject bean = (SimpleObject) generator.getObject("bean",
                    null, SimpleObject.class.getName(), this.getClass()
                            .getClassLoader(), context);
            assertTrue(false);
        } catch (NumberFormatException e) {
            assertTrue(true);
        }
    }

    public void testSimplePropertyBooleanException() {
        Context context = new ContextImpl();
        context.put("bean.flag", "true1");
        SimpleObject bean = (SimpleObject) generator.getObject("bean", null,
                SimpleObject.class.getName(), this.getClass().getClassLoader(),
                context);
        assertEquals(Boolean.FALSE, bean.getFlag());

    }

    public void testSimpleProperty() {
        Context context = new ContextImpl();
        context.put("bean.name", "name");
        context.put("bean.length", "1");
        context.put("bean.length2", "2");
        context.put("bean.flag", "true");
        SimpleObject bean = (SimpleObject) generator.getObject("bean", null,
                SimpleObject.class.getName(), this.getClass().getClassLoader(),
                context);
        assertMethod(bean);
    }

    public void testSimpleProperty2() {
        Context context = new ContextImpl();
        context.put("simpleObject.name", "name");
        context.put("simpleObject.length", "1");
        context.put("simpleObject.length2", "2");
        context.put("simpleObject.flag", "true");
        SimpleObject bean = (SimpleObject) generator.getObject(null, null,
                SimpleObject.class.getName(), this.getClass().getClassLoader(),
                context);
        assertMethod(bean);
    }

    public void testSimpleProperty3() {
        Context context = new ContextImpl();
        context.put("name", "name");
        context.put("length", "1");
        context.put("length2", "2");
        context.put("flag", "true");
        SimpleObject bean = (SimpleObject) generator.getObject(null, null,
                SimpleObject.class.getName(), this.getClass().getClassLoader(),
                context);
        assertMethod(bean);
    }

    private void assertMethod(SimpleObject bean) {
        assertEquals("name", bean.getName());
        assertEquals(2, bean.getLength2());
        assertEquals(Integer.valueOf(1), bean.getLength());
        assertEquals(Boolean.TRUE, bean.getFlag());
    }

}
