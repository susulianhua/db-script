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

import com.xquant.database.config.table.Index;
import com.xquant.database.config.table.Table;
import com.xquant.database.config.table.TableField;
import com.xquant.database.table.TableSqlProcessor;
import com.xquant.database.util.SqlUtil;
import com.xquant.metadata.config.stdfield.StandardField;
import com.xquant.metadata.util.MetadataUtil;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;


public class SqlserverSqlProcessorImpl extends SqlProcessorImpl {

    private static TableSqlProcessor tableSqlProcessor = new SqlserverSqlProcessorImpl();

    public static TableSqlProcessor getTableSqlProcessor() {
        tableSqlProcessor.setTableProcessor(TableProcessorImpl.getTableProcessor());
        return tableSqlProcessor;
    }

    protected String getDatabaseType() {
        return "sqlserver";
    }

    protected String appendIncrease() {
        return " identity(1,1) ";
    }

    protected String createAlterTypeSql(String tableName, String fieldName,
                                        String tableDataType) {
        return String.format("ALTER TABLE %s ALTER COLUMN %s %s", tableName,
                delimiter(fieldName), tableDataType);
    }

    protected String getIndexName(Index index, Table table) {
        return index.getName();
    }

    protected void appendComment(String comment, StringBuffer ddlBuffer, List<String> list) {
        //do nothing
    }

    protected void appendFooter(StringBuffer ddlBuffer, Table table, List<String> list) {
        super.appendFooter(ddlBuffer, table, list);
        appendFooterComment(table, list);
    }

    /**
     * 在footer增加comment
     *
     * @param table
     * @param list
     */
    protected void appendFooterComment(Table table, List<String> list) {
        for (TableField field : table.getFieldList()) {
            StandardField standardField = MetadataUtil.getStandardField(field
                    .getStandardFieldId(), this.getClass().getClassLoader());
            String standardComment = getComment(standardField);
            appendFooterColumnComment(table, standardField, standardComment, list);
        }
    }

    private void appendFooterColumnComment(Table table, StandardField standardField, String standardComment, List list) {
        String schema = StringUtils.defaultIfBlank(table.getSchema(), "dbo");
        String comment = String.format("execute sp_addextendedproperty N'MS_Description', N'%s', N'SCHEMA', N'%s', N'TABLE', N'%s', N'COLUMN', N'%s'"
                , standardComment, schema, table.getNameWithOutSchema(), standardField.getName());
        list.add(comment);
    }

    protected boolean checkDefSame(String standardDefault, String columnDef) {
        //准备比较的字符串
        String preComparedStdDef = standardDefault;
        if (standardDefault != null) {
            //替换首尾特殊字符
            preComparedStdDef = SqlUtil.trim(standardDefault.trim(), "^['\"`]?|['\"`]?$");
        }
        //元数据中默认值null会返回'NULL'字符串
        if (columnDef != null) {
            if (columnDef.indexOf("NULL") >= 0 && "NULL".equalsIgnoreCase(columnDef.replaceAll("\n", ""))) {
                columnDef = null;
            } else {
                columnDef = SqlUtil.trim(columnDef.trim(), "^[(]?|[)]?$");
                //int类型默认值两层括号，varchar类型一层括号加单引号
                columnDef = SqlUtil.trim(columnDef.trim(), "^['(]?|[)']?$");
            }
        }
        return StringUtils.equals(preComparedStdDef, columnDef);
    }

    protected boolean checkCommentSame(String standardComment, String remarks) {
        return true;
    }

    protected void getChangedFooterComment(Connection connection, Table table, List<String> list) throws SQLException {
        //do nothing
    }

    protected boolean checkTypeSame(String dbColumnType, String tableDataType, String dbDataType) {
        String tbDataTypeLower = tableDataType.replaceAll(" ", "")
                .replaceAll(",0", "").toLowerCase();
        if (String.valueOf(Types.INTEGER).equals(dbDataType)) {
            return "integer".equals(tbDataTypeLower) || "int".equals(tbDataTypeLower);
        }
        //我们认为精度为0不需要做为比较对象
        return dbColumnType.replaceAll(",0", "").indexOf(tbDataTypeLower) != -1;
    }

    protected String getDropIndexBaseSql(String dropIndexName, String nameWithOutSchema) {
        return String.format("DROP INDEX [%s] ON [%s] WITH ( ONLINE = OFF )",
                delimiter(dropIndexName), delimiter(nameWithOutSchema));
    }

    protected String getSchema(String schema, Connection connection) throws SQLException {
        if (!StringUtils.isBlank(schema)) {
            return schema;
        }
        return "dbo";
    }
}
