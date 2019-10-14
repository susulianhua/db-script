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
package org.tinygroup.databasebuinstaller;

import org.tinygroup.exception.BaseRuntimeException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 执行增量sql的类
 *
 * @author wangwy
 */
public class ChangeSqlExecute {

    private DatabaseInstallerProcessor databaseInstallerProcessor;

    public void setDatabaseInstallerProcessor(
            DatabaseInstallerProcessor databaseInstallerProcessor) {
        this.databaseInstallerProcessor = databaseInstallerProcessor;
    }

    public void sqlExecute() {
        DataSource dataSource = DataSourceHolder.getDataSource();
        Connection con = null;
        try {
            List<String> toolChangeSqls = databaseInstallerProcessor.getChangeSqls();
            con = dataSource.getConnection();
            databaseInstallerProcessor.execute(toolChangeSqls, con);
        } catch (SQLException ex) {
            throw new BaseRuntimeException(ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                }
            }
        }

    }

}
