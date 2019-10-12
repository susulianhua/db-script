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
package com.xquant.database.table.impl;

import com.xquant.database.config.table.Table;
import com.xquant.database.config.table.TableField;
import com.xquant.database.table.TableSqlProcessor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;


public class DerbySqlProcessorImpl extends SqlProcessorImpl {

    private static TableSqlProcessor tableSqlProcessor = new DerbySqlProcessorImpl();

    public static TableSqlProcessor getTableSqlProcessor() {
        tableSqlProcessor.setTableProcessor(TableProcessorImpl.getTableProcessor());
        return tableSqlProcessor;
    }

    protected String getDatabaseType() {
        return "derby";
    }

    protected String appendIncrease() {
        return " GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1)";
    }

    protected String createAlterTypeSql(String tableName, String fieldName,
                                        String tableDataType) {
        return String.format("ALTER TABLE %s ALTER %s SET DATA TYPE %s", tableName, delimiter(fieldName), tableDataType);
    }

    protected void appendComment(String comment, StringBuffer ddlBuffer, List<String> list) {
        //do nothing
    }

    protected void dealDefaultValueUpdate(StringBuffer alterTypeBuffer, String fieldDefaultValue, String columnDef) {

    }

    protected void dealNotNullSql(StringBuffer alterTypeBuffer, TableField field, boolean dbNullAble) {

    }

    /**
     * footer检查comment变化
     *
     * @param connection
     * @param table
     * @param list
     * @throws SQLException
     */
    protected void getChangedFooterComment(Connection connection, Table table, List<String> list) throws SQLException {

    }

    protected boolean checkCommentSame(String standardComment, String remarks) {
        return true;
    }

    protected String getSchema(Table table, DatabaseMetaData metadata) throws SQLException {
        String schema = table.getSchema();
        if (schema == null || "".equals(schema)) {
            return null;
        }
        return schema;
    }

    protected String getDropIndexBaseSql(String dropIndexName, String tableName) {
        return String.format("DROP INDEX %s", delimiter(dropIndexName));
    }
}
