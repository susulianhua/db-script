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
package org.tinygroup.database.view.impl;

import org.tinygroup.database.config.view.View;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InformixViewSqlProcessorImpl extends ViewSqlProcessorImpl {

    protected String getDatabaseType() {
        return "informix";
    }


    public boolean checkViewExists(View view, Connection conn) throws SQLException {
        String schema = conn.getMetaData().getUserName();
        ResultSet rs = null;
        try {
            rs = conn.getMetaData().getTables(conn.getCatalog(), schema, view.getNameWithOutSchema(), new String[]{"VIEW"});
            if (rs.next()) {
                return true;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return false;
    }

    public String getDropSql(View view) {
        return String.format("DROP VIEW %s ", view.getNameWithOutSchema());
    }


}
