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
package org.tinygroup.context2object.test.testcase;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.test.bean.CommonObject;

public class TestCommonObject extends BastTestCast {

    public void testCommonObject() {
        Context context = new ContextImpl();
        context.put("field.in", "1");
        context.put("field.name", "name");
        CommonObject obj = (CommonObject) generator.getObject("field", null,
                CommonObject.class.getName(), this.getClass().getClassLoader(),
                context);
        assertNotNull(obj);
        assertEquals(Integer.valueOf("1"), obj.getIn());
        assertEquals("name", obj.getName());
    }


    public void testCommonObject2() {
        Context context = new ContextImpl();
        context.put("field.in", "1");
        CommonObject obj = (CommonObject) generator.getObject("field", null,
                CommonObject.class.getName(), this.getClass().getClassLoader(),
                context);
        assertNotNull(obj);
        assertEquals(Integer.valueOf("1"), obj.getIn());
    }
}
