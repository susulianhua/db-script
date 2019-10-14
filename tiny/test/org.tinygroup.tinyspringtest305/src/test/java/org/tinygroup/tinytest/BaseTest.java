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
package org.tinygroup.tinytest;

import org.junit.BeforeClass;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.tinygroup.commons.tools.Resources;
import org.tinygroup.tinytestutil.script.ScriptRunner;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseTest extends AbstractTransactionalJUnit4SpringContextTests {

    @BeforeClass
    public static void setUp() throws Exception {
        Connection conn = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection("jdbc:derby:TESTDB;create=true", "opensource", "opensource");
            ScriptRunner debyrunner = new ScriptRunner(conn, false, false);
            Resources.setCharset(Charset.forName("utf-8"));
            debyrunner.runScript(Resources
                    .getResourceAsReader("table_derby.sql"));

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

    }

}
