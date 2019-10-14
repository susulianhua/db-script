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
import org.tinygroup.context2object.test.generator2.config.IntefaceObject;


/**
 * @author Administrator
 */
public class TestIntefaceObject extends BaseTestCast2 {

    public void testArray() {
        Context context = new ContextImpl();
        String[] nameLists = new String[]{"a", "b", "c"};
        String[] nameArrays = new String[]{"q", "w", "e"};
        context.put("cats.name", nameLists);
        context.put("catsArray.name", nameArrays);
        context.put("cat.name", "a");
        context.put("name", "z");

        IntefaceObject obj = (IntefaceObject) generator.getObject(null, null, IntefaceObject.class.getName(), this.getClass().getClassLoader(), context);

        assertTrue(3 == obj.getCats().size());
        assertEquals("a", obj.getCats().get(0).getName());
        assertEquals("b", obj.getCats().get(1).getName());
        assertEquals("c", obj.getCats().get(2).getName());

        assertTrue(3 == obj.getCatsArray().length);
        assertEquals("q", obj.getCatsArray()[0].getName());
        assertEquals("w", obj.getCatsArray()[1].getName());
        assertEquals("e", obj.getCatsArray()[2].getName());

        assertEquals("a", obj.getCat().getName());
        assertEquals("z", obj.getName());
    }

}
