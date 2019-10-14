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
package org.tinygroup.database.table.impl;

import org.tinygroup.database.config.table.Index;
import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.config.table.TableField;
import org.tinygroup.database.pojo.ColumnContext;
import org.tinygroup.database.table.TableSqlProcessor;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.metadata.config.stdfield.StandardField;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by wangwy on 2017/2/25.
 */
public class SybaseSqlProcessorImpl extends SqlProcessorImpl {
    private static TableSqlProcessor tableSqlProcessor = new SybaseSqlProcessorImpl();

    private static Logger logger = LoggerFactory
            .getLogger(SybaseSqlProcessorImpl.class);

    public static TableSqlProcessor getTableSqlProcessor() {
        tableSqlProcessor.setTableProcessor(TableProcessorImpl.getTableProcessor());
        return tableSqlProcessor;
    }

    protected String getDatabaseType() {
        return "sybase";
    }

    protected String appendIncrease() {
        return " identity ";
    }


    protected String createAlterTypeSql(String tableName, String fieldName,
                                        String tableDataType) {
        return String.format("ALTER TABLE %s MODIFY %s %s", tableName,
                delimiter(fieldName), tableDataType);
    }

    protected String getIndexName(Index index, Table table) {
        return index.getName();
    }

    /**
     * 在footer增加comment
     * mysql 实现为空
     *
     * @param table
     * @param list
     */
    protected void appendFooterComment(Table table, List<String> list) {
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

    protected String delimiter(String name) {
        return name;
    }

    protected void appendTableSpace(StringBuffer ddlBuffer, Table table) {
        //mysql没有表空间
    }

    protected void appendComment(String comment, StringBuffer ddlBuffer, List<String> list) {
        //do nothing
    }

    /**
     * 与其他数据库顺序不一样,identity必须在not null之前
     *
     * @param ddlBuffer
     * @param field
     * @param standardField
     */
    protected void appendTypeAndDefault(StringBuffer ddlBuffer, TableField field, StandardField standardField) {
        String fieldDefaultValue = getDefaultValue(field, standardField);
        // 非自增的字段设置字段默认值
        if (!field.isAutoIncrease()) {
            appendDefaultValue(fieldDefaultValue, ddlBuffer);
        }

        // 处理自增
        if (field.isAutoIncrease() && field.getPrimary()) {// 如果是自增而且是主键
            ddlBuffer.append(appendIncrease());
        }

        Boolean notNull = field.getNotNull();
        if (notNull != null && notNull.booleanValue()) {
            ddlBuffer.append(" NOT NULL");
        } else {
            ddlBuffer.append(" NULL");
        }
    }

    /**
     * @param existUpdateList
     * @param field
     * @param standardField
     * @param columnContext
     */
    protected void appendColumnUpdate(List<String> existUpdateList,
                                      TableField field, StandardField standardField,
                                      ColumnContext columnContext) {
        //类型或not不一致 就被修改
        if (!checkTypeSame(columnContext.getDbColumnType(), columnContext.getTableDataType(), columnContext.getDbDataType())
                || columnContext.isDbNullAble() == columnContext.isFieldNotNull()) {
            StringBuffer alterTypeBuffer = new StringBuffer();
            // 如果数据库中字段允许为空，但table中不允许为空
            alterTypeBuffer.append(createAlterTypeSql(columnContext.getTableName(),
                    columnContext.getStandardFieldName(), columnContext.getTableDataType()));

            dealNotNullSql(alterTypeBuffer, field, columnContext.isDbNullAble());

            existUpdateList.add(alterTypeBuffer.toString());
        }

        if (!checkDefSame(columnContext.getStandardDefault(), columnContext.getDbColumnDef())) {
            // 非自增的字段设置字段默认值
            if (!field.isAutoIncrease()) {
                String fieldDefaultValue = getDefaultValue(field, standardField);
                StringBuffer alterDefaultBuffer = new StringBuffer();
                dealDefaultValueUpdate(alterDefaultBuffer, fieldDefaultValue, columnContext.getDbColumnDef());
                if (alterDefaultBuffer.length() > 0) {
                    existUpdateList.add(String.format("ALTER TABLE %s REPLACE %s %s",
                            columnContext.getTableName(), delimiter(columnContext.getStandardFieldName()),
                            alterDefaultBuffer));
                }
            }
        }
    }

    protected void dealNotNullSql(StringBuffer alterTypeBuffer, TableField field, boolean dbNullAble) {
        if (field.getNotNull()) {//类型有变化,且not null
            alterTypeBuffer.append(" NOT NULL");
        } else {//类型有变化,且null
            alterTypeBuffer.append(" NULL");
        }
    }

    /**
     * 检查数据类型是否相同
     *
     * @param dbColumnType
     * @param tableDataType
     * @return
     */
    protected boolean checkTypeSame(String dbColumnType, String tableDataType, String dbDataType) {
        int index = dbColumnType.indexOf("(");
        if (index > 0) {
            String dbBaseType = dbColumnType.substring(0, index);
            if (dbBaseType.equalsIgnoreCase("int")
                    && ("int".equalsIgnoreCase(tableDataType) || "integer".equalsIgnoreCase(tableDataType))) {
                return true;
            }
        }
        return super.checkTypeSame(dbColumnType, tableDataType, dbDataType);
    }

    /**
     * @param dropIndexName
     * @param nameWithOutSchema
     * @return
     */
    protected String getDropIndexBaseSql(String dropIndexName, String nameWithOutSchema) {
        return String.format("DROP INDEX %s.%s", delimiter(nameWithOutSchema), delimiter(dropIndexName));
    }

    /**
     * @param connection
     * @param table
     * @return
     * @throws SQLException
     */
    protected ResultSet getDbForeignRsSql(Connection connection, Table table) throws SQLException {
        try {
            return connection.getMetaData()
                    .getImportedKeys(connection.getCatalog(), null, dealTableName(table.getNameWithOutSchema()));
        } catch (SQLException e) {
            logger.debugMessage("该表没有外键", e);
            return null;
        }
    }
}
