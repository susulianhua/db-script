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
import org.tinygroup.context2object.test.bean.CatInterface;
import org.tinygroup.context2object.test.bean.SmallCat;

public class TestArray extends BastTestCast {

    public void testObjectArray() {
        Context context = new ContextImpl();
        String[] names = {"tomcat", "name1", "name2"};
        String[] colors = {"red", "coller", "coller2"};
        context.put("smallCat.name", names);
        context.put("smallCat.coller", colors);
//		SmallCat[] parts = (SmallCat[]) generator.getObject(null,null, SmallCat[].class.getName(),this.getClass().getClassLoader(), context);
        SmallCat[] parts = (SmallCat[]) generator.getObjectArray(null, SmallCat.class.getName(), this.getClass().getClassLoader(), context);

        assertEquals(3, parts.length);
        assertEquals("tomcat", parts[0].getName());
        assertEquals("name1", parts[1].getName());
        assertEquals("name2", parts[2].getName());
        assertEquals("red", parts[0].getColler());
        assertEquals("coller", parts[1].getColler());
        assertEquals("coller2", parts[2].getColler());
    }

    public void testObjectArray2() {
        Context context = new ContextImpl();
        String[] names = {"tomcat", "name1", "name2"};
        String[] colors = {"red", "coller"};
        context.put("smallCat.name", names);
        context.put("smallCat.coller", colors);
//		SmallCat[] parts = (SmallCat[]) generator.getObject(null,null, SmallCat[].class.getName(),this.getClass().getClassLoader(), context);
        SmallCat[] parts = (SmallCat[]) generator.getObjectArray(null, SmallCat.class.getName(), this.getClass().getClassLoader(), context);

        assertEquals(2, parts.length);
        assertEquals("tomcat", parts[0].getName());
        assertEquals("name1", parts[1].getName());
        assertEquals("red", parts[0].getColler());
        assertEquals("coller", parts[1].getColler());
    }

    public void testObjectArrayWithOneLength() {
        Context context = new ContextImpl();
        context.put("smallCat.name", "tomcat");
        context.put("smallCat.coller", "red");
//		SmallCat[] parts = (SmallCat[]) generator.getObject(null,null, SmallCat[].class.getName(),this.getClass().getClassLoader(), context);
        SmallCat[] parts = (SmallCat[]) generator.getObjectArray(null, SmallCat.class.getName(), this.getClass().getClassLoader(), context);

        assertEquals(1, parts.length);
        assertEquals("tomcat", parts[0].getName());
        assertEquals("red", parts[0].getColler());
    }

    public void testInterfaceArray() {
        Context context = new ContextImpl();
        String[] names = {"tomcat", "name1", "name2"};
        String[] colors = {"red", "coller", "coller2"};
        context.put("smallCat.name", names);
        context.put("smallCat.coller", colors);
//		CatInterface[] parts = (CatInterface[]) generator.getObject(null,null, CatInterface[].class.getName(),this.getClass().getClassLoader(), context);
        CatInterface[] parts = (CatInterface[]) generator.getObjectArray(null, CatInterface.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(3, parts.length);
        assertEquals(true, parts[0].getName().equals("tomcat"));
        assertEquals(true, parts[1].getName().equals("name1"));
        assertEquals(true, parts[2].getName().equals("name2"));
    }

    public void testStringArray() {
        Context context = new ContextImpl();
        context.put("a", new String[]{"tomcat", "name"});
//		String[] s = (String[]) generator.getObject("a", null,String[].class.getName(),this.getClass().getClassLoader(), context);
        String[] s = (String[]) generator.getObjectArray("a", String.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(2, s.length);
        assertEquals("tomcat", s[0]);
        assertEquals("name", s[1]);
    }

    public void testStringArrayWithOneLength() {
        Context context = new ContextImpl();
        context.put("a", "tomcat");
//		String[] s = (String[]) generator.getObject("a", null,String[].class.getName(),this.getClass().getClassLoader(), context);
        String[] s = (String[]) generator.getObjectArray("a", String.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(1, s.length);
        assertEquals("tomcat", s[0]);
    }


    public void testIntegerArray1() {
        Context context = new ContextImpl();
        context.put("a", "1");
        Integer[] s = (Integer[]) generator.getObjectArray("a", Integer.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(1, s.length);
        assertEquals(Integer.valueOf(1), s[0]);
    }

    public void testIntegerArray2() {
        Context context = new ContextImpl();
        context.put("a", new String[]{"1", "2"});
        Integer[] s = (Integer[]) generator.getObjectArray("a", Integer.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(2, s.length);
        assertEquals(Integer.valueOf(1), s[0]);
        assertEquals(Integer.valueOf(2), s[1]);
    }

    public void testBooleanArray1() {
        Context context = new ContextImpl();
        context.put("a", "true");
        Boolean[] s = (Boolean[]) generator.getObjectArray("a", Boolean.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(1, s.length);
        assertEquals(Boolean.TRUE, s[0]);
    }

    public void testBooleanArray2() {
        Context context = new ContextImpl();
        context.put("a", new String[]{"true", "false"});
        Boolean[] s = (Boolean[]) generator.getObjectArray("a", Boolean.class.getName(), this.getClass().getClassLoader(), context);
        assertEquals(2, s.length);
        assertEquals(Boolean.TRUE, s[0]);
        assertEquals(Boolean.FALSE, s[1]);
    }

}
