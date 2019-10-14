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
package org.tinygroup.context2object.test.convert;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.context2object.test.testcase.BastTestCast;

import java.util.List;

public class TestEnumConverter extends BastTestCast {
    public void testEnumArray() {
        Context context = new ContextImpl();
        String[] names = {"MON", "FRI"};
        context.put("enumObject", names);
        EnumObject[] parts = (EnumObject[]) generator.getObjectArray("enumObject", EnumObject.class.getName(), this.getClass().getClassLoader(), context);

        assertEquals(2, parts.length);
        assertEquals(parts[0], EnumObject.MON);
        assertEquals(parts[1], EnumObject.FRI);

    }

    public void testEnumArray2() {
        Context context = new ContextImpl();
        String[] names = {"MON",};
        context.put("enumObject", names);
        EnumObject[] parts = (EnumObject[]) generator.getObjectArray("enumObject", EnumObject.class.getName(), this.getClass().getClassLoader(), context);

        assertEquals(1, parts.length);
        assertEquals(parts[0], EnumObject.MON);

    }

    public void testEnumList() {
        Context context = new ContextImpl();
        String[] names = {"MON", "FRI",};
        context.put("enumObject", names);
        List<EnumObject> parts = (List<EnumObject>) generator.getObjectCollection("enumObject", List.class.getName(), EnumObject.class.getName(), this.getClass().getClassLoader(), context);

        assertEquals(2, parts.size());
        assertEquals(parts.get(0), EnumObject.MON);
        assertEquals(parts.get(1), EnumObject.FRI);

    }

    public void testEnumList2() {
        Context context = new ContextImpl();
        String[] names = {"MON",};
        context.put("enumObject", names);
        List<EnumObject> parts = (List<EnumObject>) generator.getObjectCollection("enumObject", List.class.getName(), EnumObject.class.getName(), this.getClass().getClassLoader(), context);

        assertEquals(1, parts.size());
        assertEquals(parts.get(0), EnumObject.MON);

    }

    public void testBeanEnum() {
        Context context = new ContextImpl();
        String[] names = {"MON", "FRI",};
        String[] names2 = {"MON", "FRI", "MON"};
        context.put("enumBean.array", names);
        context.put("enumBean.list", names);
        context.put("enumBean.simpleList.object", names);
        context.put("enumBean.simpleList.name", names);
        context.put("enumBean.simpleArray.name", names2);
        context.put("enumBean.simpleArray.object", names2);
        EnumBean parts = (EnumBean) generator.getObject("enumBean", null, EnumBean.class.getName(), this.getClass().getClassLoader(), context);

        assertEquals(2, parts.getArray().length);
        assertEquals(parts.getArray()[0], EnumObject.MON);
        assertEquals(parts.getArray()[1], EnumObject.FRI);

        assertEquals(2, parts.getList().size());
        assertEquals(parts.getList().get(0), EnumObject.MON);
        assertEquals(parts.getList().get(1), EnumObject.FRI);


        assertEquals(2, parts.getSimpleList().size());
        assertEquals(parts.getSimpleList().get(0).getObject(), EnumObject.MON);
        assertEquals(parts.getSimpleList().get(1).getObject(), EnumObject.FRI);
        assertEquals(parts.getSimpleList().get(0).getName(), "MON");
        assertEquals(parts.getSimpleList().get(1).getName(), "FRI");

        assertEquals(3, parts.getSimpleArray().length);
        assertEquals(parts.getSimpleArray()[0].getObject(), EnumObject.MON);
        assertEquals(parts.getSimpleArray()[1].getObject(), EnumObject.FRI);
        assertEquals(parts.getSimpleArray()[2].getObject(), EnumObject.MON);
        assertEquals(parts.getSimpleArray()[0].getName(), "MON");
        assertEquals(parts.getSimpleArray()[1].getName(), "FRI");
        assertEquals(parts.getSimpleArray()[2].getName(), "MON");

    }


    public void testBeanEnum2() {
        Context context = new ContextImpl();
        String[] names = {"MON"};
        String[] names2 = {"MON"};
        context.put("enumBean.array", names);
        context.put("enumBean.list", names);
        context.put("enumBean.simpleList.object", names);
        context.put("enumBean.simpleList.name", names);
        context.put("enumBean.simpleArray.name", names2);
        context.put("enumBean.simpleArray.object", names2);
        EnumBean parts = (EnumBean) generator.getObject("enumBean", null, EnumBean.class.getName(), this.getClass().getClassLoader(), context);

        assertEquals(1, parts.getArray().length);
        assertEquals(parts.getArray()[0], EnumObject.MON);

        assertEquals(1, parts.getList().size());
        assertEquals(parts.getList().get(0), EnumObject.MON);


        assertEquals(1, parts.getSimpleList().size());
        assertEquals(parts.getSimpleList().get(0).getObject(), EnumObject.MON);
        assertEquals(parts.getSimpleList().get(0).getName(), "MON");

        assertEquals(1, parts.getSimpleArray().length);
        assertEquals(parts.getSimpleArray()[0].getObject(), EnumObject.MON);
        assertEquals(parts.getSimpleArray()[0].getName(), "MON");

    }

}
