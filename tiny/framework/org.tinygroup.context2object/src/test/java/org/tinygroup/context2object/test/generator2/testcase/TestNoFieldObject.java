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
import org.tinygroup.context2object.test.generator2.config.NoFieldObject;

/**
 * @author ywj
 */
public class TestNoFieldObject extends BaseTestCast2 {

    public void testSimpleProperty() {
        Context context = new ContextImpl();
        context.put("bean.name", "name");
        context.put("bean.age", "11");
        NoFieldObject bean = (NoFieldObject) generator.getObject("bean", null,
                NoFieldObject.class.getName(), this.getClass().getClassLoader(),
                context);
        assertEquals("name", bean.getName());
        assertEquals(Integer.valueOf(11), bean.getAge());
    }

}
