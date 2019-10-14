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
package org.tinygroup.tinydb.testcase.operator;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.exception.TinyDbException;
import org.tinygroup.tinydb.test.BaseTest;

/**
 * 功能说明: 带方言函数的sql语句测试用例
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-21 <br>
 * <br>
 */
public class TestSqlOperatorWithDialect extends BaseTest {


    public void testDialectFunction() throws TinyDbException {
        Bean[] insertBeans = getBeans(25);
        getOperator().batchDelete(insertBeans);
        getOperator().batchInsert(insertBeans);
        String sql = "select #{count(*)} as acount from ANIMAL";
        Bean value = getOperator().getSingleValue(sql);
        int acount = (Integer) value.getProperty("acount");
        assertEquals(25, acount);

        sql = "select #{concat(name,'con')} as con from ANIMAL";
        Bean[] beans = getOperator().getBeans(sql);

        assertEquals("testSqlcon", beans[0].getProperty("con"));

        getOperator().batchDelete(insertBeans);
    }

    public void testMutiDialectFunction() throws TinyDbException {
        Bean[] insertBeans = getBeans(25);
        getOperator().batchDelete(insertBeans);
        getOperator().batchInsert(insertBeans);
        String sql = "select #{substr(#{nvl(name,'')},1,2)} as aname from ANIMAL";
        Bean[] beans = getOperator().getBeans(sql);
        assertEquals("te", beans[0].getProperty("aname"));
        getOperator().batchDelete(insertBeans);

    }
}
