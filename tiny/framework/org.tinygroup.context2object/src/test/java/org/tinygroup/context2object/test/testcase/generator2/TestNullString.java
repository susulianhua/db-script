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
import org.tinygroup.context2object.CatChild;
import org.tinygroup.context2object.test.generator2.testcase.BaseTestCast2;

public class TestNullString extends BaseTestCast2 {
    public void testObjectArray() {
        Context context = new ContextImpl();
        context.put("name", "name1");
        context.put("nickName", "");
        CatChild c = (CatChild) generator.getObject(null, null, CatChild.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(c.getNickName(), "");
        System.out.println(c.getNickName().equals(""));
    }
}
