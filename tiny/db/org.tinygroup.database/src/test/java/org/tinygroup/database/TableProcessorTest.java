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

public class TableProcessorTest extends TestCase {
    static {
        TestInit.init();

    }

    TableProcessor tableProcessor;

    protected void setUp() throws Exception {
        super.setUp();
        tableProcessor = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean(DataBaseUtil.TABLEPROCESSOR_BEAN);
    }

    public void testGetTableStringString() {
        assertNotNull(tableProcessor.getTable("database", "aa"));
    }

    public void testGetTableString() {
        assertNotNull(tableProcessor.getTable("aa"));
    }

    public void testGetCreateSqlStringStringString() {
        System.out.println("aa.bb.aa,sql:");
        List<String> tableSql = tableProcessor.getCreateSql("aa", "aa.bb", "oracle");
        System.out.println(tableSql);
//		assertEquals("CREATE TABLE aa(aa varchar(12),bb varchar(112))",
//				tableSql);
    }

    public void testGetCreateSqlStringStringString1() {
        List<String> tableSql = tableProcessor.getCreateSql("bb", "aa.bb", "oracle");
        System.out.println("aa.bb.bb,sql:");
        System.out.println(tableSql);
    }

    public void testGetCreateSqlStringString() {
        List<String> tableSql = tableProcessor.getCreateSql("aa", "oracle");
        System.out.println("aa,sql:");
        System.out.println(tableSql);
    }

    public void testGetCreateSqlStringStringMySql() {
        List<String> tableSql = tableProcessor.getCreateSql("aa", "mysql");
        System.out.println("aa,mysql sql:");
        System.out.println(tableSql);
    }

}
