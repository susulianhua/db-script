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
import org.tinygroup.context2object.test.bean.PartMent2;
import org.tinygroup.context2object.test.generator2.testcase.BaseTestCast2;

public class TestInterfaceWithVarName extends BaseTestCast2 {


    public void testPropertyInterfaceList() {
        Context context = new ContextImpl();
        context.put("a.name", "name1");
        context.put("a.num", 11);
        String[] names = {"tomcat", "name1", "name2"};
        String[] colors = {"red", "coller", "coller2"};
        context.put("a.cat.name", "tomcat");
        context.put("a.cat.coller", "red");
        context.put("a.cats.name", names);
        context.put("a.cats.coller", colors);
        context.put("a.catsArray.name", names);
        context.put("a.catsArray.coller", colors);
        PartMent2 part = (PartMent2) generator.getObject("a", null, PartMent2.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(3, part.getCats().size());
        assertEquals("tomcat", part.getCats().get(0).getName());
        assertEquals("name1", part.getCats().get(1).getName());
        assertEquals("name2", part.getCats().get(2).getName());
        assertEquals(3, part.getCatsArray().length);
    }

}
