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
import org.tinygroup.database.initdata.InitDataProcessor;
import org.tinygroup.database.util.DataBaseUtil;

public class InitDataProcessorTest extends TestCase {
    static {
        TestInit.init();
    }

    private InitDataProcessor initDataProcessor;

    protected void setUp() throws Exception {
        super.setUp();
        initDataProcessor = BeanContainerFactory.getBeanContainer(this.getClass().getClassLoader()).getBean(DataBaseUtil.INITDATA_BEAN);
    }

    public void test() {

    }

/*    public void testOracleInitSql() {
        System.out.println(initDataProcessor.getInitSql("oracle").toString());
        assertEquals(2, initDataProcessor.getInitSql("oracle").size());
    }*/

    /*public void testOracleDeInitSql() {
        System.out.println(initDataProcessor.getDeInitSql("oracle").toString());
        assertEquals(2, initDataProcessor.getDeInitSql("oracle").size());
    }

    public void testDb2InitSql() {
        System.out.println(initDataProcessor.getInitSql("db2").toString());
        assertEquals(2, initDataProcessor.getInitSql("db2").size());
    }

    public void testDb2DeInitSql() {
        System.out.println(initDataProcessor.getDeInitSql("db2").toString());
        assertEquals(2, initDataProcessor.getDeInitSql("db2").size());
    }

    public void testMysqlInitSql() {
        System.out.println(initDataProcessor.getInitSql("mysql").toString());
        assertEquals(2, initDataProcessor.getInitSql("mysql").size());
    }

    public void testMysqlDeInitSql() {
        System.out.println(initDataProcessor.getDeInitSql("mysql").toString());
        assertEquals(2, initDataProcessor.getDeInitSql("mysql").size());
    }

    public void testH2InitSql() {
        System.out.println(initDataProcessor.getInitSql("h2").toString());
        assertEquals(2, initDataProcessor.getInitSql("h2").size());
    }

    public void testH2DeInitSql() {
        System.out.println(initDataProcessor.getDeInitSql("h2").toString());
        assertEquals(2, initDataProcessor.getDeInitSql("h2").size());
    }

    public void testDerbyInitSql() {
        System.out.println(initDataProcessor.getInitSql("derby").toString());
        assertEquals(2, initDataProcessor.getInitSql("derby").size());
    }

    public void testDerbyDeInitSql() {
        System.out.println(initDataProcessor.getDeInitSql("derby").toString());
        assertEquals(2, initDataProcessor.getDeInitSql("derby").size());
    }

    public void testGetInitSqlByTable() {

        List<String> list = initDataProcessor.getInitSql("user", "oracle");
        System.out.println("testGetInitSqlByTable,aa:");
        System.out.println(list.toString());
        assertEquals(2, list.size());
    }



    public void testGetInitDataByTable() {
        assertFalse(initDataProcessor.getInitData("user") == null);
        assertEquals("test_user", initDataProcessor.getInitData("user").getTableId());
    }*/

}
