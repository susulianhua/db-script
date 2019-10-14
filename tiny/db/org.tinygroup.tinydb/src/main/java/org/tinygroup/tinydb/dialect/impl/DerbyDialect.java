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
public class DerbyDialect extends AbstractColumnDialcet {

    /**
     * The default for dummy name
     */
    private static final String DEFAULT_DUMMY_NAME = "dummy";

    /**
     * The name of the dummy column used for inserts
     */
    private String dummyName = DEFAULT_DUMMY_NAME;

    /**
     * The current cache of values
     */
    private int[] valueCache;

    /**
     * The next id to serve from the value cache
     */
    private int nextValueIndex = -1;

    /**
     * Instantiates a new my sql dialect.
     */
    public DerbyDialect() {
    }

    /**
     * getLimitString.
     *
     * @param sql    String
     * @param offset int
     * @param limit  int
     * @return String
     */
    public String getLimitString(String sql, int offset, int limit) {
        return getLimitStringVersion106(sql, offset, limit);
    }

    private String getLimitStringVersion106(String sql, int offset, int limit) {
        int start = offset;
        if (offset < 0) {
            start = 0;
        }
        if (start > 0) {
            start = start - 1;
        }
        StringBuffer pagingSelect = new StringBuffer();
        pagingSelect.append(sql);
        pagingSelect.append(" OFFSET ").append(start).append(" ROWS ")
                .append(" FETCH NEXT ").append(limit).append("  ROWS ONLY  ");
        return pagingSelect.toString();
    }

    /**
     * supportsLimit.
     *
     * @return boolean
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
        if (this.nextValueIndex < 0 || this.nextValueIndex >= getCacheSize()) {
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
                this.valueCache = new int[getCacheSize()];
                this.nextValueIndex = 0;
                for (int i = 0; i < getCacheSize(); i++) {
                    stmt.executeUpdate("insert into " + getIncrementerName() + " (" + getDummyName() + ") values(null)");
                    ResultSet rs = stmt.executeQuery("select IDENTITY_VAL_LOCAL() from " + getIncrementerName());
                    try {
                        if (!rs.next()) {
                            throw new DataAccessResourceFailureException("IDENTITY_VAL_LOCAL() failed after executing an update");
                        }
                        this.valueCache[i] = rs.getInt(1);
                    } finally {
                        JdbcUtils.closeResultSet(rs);
                    }
                }
                long maxValue = this.valueCache[(this.valueCache.length - 1)];
                stmt.executeUpdate("delete from " + getIncrementerName() + " where " + getColumnName() + " < " + maxValue);
            } catch (SQLException ex) {
                throw new DataAccessResourceFailureException("Could not obtain IDENTITY value", ex);
            } finally {
                JdbcUtils.closeStatement(stmt);
                DataSourceUtils.releaseConnection(con, getDataSource());
            }
        }
        return this.valueCache[this.nextValueIndex++];

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
        return functionProcessor.getFunctionSql(sql, DataBaseUtil.DB_TYPE_DERBY);
    }

    /**
     * Return the name of the dummy column.
     */
    public String getDummyName() {
        return this.dummyName;
    }

    /**
     * Set the name of the dummy column.
     */
    public void setDummyName(String dummyName) {
        this.dummyName = dummyName;
    }

}
