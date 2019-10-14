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
package org.tinygroup.tinydb.convert.impl;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.tinydb.config.ColumnConfiguration;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.exception.TinyDbException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * 通过数据库databsemeta元数据来获取表配置信息
 *
 * @author renhui
 */
public class MetadataTableConfigLoad extends AbstractTableConfigLoad {

    private static final String[] TABLE_TYPES = new String[]{"TABLE", "VIEW"};// 只查询TABLE和VIEW类型的表
    private static final String NULLABLE = "NULLABLE";
    private static final String TYPE_NAME = "TYPE_NAME";
    private static final String COLUMN_SIZE = "COLUMN_SIZE";
    private static final String DECIMAL_DIGITS = "DECIMAL_DIGITS";
    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final String PK_NAME = "COLUMN_NAME";
    private static final String DATA_TYPE = "DATA_TYPE";
    private static final String TABLE_NAME = "TABLE_NAME";
    /**
     * 不以'_'开头，且不以'_'或者'_数字'结尾的表名
     */
    private static final Pattern pattern = Pattern
            .compile("^(?!_)(?!.*?(_[0-9]*)$)[a-zA-Z]+(_?[a-zA-Z0-9])+$");
    private String schema;
    private String tableNamePattern;

    private static TableConfiguration getTableConfiguration(String tableName,
                                                            String tableNamePatternStr, String schemaPattern,
                                                            String columnNamePattern, DatabaseMetaData metaData)
            throws SQLException, TinyDbException {
        ResultSet colRet = metaData.getColumns(null, schemaPattern,
                tableNamePatternStr, columnNamePattern);
        ResultSet primaryKeySet = null;
        TableConfiguration table = new TableConfiguration();
        try {
            boolean flag = false;
            while (colRet.next()) {
                flag = true;
                ColumnConfiguration column = new ColumnConfiguration();
                column.setColumnName(colRet.getString(COLUMN_NAME)
                        .toUpperCase());
                column.setColumnSize(colRet.getString(COLUMN_SIZE));
                column.setDecimalDigits(colRet.getString(DECIMAL_DIGITS));
                column.setAllowNull(colRet.getString(NULLABLE));
                column.setTypeName(colRet.getString(TYPE_NAME));
                column.setDataType(colRet.getInt(DATA_TYPE));
                table.getColumns().add(column);
            }
            if (!flag) {// 说明没有colRet中没有查到列信息
                return null;
            }
            primaryKeySet = metaData.getPrimaryKeys(null, schemaPattern,
                    tableNamePatternStr);
            if (primaryKeySet != null && primaryKeySet.next()) {
                table.setPrimaryKey(primaryKeySet.getString(PK_NAME));
            }
            table.setName(tableName);
            return table;
        } finally {
            if (colRet != null) {
                colRet.close();
            }
            if (primaryKeySet != null) {
                primaryKeySet.close();
            }
        }

    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTableNamePattern() {
        return tableNamePattern;
    }

    public void setTableNamePattern(String tableNamePattern) {
        this.tableNamePattern = tableNamePattern;
    }

    protected void realLoadTable() throws TinyDbException {
        DataSource dataSource = configuration.getUseDataSource();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            logger.logMessage(LogLevel.INFO, "开始扫描schema：{0}", schema);
            initSchemaConfiguration(connection.getMetaData());
            logger.logMessage(LogLevel.INFO, "扫描schema结束：{0}", schema);
        } catch (SQLException e) {
            throw new TinyDbException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new TinyDbException(e);
                }
            }
        }

    }

    private void initSchemaConfiguration(DatabaseMetaData metaData) throws SQLException, TinyDbException {
        ResultSet tables = metaData.getTables("", schema.toUpperCase(),
                tableNamePattern, TABLE_TYPES);
        try {
            while (tables.next()) {
                String tableName = tables.getString(TABLE_NAME);
                if (pattern.matcher(tableName).matches()) {
                    initTableConfiguration(tableName, schema, metaData);

                } else {
                    logger.logMessage(LogLevel.ERROR, "表名：{0}不符合命名规范将被忽略",
                            tableName);
                }
            }
        } finally {
            if (tables != null) {
                tables.close();
            }
        }
    }

    private void initTableConfiguration(String tableName, String schema,
                                        DatabaseMetaData metaData) throws SQLException, TinyDbException {
        logger.logMessage(LogLevel.INFO, "开始获取表格:{0}信息", tableName);
        if (existsTable(tableName, schema)) {
            logger.logMessage(LogLevel.WARN, "表格:{0}已存在，无需重新获取", tableName);
            return;
        }
        // 不对表名和schame作处理获取表信息
        TableConfiguration table = getTableConfiguration(tableName, tableName,
                schema, "%", metaData);
        if (table == null) {
            // 小写表名读取信息 with schema
            table = getTableConfiguration(tableName, tableName.toLowerCase(),
                    schema.toLowerCase(), "%", metaData);
        }
        if (table == null) {
            // 大写表名读取信息 with schema
            table = getTableConfiguration(tableName, tableName.toUpperCase(),
                    schema.toUpperCase(), "%", metaData);
        }
        if (table == null) {
            // 大写表名读取信息
            table = getTableConfiguration(tableName, tableName.toUpperCase(),
                    "%", "%", metaData);
        }
        if (table == null) {
            // 小写写表名读取信息
            table = getTableConfiguration(tableName, tableName.toLowerCase(),
                    "%", "%", metaData);
        }
        if (table != null) {
            table.setSchema(schema);
            addTableConfiguration(table);
            logger.logMessage(LogLevel.INFO, "获取表格:{0}信息完成", tableName);
        } else {
            logger.logMessage(LogLevel.ERROR, "未能获取表格:{0}信息", tableName);
        }

    }


}
