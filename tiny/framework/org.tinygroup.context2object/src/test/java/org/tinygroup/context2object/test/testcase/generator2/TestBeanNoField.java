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
import org.tinygroup.context2object.test.bean.BeanField;
import org.tinygroup.context2object.test.generator2.testcase.BaseTestCast2;

public class TestBeanNoField extends BaseTestCast2 {

    public void testRun() {
        Context context = new ContextImpl();
        context.put("name", "name1");
        context.put("field.name", "name2");

        BeanField bean = (BeanField) generator.getObject(null, null,
                BeanField.class.getName(), this.getClass().getClassLoader(),
                context);
        assertEquals("name1", bean.getName());
        assertEquals("name2", bean.getField().getName());
    }
}
