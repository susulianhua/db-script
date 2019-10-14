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
package org.tinygroup.database;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.database.util.DataBaseUtil;

import java.util.List;

public class TestTableTest extends TestCase {
    static {
        TestInit.init();

    }

    TableProcessor tableProcessor;

    protected void setUp() throws Exception {
        super.setUp();
        tableProcessor = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean(DataBaseUtil.TABLEPROCESSOR_BEAN);
    }

    public void testGetTable() {
        assertNotNull(tableProcessor.getTable("user"));
        assertNotNull(tableProcessor.getTable("org.tinygroup.test", "user"));
    }

    public void testMysqlCreateSql() {
        List<String> tableSql = tableProcessor.getCreateSql("user", "org.tinygroup.test", "mysql");
        System.out.println(tableSql);
        assertEquals(1, tableSql.size());
    }

    public void testOracleCreateSql() {
        List<String> tableSql = tableProcessor.getCreateSql("user", "org.tinygroup.test", "oracle");
        System.out.println(tableSql);
        assertEquals(5, tableSql.size());
    }

}
