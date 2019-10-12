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


import com.xquant.database.config.table.ForeignReference;
import com.xquant.database.config.table.Index;
import com.xquant.database.config.table.Table;
import com.xquant.database.config.table.TableField;
import com.xquant.database.config.tablespace.TableSpace;
import com.xquant.database.table.TableSqlProcessor;
import com.xquant.database.util.DataBaseUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wangwy on 2017/2/25.
 */
public class InformixSqlProcessorImpl extends SqlProcessorImpl {
    private static TableSqlProcessor tableSqlProcessor = new InformixSqlProcessorImpl();
    protected String currentDbSchema = null;
    private Logger logger = LoggerFactory
            .getLogger(OracleSqlProcessorImpl.class);

    public static TableSqlProcessor getTableSqlProcessor() {
        tableSqlProcessor.setTableProcessor(TableProcessorImpl.getTableProcessor());
        return tableSqlProcessor;
    }

    protected String getDatabaseType() {
        return "informix";
    }

    protected String createAlterTypeSql(String tableName, String fieldName,
                                        String tableDataType) {
        return String.format("ALTER TABLE %s MODIFY %s %s", tableName,
                delimiter(fieldName), tableDataType);
    }

    protected void appendComment(String comment, StringBuffer ddlBuffer) {
    }


    /**
     * 注释在foot上
     * 不需要在字段中体现变化
     *
     * @param standardComment
     * @param remarks
     * @return
     */
    protected boolean checkCommentSame(String standardComment, String remarks) {
        return true;
    }


    protected String getSchema(String schema, Connection connection) throws SQLException {
        if (!StringUtils.isBlank(schema)) {
            return schema;
        }
        if (currentDbSchema == null) {
            currentDbSchema = connection.getMetaData().getUserName();
        }
        return currentDbSchema;
    }

    protected void getChangedFooterComment(Connection connection, Table table, List<String> list) throws SQLException {
        //do nothing
    }

    protected String getDropForeignSql(String dropConstraint, Table table) {
        return String.format(
                "ALTER TABLE %s DROP CONSTRAINT %s",
                getTableName(table), delimiter(dropConstraint));
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


    protected void appendIndexReverse(StringBuffer ddlBuffer, Index index) {
        if (index.getReverse() != null
                && index.getReverse().booleanValue() == true) {
            ddlBuffer.append(" REVERSE");
        }
    }

    /**
     * 在footer增加comment
     * 实现为空
     *
     * @param table
     * @param list
     */
    protected void appendFooterComment(Table table, List<String> list) {
    }

    protected void appendComment(String comment, StringBuffer ddlBuffer, List<String> list) {
        //do nothing
    }

    protected void appendPrimarySql(StringBuffer ddlBuffer, Table table, List<String> list) {
        StringBuffer keyBuffer = getKeysBuffer(table);
        //约束名约定为pk_加表名
        if (keyBuffer.length() == 0) {
            return;
        }
        ddlBuffer.append(String.format(",PRIMARY KEY (%s) CONSTRAINT %s", keyBuffer, delimiter("pk_" + table.getNameWithOutSchema())));
    }

    protected String getOneFkSql(Table table, Table foreignTable, ForeignReference foreignReference) {
        return String.format("ALTER TABLE %s ADD CONSTRAINT (FOREIGN KEY (%s) REFERENCES %s(%s) CONSTRAINT %s)",
                getTableName(table),
                delimiter(getFieldStdFieldName(
                        foreignReference.getForeignField(),
                        table)),
                delimiter(foreignTable.getNameWithOutSchema()),
                delimiter(getFieldStdFieldName(
                        foreignReference.getReferenceField(),
                        foreignTable)),
                delimiter(foreignReference.getName()));
    }

    protected String getDropIndexBaseSql(String dropIndexName, String nameWithOutSchema) {
        return String.format("DROP INDEX %s", delimiter(dropIndexName));
    }

    /**
     * informix不会删除索引
     * 因为有系统索引(主键、外键、clob都会有)存在，无法辨别
     * @param table
     * @param dbIndexName
     * @param dbIndexMaps
     * @return
     */
    protected boolean notNeedDropIndex(List<Index> preAddIndexes, Table table, String dbIndexName, Map<String,
            Map<String, String>> dbIndexMaps, Connection connection) {
        Iterator<Index> indexIterator = preAddIndexes.iterator();
        while (indexIterator.hasNext()) {
            Index index = indexIterator.next();
            if (index.getName().equalsIgnoreCase(dbIndexName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 删除有关联的索引
     *informix不做处理
     * @param table
     * @param preDropIndexes
     */
    protected void dealConstraintIndex(Table table, List<String> preDropIndexes,
                                       Map<String, Map<String, String>> dbIndexes) {

    }


    /**
     *
     * @param dropFields
     * @param table
     * @return
     */
    protected List<String> dealDropFields(List<String> dropFields, Table table) {
        List<String> droplist = new ArrayList<String>();
        for (String column : dropFields) {
            StringBuffer ddlBuffer = new StringBuffer();
            ddlBuffer.append(String.format("ALTER TABLE %s DROP(%s)",
                    getTableName(table), delimiter(column)));
            droplist.add(ddlBuffer.toString());

        }
        return droplist;
    }

    /**
     *
     * @param ddlBuffer
     * @param table
     */
    protected void appendTableSpace(StringBuffer ddlBuffer, Table table) {
        if (!StringUtils.isEmpty(table.getTableSpace())) {
            TableSpace tableSpace = DataBaseUtil.getTableSpace(getClass().getClassLoader(), table.getTableSpace());
            ddlBuffer.append(" IN ").append(delimiter(tableSpace.getName()));
        }
    }
}
