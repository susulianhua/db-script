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
package org.tinygroup.tinydb.dialect.impl;

import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.tinygroup.database.util.DataBaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The Class MySQLDialect.
 */
public class MySQLDialect extends AbstractColumnDialcet {

    /**
     * The SQL string for retrieving the new sequence value
     */
    private static final String VALUE_SQL = "select last_insert_id()";

    /**
     * The next id to serve
     */
    private int nextId = 0;

    /**
     * The max id to serve
     */
    private int maxId = 0;


    /**
     * Instantiates a new my sql dialect.
     */
    public MySQLDialect() {
    }

    /**
     * getLimitString.
     *
     * @param sql    String
     * @param offset int
     * @param limit  int
     * @return String
     * @todo Implement this snowrain.database.data.Dialect method
     */
    public String getLimitString(String sql, int offset, int limit) {
        StringBuffer pagingSelect = new StringBuffer();
        pagingSelect.append(sql);
        pagingSelect.append(" limit " + (offset - 1) + ", " + limit);
        return pagingSelect.toString();
    }

    /**
     * supportsLimit.
     *
     * @return boolean
     * @todo Implement this snowrain.database.data.Dialect method
     */
    public boolean supportsLimit() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     */
    public synchronized int getNextKey() {
        if (this.maxId == this.nextId) {
            /*
             * Need to use straight JDBC code because we need to make sure that the insert and select
             * are performed on the same connection (otherwise we can't be sure that last_insert_id()
             * returned the correct value)
             */
            Connection con = DataSourceUtils.getConnection(getDataSource());
            Statement stmt = null;
            try {
                stmt = con.createStatement();
                DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
                // Increment the sequence column...
                String columnName = getColumnName();
                stmt.executeUpdate("update " + getIncrementerName() + " set " + columnName +
                        " = last_insert_id(" + columnName + " + " + getCacheSize() + ")");
                // Retrieve the new max of the sequence column...
                ResultSet rs = stmt.executeQuery(VALUE_SQL);
                try {
                    if (!rs.next()) {
                        throw new DataAccessResourceFailureException("last_insert_id() failed after executing an update");
                    }
                    this.maxId = rs.getInt(1);
                } finally {
                    JdbcUtils.closeResultSet(rs);
                }
                this.nextId = this.maxId - getCacheSize() + 1;
            } catch (SQLException ex) {
                throw new DataAccessResourceFailureException("Could not obtain last_insert_id()", ex);
            } finally {
                JdbcUtils.closeStatement(stmt);
                DataSourceUtils.releaseConnection(con, getDataSource());
            }
        } else {
            this.nextId++;
        }
        return this.nextId;
    }


    /*
     * (non-Javadoc)
     *
     */
    public String getCurrentDate() {
        return "select now()";
    }

    @Deprecated
    public String buildSqlFuction(String sql) {
        return buildSqlFunction(sql);
    }

    public String buildSqlFunction(String sql) {
        return functionProcessor.getFunctionSql(sql, DataBaseUtil.DB_TYPE_MYSQL);
    }
}
