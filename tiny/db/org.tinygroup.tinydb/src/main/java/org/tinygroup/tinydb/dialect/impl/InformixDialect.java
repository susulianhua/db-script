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

import org.tinygroup.database.util.DataBaseUtil;

/**
 * The Class InformixDialect.
 */
public class InformixDialect extends AbstractSequenceDialect {

    /**
     * Instantiates a new informix dialect.
     */
    public InformixDialect() {
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
        StringBuffer pagingSelect = new StringBuffer();
        pagingSelect.append("select skip ");
        pagingSelect.append(offset - 1);
        pagingSelect.append(" first ").append(limit);
        pagingSelect.append(" * from (").append(sql);
        pagingSelect.append(" )");
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
     */
    public String getCurrentDate() {
        return "SELECT current FROM sysmaster:sysshmvals";
    }

    @Deprecated
    public String buildSqlFuction(String sql) {
        return buildSqlFunction(sql);
    }

    public String buildSqlFunction(String sql) {
        return functionProcessor.getFunctionSql(sql, DataBaseUtil.DB_TYPE_INFORMIX);
    }

    protected String getSequenceQuery() {
        return "select " + getSelectSequenceNextValString(getIncrementerName()) + " from systables where tabid=1";
    }

    private String getSelectSequenceNextValString(String sequenceName) {
        return sequenceName + ".nextval";
    }


}
