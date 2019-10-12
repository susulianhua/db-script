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
import com.xquant.database.exception.DatabaseRuntimeException;
import com.xquant.database.table.TableSqlProcessor;
import com.xquant.database.util.ColTypeGroupUtil;
import com.xquant.database.util.DataBaseUtil;

import com.xquant.metadata.config.stdfield.StandardField;
import com.xquant.metadata.util.ConfigUtil;
import com.xquant.metadata.util.MetadataUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OracleSqlProcessorImpl extends SqlProcessorImpl {

    protected static final String FORMAT_FUNCTION_STR = "to_date(%s,'yyyy-mm-dd hh24:mi:ss')";
    private static TableSqlProcessor tableSqlProcessor = new OracleSqlProcessorImpl();
    protected String currentDbSchema = null;
    private Logger logger = LoggerFactory
            .getLogger(OracleSqlProcessorImpl.class);
    private List<String> cacheSeqList = null;

    public static TableSqlProcessor getTableSqlProcessor() {
        tableSqlProcessor.setTableProcessor(TableProcessorImpl.getTableProcessor());
        return tableSqlProcessor;
    }

    protected String getDatabaseType() {
        return "oracle";
    }

    protected String getQueryForeignSql(Table table, String schema) {
        String sql = "Select a.constraint_name CONSTRAINT_NAME,"
                + "a.column_name  COLUMN_NAME,"
                + "b.table_name  REFERENCED_TABLE_NAME,"
                + "b.column_name REFERENCED_COLUMN_NAME"
                + " From (Select a.owner,a.constraint_name,"
                + "b.table_name,b.column_name,a.r_constraint_name"
                + " From all_constraints a, all_cons_columns b"
                + " Where a.constraint_type = 'R'"
                + " And a.constraint_name = b.constraint_name) a,"
                + "(Select Distinct a.r_constraint_name, b.table_name, b.column_name"
                + " From all_constraints a, all_cons_columns b"
                + " Where a.constraint_type = 'R'"
                + " And a.r_constraint_name = b.constraint_name) b"
                + " Where a.r_constraint_name = b.r_constraint_name"
                + " and a.table_name ='" + table.getNameWithOutSchema().toUpperCase() + "'";
        if (schema != null && schema.trim().length() > 0) {
            sql += " and a.owner='"
                    + schema.toUpperCase() + "'";
        }
        return sql;
    }

    protected String createAlterTypeSql(String tableName, String fieldName,
                                        String tableDataType) {
        return String.format("ALTER TABLE %s MODIFY %s %s", tableName,
                delimiter(fieldName), tableDataType);
    }

    protected void appendComment(String comment, StringBuffer ddlBuffer) {
    }

    protected void appendFooter(StringBuffer ddlBuffer, Table table, List<String> list) {
        super.appendFooter(ddlBuffer, table, list);
        appendFooterComment(table, list);
    }

    /**
     * 添加oracle的字段备注信息
     *
     * @param comment
     * @param ddlBuffer
     * @param list
     */
    protected void appendComment(String comment, StringBuffer ddlBuffer, List<String> list) {
        //do nothing
    }

    /**
     * oracle注释在foot上
     * 不需要在字段中体现变化
     *
     * @param standardComment
     * @param remarks
     * @return
     */
    protected boolean checkCommentSame(String standardComment, String remarks) {
        return true;
    }

    /**
     * @param dbColumnType
     * @param tableDataType
     * @return
     */
    protected boolean checkTypeSame(String dbColumnType, String tableDataType, String dbDataType) {
        String type = ColTypeGroupUtil.getSpecialType(tableDataType);
        if (type != null) {
            tableDataType = type;
        }
//        ColTypeGroupUtil.getSpecialType(tableDataType);
        String tbDataTypeLower = tableDataType.replaceAll(" ", "")
                .replaceAll(",0", "").toLowerCase();
        //我们认为精度为0不需要做为比较对象
        return dbColumnType.replaceAll(",0", "").indexOf(tbDataTypeLower) != -1;
    }

    protected String getSchema(String schema, Connection connection) throws SQLException {
        if (!StringUtils.isBlank(schema)) {
            return schema;
        }
        if (currentDbSchema == null) {
            currentDbSchema = connection.getMetaData().getUserName();
            /*Statement statement = connection.createStatement();
            ResultSet userrs = statement.executeQuery("select user from dual");
            if (userrs.next()) {
                currentDbSchema = userrs.getString(1);
                return currentDbSchema;
            }*/
        }
        return currentDbSchema;
    }

    protected List<String> getSeqTriggerSql(Table table, String packageName) {
        List<String> sqlList = new ArrayList<String>();
        for (TableField tableField : table.getFieldList()) {
            //非自增长和非主键排除
            if (!tableField.isAutoIncrease() || !tableField.getPrimary()) {
                continue;
            }

            StandardField standardField = MetadataUtil.getStandardField(
                    tableField.getStandardFieldId(), this.getClass().getClassLoader());
            sqlList.add(String.format("create sequence %s", getSeqName(table)).toUpperCase());
            if (ConfigUtil.isUseDbTrigger()) {
                String baseStr = "create or replace trigger %s before insert on %s for each row" +
                        " when (new.%s is null) begin select %s.nextval into :new.%s from dual; end;";
                String from = DataBaseUtil.fromSourceLocal.get();
                //从工具
                if (from != null && from.equals("tool")) {
                    baseStr += "\n/\n";
                }
                String triggerName = "TRI_" + table.getNameWithOutSchema();
                sqlList.add(String.format(baseStr, triggerName,
                        table.getNameWithOutSchema(), standardField.getName(), getSeqName(table)
                        , standardField.getName()).toUpperCase());
            }
        }
        return sqlList;
    }

    protected void getChangedFooterComment(Connection connection, Table table, List<String> list) throws SQLException {
        //do nothing
    }

    protected void getSeqTriggerUpdate(Connection connection, Table table, List<String> list) throws SQLException {
        String querySchemaName = getSchema(table.getSchema(), connection);
        boolean isSeqExits = getAllSequence(connection, querySchemaName).contains(getSeqName(table));
        //已经存在则不需要创建序列和触发器
        if (isSeqExits) {
            return;
        }
        list.addAll(getSeqTriggerSql(table, null));
    }

    private String getSeqName(Table table) {
        return "SEQ_" + table.getNameWithOutSchema().toUpperCase();
    }

    protected String getDropForeignSql(String dropConstraint, Table table) {
        return String.format(
                "ALTER TABLE %s DROP CONSTRAINT %s",
                getTableName(table), delimiter(dropConstraint));
    }

    protected String getDropIndexBaseSql(String dropIndexName, String tableName) {
        return String.format("DROP INDEX %s", delimiter(dropIndexName));
    }

    private String getSeqSql(String schema) {
        String queryStr = "select SEQUENCE_NAME from all_sequences ";
        if (schema != null && schema.trim().length() > 0) {
            queryStr += " where SEQUENCE_OWNER='"
                    + schema.toUpperCase() + "'";
        }
        return queryStr;
    }

    private List<String> getAllSequence(Connection connection, String schema) throws SQLException {
        Statement statement = null;
        ResultSet rs = null;
        List<String> seqlist = new ArrayList<String>();
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(getSeqSql(schema));
            if (cacheSeqList != null) {
                return cacheSeqList;
            }
            cacheSeqList = new ArrayList<String>();
            while (rs.next()) {
                String seq = rs.getString(1);
                if (seq != null) {
                    seq = seq.toUpperCase();
                }
                seqlist.add(seq);
            }
            cacheSeqList.addAll(seqlist);
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return cacheSeqList;
    }

    public List<String> getClearTableSql(Table table, Connection connection) throws SQLException {
        List<String> clearSqls = new ArrayList<String>();
        clearSeq(clearSqls, table, connection);
        return clearSqls;
    }

    private void clearSeq(List<String> clearsqls, Table table, Connection connection) throws SQLException {
        boolean isSeqExits = getAllSequence(connection, table.getSchema()).contains(getSeqName(table));
        if (isSeqExits) {
            clearsqls.add(String.format("DROP SEQUENCE %s", getSeqName(table)));
            logger.info( "表格[{0}]在数据库中不存在,将清理残留的序列[{1}]", table.getName()
                    , getSeqName(table));
        }
    }

    protected void dealDefaultValueUpdate(StringBuffer alterTypeBuffer, String fieldDefaultValue, String columnDef) {
        //原来非空现在为null
        if (columnDef != null && fieldDefaultValue == null) {
            alterTypeBuffer.append(" DEFAULT NULL");
        } else {
            appendDefaultValue(fieldDefaultValue, alterTypeBuffer);
        }
    }

    protected void dealNotNullSql(StringBuffer alterTypeBuffer, TableField field, boolean dbNullAble) {
        if (field.getNotNull()
                && dbNullAble) {//类型有变化,且not null
            alterTypeBuffer.append(" NOT NULL");
        } else if (!field.getNotNull()
                && !dbNullAble) {//类型有变化,且null
            alterTypeBuffer.append(" NULL");
        }
    }

    protected boolean checkIndexBaseSame(Index tableIndex, Map<String, String> dbIndexMap,
                                         Connection con) {
        boolean isDbReverse = isIndexReverse(tableIndex, con);
        boolean isIndexReverse = false;
        if (tableIndex.getReverse() != null) {
            isIndexReverse = tableIndex.getReverse();
        }
        return super.checkIndexBaseSame(tableIndex, dbIndexMap, con) && isDbReverse == isIndexReverse;
    }

    private boolean isIndexReverse(Index tableIndex, Connection con) {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = con.createStatement();
            rs = statement.executeQuery(String.format("select INDEX_TYPE from user_indexes where index_name='%s'",
                    tableIndex.getName().toUpperCase()));
            if (rs.next()) {
                String indexType = rs.getString("INDEX_TYPE");
                if ("NORMAL/REV".equalsIgnoreCase(indexType)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseRuntimeException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {

                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {

                }
            }
        }
        return false;
    }

    protected void appendIndexReverse(StringBuffer ddlBuffer, Index index) {
        if (index.getReverse() != null
                && index.getReverse().booleanValue() == true) {
            ddlBuffer.append(" REVERSE");
        }
    }

    protected String dealDateType(String value) {
        return String.format(FORMAT_FUNCTION_STR, value);
    }
}
