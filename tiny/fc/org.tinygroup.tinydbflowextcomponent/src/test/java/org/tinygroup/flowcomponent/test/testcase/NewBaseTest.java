package org.tinygroup.flowcomponent.test.testcase;

import org.tinygroup.commons.tools.Resources;
import org.tinygroup.tinytestutil.script.ScriptRunner;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class NewBaseTest extends AbstractFlowComponent {
    protected static final String DB_DRIVER = "org.h2.Driver";
    protected static final String URL_BASE = "jdbc:h2:./dbfile/%s;MODE=MYSQL;DB_CLOSE_ON_EXIT=FALSE";
    private static Connection conn = null;
    private static ScriptRunner dbrunner = null;

    static {
        //初始化数据库环境 表格创建

        Resources.setCharset(Charset.forName("utf-8"));
        try {
            initEmbeddedDb("testdb", "h2/table_test.sql");
            Statement statement = conn.createStatement();
            statement.execute("delete from tsys_user");
            statement.execute("delete from sequence");
            statement.execute("INSERT INTO tsys_user (user_id, branch_code, dep_code, user_name, user_pwd) VALUES ('u001', 'b002', 'g001', 'b001', 'e10adc3949ba59abbe56e057f20f883e')");
            statement.execute("INSERT INTO tsys_user (user_id, branch_code, dep_code, user_name, user_pwd) VALUES ('u003', 'b003', 'd001', 'b003', 'a906449d5769fa7361d7ecc6aa3f6d28')");
            statement.execute("INSERT INTO sequence (name, value, min_value, max_value, step, gmt_create, gmt_modified) VALUES ('user', '0', '0', '100000', '10', NULL, '2016-08-24')");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    //do nothing
                }
            }
        }

    }

    private static void initEmbeddedDb(String schemaName, String resourceFileName) throws Exception {
        Class.forName(DB_DRIVER);
        String url = String.format(URL_BASE, schemaName);
        conn = DriverManager.getConnection(url, "sa", "123456");
        dbrunner = new ScriptRunner(conn, false, false);
        dbrunner.runScript(Resources.getResourceAsReader(resourceFileName));
    }

}
