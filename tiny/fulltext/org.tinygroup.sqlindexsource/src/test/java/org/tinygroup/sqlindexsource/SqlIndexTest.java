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
package org.tinygroup.sqlindexsource;

import junit.framework.TestCase;
import org.tinygroup.beancontainer.BeanContainer;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.commons.tools.Resources;
import org.tinygroup.fulltext.FullText;
import org.tinygroup.fulltext.Pager;
import org.tinygroup.fulltext.document.Document;
import org.tinygroup.tinyrunner.Runner;

import java.nio.charset.Charset;
import java.sql.*;

public class SqlIndexTest extends TestCase {

    private final static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private final static String url = "jdbc:derby:TESTDB;create=true";
    private final static String user = "opensource";
    private final static String password = "opensource";
    private FullText fullText;

    protected void setUp() throws Exception {
        super.setUp();
        Runner.init("application.xml", null);

        BeanContainer<?> container = BeanContainerFactory.getBeanContainer(this
                .getClass().getClassLoader());
        fullText = container.getBean("memoryFullText");

        Class.forName(driver);

        initDerby();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private void initDerby() throws Exception {
        Connection conn = getConnection();
        Statement stmt = null;
        try {
            ScriptRunner runner = new ScriptRunner(conn, false, false);
            // 设置字符集
            Resources.setCharset(Charset.forName("utf-8"));
            // 加载sql脚本并执行
            runner.runScript(Resources.getResourceAsReader("table_derby.sql"));

            //执行清理
            stmt = conn.createStatement();
            stmt.execute("delete from ARTICLE");

            //执行插入
            stmt.execute("insert into ARTICLE (id,title,content,author,create_date) values('10001','山海关','一二三四五六七八九','白玉','20061225')");
            stmt.execute("insert into ARTICLE (id,title,content,author,create_date) values('10002','随机数据','abcdefdfdfdfdfdf','黑箱','20160522')");
            stmt.execute("insert into ARTICLE (id,title,content,author,create_date) values('10003','测试数据','我爱我家','Mr.R','20061125')");

            conn.commit();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public void testDB() throws Exception {
        assertNotNull(fullText);

        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from ARTICLE");

            // 创建索引
            fullText.createIndex("db", rs);

            Pager<Document> page = fullText.search("数据");
            assertEquals(2, page.getTotalCount());

            Document doc = page.getRecords().get(0);
            assertEquals("10002", doc.getId().getValue());
            assertEquals("db", doc.getType().getValue());
            assertEquals("随机数据", doc.getTitle().getValue());
            assertEquals("abcdefdfdfdfdfdf黑箱", doc.getAbstract().getValue());  //sql_abstract.page定义摘要内容是content和author的拼接
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
