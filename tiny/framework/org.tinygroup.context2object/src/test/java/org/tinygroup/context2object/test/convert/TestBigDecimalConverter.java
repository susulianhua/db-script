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

import java.math.BigDecimal;

public class TestBigDecimalConverter extends BastTestCast {
    public void testBigDecimalBean() {
        Context context = new ContextImpl();
        String[] value = {"11", "21"};
        String[] value2 = {"12", "22"};
        String[] value3 = {"13", "23"};
        context.put("array", value);
        context.put("list", value2);
        context.put("objects.b", value3);
        BigDecimalBean bean = (BigDecimalBean) generator.getObject(null, null,
                BigDecimalBean.class.getName(), this.getClass()
                        .getClassLoader(), context);
        assertEquals(2, bean.getArray().length);
        assertEquals(new BigDecimal(11), bean.getArray()[0]);
        assertEquals(new BigDecimal(21), bean.getArray()[1]);


        assertEquals(2, bean.getList().size());
        assertEquals(new BigDecimal(12), bean.getList().get(0));
        assertEquals(new BigDecimal(22), bean.getList().get(1));

        assertEquals(2, bean.getObjects().size());
        assertEquals(new BigDecimal(13), bean.getObjects().get(0).getB());
        assertEquals(new BigDecimal(23), bean.getObjects().get(1).getB());

    }
}
