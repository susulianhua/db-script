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

import com.xquant.database.config.table.*;
import com.xquant.database.config.tablespace.TableSpace;
import com.xquant.database.pojo.ColumnContext;
import com.xquant.database.sqlvalue.SqlValueProcessor;
import com.xquant.database.table.TableProcessor;
import com.xquant.database.table.TableSqlProcessor;
import com.xquant.database.util.ColTypeGroupUtil;
import com.xquant.database.util.DataBaseNameUtil;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.database.util.SqlUtil;
import com.xquant.metadata.config.PlaceholderValue;
import com.xquant.metadata.config.bizdatatype.BusinessType;
import com.xquant.metadata.config.stddatatype.StandardType;
import com.xquant.metadata.config.stdfield.StandardField;
import com.xquant.metadata.util.ConfigUtil;
import com.xquant.metadata.util.MetadataUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class SqlProcessorImpl implements TableSqlProcessor {

    private TableProcessor tableProcessor;

    private Map<String, Map<String, Map<String, String>>> allTableColsCache =
            new HashMap<String, Map<String, Map<String, String>>>();

    public TableProcessor getTableProcessor() {
        return tableProcessor;
    }

    public void setTableProcessor(TableProcessor tableProcessor) {
        this.tableProcessor = tableProcessor;
    }

    protected abstract String getDatabaseType();

    public List<String> getCreateSql(Table table, String packageName) {
        List<String> list = new ArrayList<String>();
        // 生成表格创建语句
        list.addAll(getTableCreateSql(table, packageName));

        //自增长序列和触发器
        list.addAll(getSeqTriggerSql(table, packageName));
        // 增加外键语句
        list.addAll(getForeignKeySqls(table, packageName));
        // 生成index
        list.addAll(getIndexCreateSql(table, packageName));
        return list;
    }

    /**
     * 创建seq和trigger语句
     *
     * @param table
     * @param packageName
     * @return
     */
    protected List<String> getSeqTriggerSql(Table table, String packageName) {
        return new ArrayList<String>();
    }

    protected void appendPrimarySql(StringBuffer ddlBuffer, Table table, List<String> list) {
        StringBuffer keyBuffer = getKeysBuffer(table);
        //约束名约定为pk_加表名
        if (keyBuffer.length() == 0) {
            return;
        }
        ddlBuffer.append(String.format(",\n\tCONSTRAINT %s PRIMARY KEY (%s)", delimiter("pk_" + table.getNameWithOutSchema()), keyBuffer));
    }

    protected StringBuffer getKeysBuffer(Table table) {
        List<TableField> fieldList = table.getFieldList();
        StringBuffer keyBuffer = new StringBuffer();
        boolean isFirst = true;
        for (int i = 0; i < fieldList.size(); i++) {
            TableField tableField = fieldList.get(i);
            Boolean primary = tableField.getPrimary();
            if (primary == null || !primary.booleanValue()) {
                continue;
            }
            if (!isFirst) {
                keyBuffer.append(",");
            } else {
                isFirst = false;
            }
            StandardField standardField = MetadataUtil.getStandardField(
                    tableField.getStandardFieldId(), this.getClass().getClassLoader());
            keyBuffer.append(delimiter(DataBaseUtil.getDataBaseName(standardField.getName())));
        }
        return keyBuffer;
    }

    /**
     * 获取外键,直接使用table的外键作为需要创建的外键(适用于全量sql)
     *
     * @param table
     * @param packageName
     * @return
     */
    public List<String> getForeignKeySqls(Table table, String packageName) {
        return getForeignKeySqls(table, packageName, table.getForeignReferences());
    }

    /**
     * 获取外键
     *
     * @param table
     * @param packageName
     * @param foreignReferences 需要更新的外键对象
     * @return
     */
    public List<String> getForeignKeySqls(Table table, String packageName, List<ForeignReference> foreignReferences) {
        List<String> foreignSqls = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(foreignReferences)) {
            for (ForeignReference foreignReference : foreignReferences) {
                Table foreignTable = tableProcessor
                        .getTableById(foreignReference.getMainTable());
                String sql = getOneFkSql(table, foreignTable, foreignReference);
                foreignSqls.add(sql);
            }
        }
        return foreignSqls;
    }

    protected String getOneFkSql(Table table, Table foreignTable, ForeignReference foreignReference) {
        return String.format("ALTER TABLE %s ADD CONSTRAINT %s FOREIGN KEY (%s) REFERENCES %s(%s)",
                getTableName(table),
                delimiter(foreignReference.getName()),
                delimiter(getFieldStdFieldName(
                        foreignReference.getForeignField(),
                        table)),
                delimiter(foreignTable.getNameWithOutSchema()),
                delimiter(getFieldStdFieldName(
                        foreignReference.getReferenceField(),
                        foreignTable)));
    }

    public List<String> getTableCreateSql(Table table, String packageName) {
        StringBuffer ddlBuffer = new StringBuffer();
        List<String> list = new ArrayList<String>();
        // 生成表格主体
        appendHeader(ddlBuffer, table, list);
        appendBody(ddlBuffer, table, list);
        appendFooter(ddlBuffer, table, list);
        list.add(0, ddlBuffer.toString());
        return list;
    }

    public List<String> getIndexCreateSql(Table table, String packageName) {
        return appendIndexes(table);
    }

    public List<String> getUpdateSql(Table table, String packageName,
                                     Connection connection) throws SQLException {
        List<String> list = new ArrayList<String>();
        DatabaseMetaData metadata = connection.getMetaData();
        String catalog = connection.getCatalog();
        getTableColumnUpdate(table, packageName, metadata, catalog, list);
        getOtherUpdate(table, packageName, connection, list);
        postSql(table);
        return list;
    }

    /**
     * sql获取完后
     */
    private void postSql(Table table) {
        //清除缓存
        allTableColsCache.remove(table.getName().toUpperCase());
    }

    protected void getOtherUpdate(Table table, String packageName,
                                  Connection connection, List<String> list) throws SQLException {
        getChangedFooterComment(connection, table, list);
        getSeqTriggerUpdate(connection, table, list);
        getForeignUpdate(table, packageName, connection, list);
        getIndexUpdate(connection, table, list);
    }

    protected void getSeqTriggerUpdate(Connection connection, Table table, List<String> list) throws SQLException {
        //no nothing
    }

    protected void getIndexUpdate(Connection connection, Table table, List<String> list) throws SQLException {
        List<Index> tableIndexList = table.getIndexList();
        if (tableIndexList == null) {
            return;
        }
        Map<String, Map<String, String>> dbIndexMaps =
                getDbIndexColumns(connection.getMetaData(), getSchema(table.getSchema(), connection), table);
        List<String> preDropIndexes = new ArrayList<String>();
        List<Index> preAddIndexes = mergeIndexes(tableIndexList, table);
        dealIndex(preAddIndexes, preDropIndexes, table, dbIndexMaps, connection);

        for (String dropIndexName : preDropIndexes) {
            list.add(getDropIndexBaseSql(dropIndexName, table.getNameWithOutSchema()));
        }

        for (Index index : preAddIndexes) {
            list.add(getIndex(index, table));
        }
    }

    protected String getDropIndexBaseSql(String dropIndexName, String nameWithOutSchema) {
        return String.format("ALTER TABLE %s DROP INDEX %s",
                delimiter(nameWithOutSchema), delimiter(dropIndexName));
    }

    protected String getSchema(String schema, Connection connection) throws SQLException {
        if (schema != null && schema.trim().length() == 0) {
            return null;
        }
        return schema;
    }

    /**
     * 将字段中的是否唯一和表格中的索引列表合并
     *
     * @param tableIndexList
     * @param table
     * @return
     */
    private List<Index> mergeIndexes(List<Index> tableIndexList, Table table) {
        List<Index> newIndexes = new ArrayList<Index>();
        List<String> indexNames = new ArrayList<String>();
        for (Index index : tableIndexList) {
            newIndexes.add(index);
            indexNames.add(index.getName());
        }

        for (TableField tableField : table.getFieldList()) {
            StandardField standardField = MetadataUtil.getStandardField(tableField.getStandardFieldId(),
                    this.getClass().getClassLoader());
            if (tableField.getPrimary() || !isUnique(tableField)) {
                continue;
            }
            //列表不包含才去新加到table index中
            String isUniqueIndexName = ("idx_" + table.getNameWithOutSchema() + "_" + standardField.getName()).toUpperCase();
            if (!indexNames.contains(isUniqueIndexName)) {
                Index index = new Index();
                index.setName(isUniqueIndexName);
                index.setUnique(true);
                List<IndexField> fields = new ArrayList<IndexField>();
                IndexField indexField = new IndexField();
                indexField.setField(tableField.getId());
                fields.add(indexField);
                index.setFields(fields);
                newIndexes.add(index);
            }

        }
        return newIndexes;
    }

    private void dealIndex(List<Index> preAddIndexes, List<String> preDropIndexes,
                           Table table, Map<String, Map<String, String>> dbIndexMaps, Connection con) {
        for (String dbIndexName : dbIndexMaps.keySet()) {
            if (notNeedDropIndex(preAddIndexes, table, dbIndexName, dbIndexMaps, con)) {
                continue;
            }
            preDropIndexes.add(dbIndexName);
        }
        dealIndexUpdate(preAddIndexes,preDropIndexes,table,dbIndexMaps);
        dealConstraintIndex(table, preDropIndexes, dbIndexMaps);
    }

    protected void dealIndexUpdate(List<Index> preAddIndexes, List<String> preDropIndexes, Table table, Map<String, Map<String, String>> dbIndexMaps) {

    }

    protected boolean notNeedDropIndex(List<Index> preAddIndexes, Table table, String dbIndexName, Map<String, Map<String, String>> dbIndexMaps,
                                       Connection connection) {
        return "PRIMARY".equalsIgnoreCase(dbIndexName);
    }


    /**
     * index基本信息是否一致
     *
     * @param tableIndex
     * @param dbIndexMap
     * @param con
     * @return
     */
    protected boolean checkIndexBaseSame(Index tableIndex, Map<String, String> dbIndexMap,
                                         Connection con) {
        return tableIndex.getUnique() != Boolean.valueOf(dbIndexMap.get("NON_UNIQUE"));
    }

    /**
     * 删除有关联的索引
     *
     * @param table
     * @param preDropIndexes
     */
    protected void dealConstraintIndex(Table table, List<String> preDropIndexes,
                                       Map<String, Map<String, String>> dbIndexes) {
        List<ForeignReference> foreignReferenceList = table.getForeignReferences();
        Iterator<String> preDropIndexesIter = preDropIndexes.iterator();
        //外键关联索引
        while (preDropIndexesIter.hasNext()) {
            String preDropIndexName = preDropIndexesIter.next();
            String preDropIndexUpName = preDropIndexName.toUpperCase();
            for (ForeignReference fr : foreignReferenceList) {
                String stdForeignField = getFieldStdFieldName(fr.getForeignField(), table);
                Map<String, String> indexCols = dbIndexes.get(preDropIndexUpName);
                if (indexCols.get("COLUMN_NAME").equalsIgnoreCase(stdForeignField)) {
                    //如果有外键对应索引,不删除
                    preDropIndexesIter.remove();
                    break;
                }
            }
            for (TableField tableField : table.getFieldList()) {
                StandardField standardField = MetadataUtil.getStandardField(
                        tableField.getStandardFieldId(), this.getClass().getClassLoader());
                Map<String, String> indexCols = dbIndexes.get(preDropIndexUpName);
                if (tableField.getPrimary() && indexCols.get("COLUMN_NAME").equalsIgnoreCase(standardField.getName())) {
                    preDropIndexesIter.remove();
                    break;
                }
            }
        }
    }

    protected void getChangedFooterComment(Connection connection, Table table, List<String> list) throws SQLException {
        defaultChangedFooterComment(connection, table, list);
    }

    protected void defaultChangedFooterComment(Connection connection, Table table, List<String> list) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();
        String catalog = connection.getCatalog();
        Map<String, Map<String, String>> dbColumns = getColumns(metadata,
                catalog, table);

        for (TableField field : table.getFieldList()) {
            StandardField standardField = MetadataUtil.getStandardField(
                    field.getStandardFieldId(), this.getClass().getClassLoader());
            String standardComment = getComment(standardField);
            if (standardField.getName() != null) {
                Map<String, String> attribute = dbColumns.get(standardField.getName().toLowerCase());
                if (attribute == null) {
                    attribute = dbColumns.get(standardField.getName().toUpperCase());
                }
                String remarks = (attribute == null) ? null : getDbColumnRemarks(attribute);
                //相等跳过
                if (StringUtils.equals(standardComment, remarks)) {
                    continue;
                }
            }
            appendFooterColumnComment(table, standardField, standardComment, list);
        }
    }

    protected List<Map<String, String>> getDbForeignList(Connection connection, Table table) throws SQLException {
        List<Map<String, String>> flist = new ArrayList<Map<String, String>>();
        ResultSet rs = null;
        try {
            rs = connection.getMetaData()
                    .getImportedKeys(connection.getCatalog(), getSchema(table.getSchema(), connection), dealTableName(table.getNameWithOutSchema()));
            boolean isExists = false;
            while (rs.next()) {
                isExists = true;
                addFkToFlist(rs, flist);
            }
            if (!isExists) {
                //用大写再查一遍
                rs = connection.getMetaData()
                        .getImportedKeys(connection.getCatalog(), getSchema(table.getSchema(), connection), dealTableName(table.getNameWithOutSchema().toUpperCase()));
                while (rs.next()) {
                    addFkToFlist(rs, flist);
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return flist;
    }

    private void addFkToFlist(ResultSet rs, List<Map<String, String>> flist) throws SQLException {
        //关联名称
        String constraintName = rs.getString("FK_NAME");
        String columnName = rs.getString("FKCOLUMN_NAME");
        String referenceTableName = rs
                .getString("PKTABLE_NAME");
        String referenceColumnName = rs
                .getString("PKCOLUMN_NAME");
        Map<String, String> map = new HashMap<String, String>();
        map.put("COLUMN_NAME", columnName);
        map.put("CONSTRAINT_NAME", constraintName);
        map.put("REFERENCED_TABLE_NAME", referenceTableName);
        map.put("REFERENCED_COLUMN_NAME", referenceColumnName);
        flist.add(map);
    }

    protected String dealTableName(String tableName) {
        return tableName;
    }

    /**
     * 更新外键
     *
     * @param table
     * @param packageName
     * @param connection
     * @param list
     * @throws SQLException
     */
    protected void getForeignUpdate(Table table, String packageName,
                                    Connection connection, List<String> list) throws SQLException {
        List<Map<String, String>> dbForeignList = getDbForeignList(connection, table);
        List<ForeignReference> foreignRefs = table.getForeignReferences();
        //待新增的外键
        List<ForeignReference> preparedForeignRefs = cloneReferences(foreignRefs);
        List<String> dropConstraints = new ArrayList<String>();
        for (Map<String, String> dbForeignMap : dbForeignList) {
            //关联名称
            int index = indexOfTableReference(preparedForeignRefs, table, dbForeignMap);
            //table中的外键在数据库中存在,则不需要更新,反之需要删除
            if (index >= 0) {
                preparedForeignRefs.remove(index);
            } else {
                dropConstraints.add(dbForeignMap.get("CONSTRAINT_NAME"));
            }
        }
        for (String dropConstraint : dropConstraints) {
            list.add(getDropForeignSql(dropConstraint, table));
        }
        list.addAll(getForeignKeySqls(table, packageName, preparedForeignRefs));
    }

    protected String getDropForeignSql(String dropConstraint, Table table) {
        return String.format(
                "ALTER TABLE %s DROP FOREIGN KEY %s",
                getTableName(table), delimiter(dropConstraint));
    }

    /**
     * 获取查询语句的schema
     *
     * @param schema
     * @param catalog
     * @return
     */
    protected String getQuerySchemaName(String schema, String catalog) {
        return (schema != null && schema.trim().length() != 0) ? schema : catalog;
    }


    private int indexOfTableReference(List<ForeignReference> foreignRefs, Table table, Map<String, String> dbForeignMap) {
        for (int i = 0; i < foreignRefs.size(); i++) {
            ForeignReference fr = foreignRefs.get(i);
            Table foreignTable = tableProcessor
                    .getTableById(fr.getMainTable());
            //被关联的标准字段名
            String stdRefFieldName = getFieldStdFieldName(fr.getReferenceField(), foreignTable);
            //外键的标准字段名
            String stdForeignField = getFieldStdFieldName(fr.getForeignField(), table);
            String constraintName = dbForeignMap.get("CONSTRAINT_NAME");
            String columnName = dbForeignMap.get("COLUMN_NAME");
            String referenceTableName = dbForeignMap.get("REFERENCED_TABLE_NAME");
            String referenceColumnName = dbForeignMap.get("REFERENCED_COLUMN_NAME");
            if (stdRefFieldName.equalsIgnoreCase(referenceColumnName)
                    && fr.getName().equalsIgnoreCase(constraintName)
                    && foreignTable.getNameWithOutSchema().equalsIgnoreCase(referenceTableName)
                    && stdForeignField.equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    protected String getQueryForeignSql(Table table, String schema) {
        return null;
    }

    private List<ForeignReference> cloneReferences(
            List<ForeignReference> foreignRefs) {
        List<ForeignReference> cloneForeignRefs = new ArrayList<ForeignReference>();
        for (ForeignReference foreignReference : foreignRefs) {
            cloneForeignRefs.add(foreignReference);
        }
        return cloneForeignRefs;
    }

    /**
     * 获取列变化增量语句
     * @param table
     * @param packageName
     * @param metadata
     * @param catalog
     * @param list
     * @throws SQLException
     */
    protected void getTableColumnUpdate(Table table, String packageName,
                                        DatabaseMetaData metadata, String catalog, List<String> list)
            throws SQLException {

        Map<String, Map<String, String>> dbColumns = getColumns(metadata,
                catalog, table);
        // 存放table中有但数据库表格中不存在的字段， 初始化时存放所有的字段信息
        Map<String, TableField> tableFieldDbNames = getFiledDbNames(table
                .getFieldList());
        // 存放table中有且数据库表格中存在的字段
        Map<String, TableField> existInTable = new HashMap<String, TableField>();
        // 存放table中无，但数据库中有的字段
        //此处可能有问题,可能因为dbColumns为空而导致tableFieldDbNames
        List<String> dropFields = checkTableColumn(dbColumns,
                tableFieldDbNames, existInTable);
        // 处理完所有表格列，若filedDbNames中依然有数据，则表格该数据是新增字段
        for (TableField field : tableFieldDbNames.values()) {
            // 如果新增的字段包含表格的主键,则直接drop整张表格，重新创建表格
            if (field.getPrimary()) {
                list.add(getDropSql(table, packageName));
                list.addAll(getCreateSql(table, packageName));
                return;
            }
        }
        // 对于table数据库中均有的字段的处理，这里是对比是否允许为空而进行相关修改
        List<String> existUpdateList = dealExistFields(existInTable, dbColumns,
                table);
        // 生成drop字段的sql
        List<String> dropList = dealDropFields(dropFields, table);
        // 生成add字段的sql
        List<String> addList = dealAddFields(tableFieldDbNames, packageName,
                table);
        list.addAll(existUpdateList);
        list.addAll(dropList);
        list.addAll(addList);
    }

    protected List<String> checkTableColumn(
            Map<String, Map<String, String>> columns,
            Map<String, TableField> fieldDbNames,
            Map<String, TableField> existInTable) {
        List<String> dropFields = new ArrayList<String>();

        for (String col : columns.keySet()) {
            // 遍历当前表格所有列
            // 若存在于map，则不处理，切从map中删除该key
            // 若不存在于map，则从表格中删除该列
            String temp = col.toUpperCase();
            if (fieldDbNames.containsKey(temp)) {
                existInTable.put(temp, fieldDbNames.get(temp));
                fieldDbNames.remove(temp);
                continue;
            }
            dropFields.add(col);
        }
        return dropFields;
    }

    protected List<String> dealDropFields(List<String> dropFields, Table table) {
        List<String> droplist = new ArrayList<String>();
        for (String column : dropFields) {
            StringBuffer ddlBuffer = new StringBuffer();
            ddlBuffer.append(String.format("ALTER TABLE %s DROP COLUMN %s",
                    getTableName(table), delimiter(column)));
            droplist.add(ddlBuffer.toString());

        }
        return droplist;
    }

    protected List<String> dealAddFields(Map<String, TableField> fieldDbNames,
                                         String packageName, Table table) {
        List<String> addList = new ArrayList<String>();
        for (TableField field : fieldDbNames.values()) {
            StringBuffer ddlBuffer = new StringBuffer();
            ddlBuffer.append(String.format("ALTER TABLE %s ADD ",
                    getTableName(table)));
            appendField(ddlBuffer, field, addList, table);
            addList.add(ddlBuffer.toString());
        }
        return addList;
    }

    /**
     * 比较数据库metadata和配置中数据
     *
     * @param existInTable 配置文件中对应tablefield k-v
     * @param dbColumns    数据库中的metadata k-v
     * @param table        表信息
     * @return
     */
    protected List<String> dealExistFields(
            Map<String, TableField> existInTable,
            Map<String, Map<String, String>> dbColumns, Table table) {
        List<String> existUpdateList = new ArrayList<String>();
        for (String fieldName : existInTable.keySet()) {

            TableField field = existInTable.get(fieldName);
            if (field.getPrimary()) {
                continue;
            }
            StandardField standardField = MetadataUtil.getStandardField(field
                    .getStandardFieldId(), this.getClass().getClassLoader());
            Map<String, String> attribute = dbColumns.get(fieldName);

/*            String tableDataType = MetadataUtil.getStandardFieldType(
                    standardField.getId(), getDatabaseType(), this.getClass()
                            .getClassLoader());*/
            String tableDataType = getDataTpOfStdFiled(standardField);
            String standardFieldName = standardField.getName();
            String dbColumnType = getDbColumnType(attribute)
                    .replaceAll(" ", "").toLowerCase();
            String remarks = getDbColumnRemarks(attribute);
            String columnDef = getDbColumnColumnDef(attribute);
            String standardComment = getComment(standardField);
            String standardDefault = getDefaultValue(field, standardField);
            standardDefault = StringUtils.defaultIfEmpty(standardDefault, null);
            columnDef = StringUtils.defaultIfEmpty(columnDef, null);
            boolean dbNullAble = Integer.parseInt(attribute.get(NULLABLE)) == DatabaseMetaData.columnNullable ? true : false;
            boolean fieldNotNull = field.getNotNull();

            ColumnContext columnContext = new ColumnContext();
            columnContext.setDbColumnType(dbColumnType);
            columnContext.setDbNullAble(dbNullAble);
            columnContext.setFieldNotNull(fieldNotNull);
            columnContext.setRemarks(remarks);
            columnContext.setStandardComment(standardComment);
            columnContext.setStandardFieldName(standardFieldName);
            columnContext.setTableDataType(tableDataType);
            columnContext.setStandardDefault(standardDefault);
            columnContext.setDbColumnDef(columnDef);
            columnContext.setTableNameWithSchema(getTableName(table));
            columnContext.setTableName(table.getName());
            columnContext.setDbDataType(attribute.get(DATA_TYPE));

            appendColumnUpdate(existUpdateList, field, standardField, columnContext);
        }
        return existUpdateList;

    }

    /**
     *列字段是否发生改变
     * @param existUpdateList
     * @param field
     * @param standardField
     * @param columnContext
     */
    protected void appendColumnUpdate(List<String> existUpdateList,
                                      TableField field, StandardField standardField,
                                      ColumnContext columnContext) {
        //类型或默认值或注释或not不一致 就被修改可以更新
        if (!checkTypeSame(columnContext.getDbColumnType(), columnContext.getTableDataType(), columnContext.getDbDataType())
                || !checkDefSame(columnContext.getStandardDefault(), columnContext.getDbColumnDef())
                || !checkCommentSame(columnContext.getStandardComment(), columnContext.getRemarks())
                || columnContext.isDbNullAble() == columnContext.isFieldNotNull()) {
            StringBuffer alterTypeBuffer = new StringBuffer();
            // 如果数据库中字段允许为空，但table中不允许为空
            alterTypeBuffer.append(createAlterTypeSql(columnContext.getTableNameWithSchema(),
                    columnContext.getStandardFieldName(), columnContext.getTableDataType()));

            // 非自增的字段设置字段默认值
            if (!field.isAutoIncrease()) {
                String fieldDefaultValue = getDefaultValue(field, standardField);
                dealDefaultValueUpdate(alterTypeBuffer, fieldDefaultValue, columnContext.getDbColumnDef());
            }
            dealNotNullSql(alterTypeBuffer, field, columnContext.isDbNullAble());

            //注释
            appendComment(columnContext.getStandardComment(), alterTypeBuffer, existUpdateList);
            existUpdateList.add(alterTypeBuffer.toString());
        }
    }

    protected void dealNotNullSql(StringBuffer alterTypeBuffer, TableField field, boolean dbNullAble) {
        if (field.getNotNull()) {//类型有变化,且not null
            alterTypeBuffer.append(" NOT NULL");
        }
    }

    protected void dealDefaultValueUpdate(StringBuffer alterTypeBuffer, String fieldDefaultValue, String columnDef) {
        //原来非空现在为null,无需加default
        if (fieldDefaultValue != null) {
            appendDefaultValue(fieldDefaultValue, alterTypeBuffer);
        }
    }

    /**
     * 有且只有一个索引
     *
     * @param uniqueColumns
     * @param fieldName
     * @return
     */
    private boolean isOnlyOneIdx(Map<String, String> uniqueColumns, String fieldName) {
        Collection<String> uCols = uniqueColumns.values();
        int idxNum = 0;
        for (String col : uCols) {
            if (col.equalsIgnoreCase(fieldName)) {
                idxNum++;
            }
        }
        if (idxNum == 1) {
            return true;
        }
        return false;
    }

    private boolean isUnique(TableField field) {
        Boolean unique = field.getUnique();
        if (unique != null && unique.booleanValue()) {
            return true;
        }
        return false;
    }

    protected boolean checkUniqueSame(Map<String, String> uniqueColumns, String indexName, boolean isFieldUnique) {
        boolean isDbUnique = false;
        if (uniqueColumns != null && uniqueColumns.keySet().contains(indexName.toUpperCase())) {
            isDbUnique = true;
        }
        return isDbUnique == isFieldUnique;
    }

    /**
     * 获取表的所有索引
     *
     * @param metadata
     * @param schema
     * @param table
     * @return
     * @throws SQLException
     */
    protected Map<String, Map<String, String>> getDbIndexColumns(
            DatabaseMetaData metadata, String schema,
            Table table)
            throws SQLException {
        ResultSet colRet = null;
        Map<String, Map<String, String>> dbIndexMap = new HashMap();
        try {
            Connection connection = metadata.getConnection();
            colRet = metadata.getIndexInfo(connection.getCatalog(), schema, table.getNameWithOutSchema(), false, false);
            boolean isExists = false;
            while (colRet.next()) {
                isExists = true;
                addToDbIndexMap(colRet, dbIndexMap);
            }
            if (!isExists) {
                //转大写,再查一次
                colRet = metadata.getIndexInfo(connection.getCatalog(), schema,
                        table.getNameWithOutSchema().toUpperCase(), false, false);
                while (colRet.next()) {
                    addToDbIndexMap(colRet, dbIndexMap);
                }
            }
            return dbIndexMap;
        } finally {
            if (colRet != null) {
                colRet.close();
            }
        }


    }

    private void addToDbIndexMap(ResultSet colRet, Map<String, Map<String, String>> dbIndexMap) throws SQLException {
        Map<String, String> indexMap;

        if (colRet.getString("INDEX_NAME") == null) {
            return;
        }
        String dbIndexOrigName = colRet.getString("INDEX_NAME");
        String dbIndexName = dbIndexOrigName.toUpperCase();
        //如果已经包含,修改字段
        if (dbIndexMap.keySet().contains(dbIndexName)) {
            indexMap = dbIndexMap.get(dbIndexName);
            String col_name = indexMap.get("COLUMN_NAME");
            indexMap.put("COLUMN_NAME", col_name + "," + colRet.getString("COLUMN_NAME").toUpperCase());
        } else {
            indexMap = new HashMap<String, String>();
            indexMap.put("INDEX_NAME", dbIndexName);
            indexMap.put("NON_UNIQUE", colRet.getString("NON_UNIQUE").toLowerCase());
            indexMap.put("COLUMN_NAME", colRet.getString("COLUMN_NAME").toUpperCase());
            indexMap.put("TABLE_NAME", colRet.getString("TABLE_NAME").toUpperCase());
            //indexMap.put("ASC_OR_DESC",colRet.getString("ASC_OR_DESC").toUpperCase());
//            indexMap.put("ORDINAL_POSITION",colRet.getString("ORDINAL_POSITION"));//序号
            dbIndexMap.put(dbIndexName, indexMap);
        }
    }

    private String getUniqueIndexInfo(String tableName, String schema) {
        StringBuffer sb = new StringBuffer();
        sb.append("SHOW INDEX FROM ");
        if (schema != null) {
            sb.append(schema).append(".");
        }
        sb.append(tableName)
                .append(" WHERE Non_unique = 0");
        return sb.toString();
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
            //oracle中为null在元数据中表现为NULL或者NULL\n
            if (columnDef.indexOf("NULL") >= 0 && "NULL".equalsIgnoreCase(columnDef.replaceAll("\n", ""))) {
                columnDef = null;
            } else {
                columnDef = SqlUtil.trim(columnDef.trim(), "^['\"`]?|['\"`]?$");
            }
        }
        return StringUtils.equals(preComparedStdDef, columnDef);
    }

    /**
     * 检查comment是否改变
     *
     * @param standardComment
     * @param remarks
     * @return
     */
    protected boolean checkCommentSame(String standardComment, String remarks) {
        return StringUtils.equals(standardComment, remarks);
    }

    /**
     * 检查数据类型是否相同
     *
     * @param dbColumnType
     * @param tableDataType
     * @return
     */
    protected boolean checkTypeSame(String dbColumnType, String tableDataType, String dbDataType) {
        String tbDataTypeLower = tableDataType.replaceAll(" ", "")
                .replaceAll(",0", "").toLowerCase();
        String dbColumnTypeTemp = dbColumnType.replaceAll(",0", "");
        //我们认为精度为0不需要做为比较对象
        return (dbColumnTypeTemp.equals(tbDataTypeLower) ||
                dbColumnTypeTemp.indexOf(tbDataTypeLower + "(") != -1);
    }

    protected abstract String createAlterTypeSql(String tableName,
                                                 String fieldName, String tableDataType);

    protected void appendBody(StringBuffer ddlBuffer, Table table, List<String> list) {
        boolean isFirst = true;
        for (TableField field : table.getFieldList()) {
            if (!isFirst) {
                ddlBuffer.append(",");
            } else {
                isFirst = false;
            }
            ddlBuffer.append("\n");
//			if(StringUtil.isBlank(field.getStandardFieldId()))	throw new RuntimeException(String.format("表[%s]中字段[%s]中的标准字段为空",table.getId(), field.getId()));
            appendField(ddlBuffer, field, list, table);
        }
        // 增加主键语句
        //由于mysql不允许在auto_increment时alter增加主键
        //直接在table内增加
        appendPrimarySql(ddlBuffer, table, list);
    }

    protected void appendField(StringBuffer ddlBuffer, TableField field, List<String> list, Table table) {
        StandardField standardField = MetadataUtil.getStandardField(
                field.getStandardFieldId(), this.getClass().getClassLoader());
        ddlBuffer.append(String.format("\t%s ",
                delimiter(DataBaseUtil.getDataBaseName(standardField.getName()))));
        ddlBuffer.append(" ");
        String stdFieldTp = getDataTpOfStdFiled(standardField);
        ddlBuffer.append(stdFieldTp);

        appendTypeAndDefault(ddlBuffer, field, standardField);
        dealComment(ddlBuffer, standardField, list);
    }

    /**
     * 根据标准字段获取标准类型
     * @param standardField
     * @return
     */
    private String getDataTpOfStdFiled(StandardField standardField) {
        BusinessType businessType = MetadataUtil.getBusinessType(standardField.getId(), getClass().getClassLoader());
        return MetadataUtil.getStandardFieldType(businessType, getDatabaseType(),
                getClass().getClassLoader());
    }

    protected void char2DbLen(List<String> businessTypeList,
                              BusinessType businessType, String databaseType) {
        if (businessTypeList.contains(businessType.getId())) {
            return;
        }
        if (businessType.getPlaceholderValueList() == null
                || businessType.getPlaceholderValueList().size() != 1) {
            return;
        }

        String stdType = MetadataUtil.getBaseStandardType(businessType, databaseType,
                getClass().getClassLoader());
        Integer expendSize;
        //只有char和varchar需要处理
        if (stdType.toLowerCase().startsWith("char")) {
            expendSize = ConfigUtil.getChar2ByteSize();
        } else if (stdType.toLowerCase().startsWith("varchar")
                || stdType.toLowerCase().equals("lvarchar")) {
            expendSize = ConfigUtil.getVarchar2ByteSize();
        } else {
            return;
        }
        PlaceholderValue placeholderValue =
                businessType.getPlaceholderValueList().get(0);
        placeholderValue.setExpendSize(expendSize);
        businessTypeList.add(businessType.getId());
    }

    protected void appendTypeAndDefault(StringBuffer ddlBuffer, TableField field, StandardField standardField) {
        // 非自增的字段设置字段默认值
        if (!field.isAutoIncrease()) {
            String fieldDefaultValue = getDefaultValue(field, standardField);
            appendDefaultValue(fieldDefaultValue, ddlBuffer);
        }
        Boolean notNull = field.getNotNull();
        if (notNull != null && notNull.booleanValue()) {
            ddlBuffer.append(" NOT NULL");
        }

        // 处理自增
        if (field.isAutoIncrease() && field.getPrimary()) {// 如果是自增而且是主键
            ddlBuffer.append(appendIncrease());
        }
    }


    /**
     * 处理comment
     *
     * @param ddlBuffer
     * @param standardField
     * @param list
     */
    private void dealComment(StringBuffer ddlBuffer, StandardField standardField, List<String> list) {
        // 设置字段备注信息
        String comment = getComment(standardField);
        appendComment(comment, ddlBuffer, list);
    }

    protected String getComment(StandardField standardField) {
        String title = standardField.getTitle();
        String description = standardField.getDescription();
        String comment =
                StringUtils.isBlank(title) ? description :
                        (StringUtils.isBlank(description) ? title : (title + " " + description));
        return comment == null ? "" : comment;
    }

    protected void appendComment(String comment, StringBuffer ddlBuffer, List<String> list) {
        if (!StringUtils.isBlank(comment)) {
            ddlBuffer.append(" COMMENT ")
                    .append("'")
                    .append(comment.replaceAll("'", "\\\\'").replaceAll("\"", "\\\\\""))
                    .append("'");
        }
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
        String columnName;
        if (StringUtils.isBlank(table.getSchema())) {
            columnName = String.format("%s.%s", delimiter(table.getNameWithOutSchema()),
                    delimiter(standardField.getName()));
        } else {
            columnName = String.format("%s.%s.%s", delimiter(table.getSchema()), delimiter(table.getNameWithOutSchema()),
                    delimiter(standardField.getName()));
        }
        StringBuffer commentBuffer = new StringBuffer();
        commentBuffer.append("COMMENT ON COLUMN ").append(columnName)
                .append(" IS ").append("'").append(standardComment).append("'");
        list.add(commentBuffer.toString());
    }

    // 如果是字符串类型
    protected void appendDefaultValue(String defaultValue,
                                      StringBuffer ddlBuffer) {
        if (!StringUtils.isBlank(defaultValue)) {
            ddlBuffer.append(" DEFAULT ").append(defaultValue);
        }
    }

    protected String appendIncrease() {
        return "";
    }

    private List<String> appendIndexes(Table table) {
        table.setIndexList(mergeIndexes(table.getIndexList(), table));
        List<String> indexSqlList = new ArrayList<String>();
        List<Index> list = table.getIndexList();
        if (list != null) {
            for (Index index : list) {
                indexSqlList.add(getIndex(index, table));
            }
        }
        return indexSqlList;
    }

    private String getIndex(Index index, Table table) {
        // DROP INDEX index_name ON table_name
        // DROP INDEX table_name.index_name
        // DROP INDEX index_name
        StringBuffer ddlBuffer = new StringBuffer();
        Boolean unique = index.getUnique();
        if (unique != null && unique.booleanValue()) {
            ddlBuffer.append("CREATE UNIQUE INDEX ");
        } else {
            ddlBuffer.append("CREATE INDEX ");
        }

        ddlBuffer.append(delimiter(index.getName()));

        ddlBuffer.append(" ON ");
        //schema不为空则将schema append上
        if (!StringUtils.isEmpty(table.getSchema())) {
            ddlBuffer.append(delimiter(table.getSchema())).append(".");
        }
        ddlBuffer.append(delimiter(table.getNameWithOutSchema()))
                .append(" ( ");
        List<IndexField> fields = index.getFields();
        String fieldsStr = "";
        if (fields != null) {
            for (IndexField field : fields) {
                if (fieldsStr.length() > 0) {
                    fieldsStr += ",";
                }
                fieldsStr += delimiter(getFieldStdFieldName(field.getField(), table));

                if (!StringUtils.isEmpty(field.getDirection())) {
                    fieldsStr = fieldsStr + " " + field.getDirection();
                }
            }
        }
        ddlBuffer.append(fieldsStr);
        ddlBuffer.append(" ) ");
        appendIndexReverse(ddlBuffer, index);
        appendTableSpace(ddlBuffer, table);
        return ddlBuffer.toString();
    }

    protected void appendIndexReverse(StringBuffer ddlBuffer, Index index) {
        //default do nothing
    }

    protected String getIndexName(Index index, Table table) {
        String indexName = null;
        if (table.getSchema() == null || "".equals(table.getSchema())) {
            indexName = index.getName();
        } else {
            indexName = String.format("%s.%s", delimiter(table.getSchema()),
                    delimiter(index.getName()));
        }

        return indexName;
    }

    protected String getFieldStdFieldName(String fieldId, Table table) {
        for (TableField field : table.getFieldList()) {
            if (field.getId().equals(fieldId)) {
                StandardField standardField = MetadataUtil.getStandardField(
                        field.getStandardFieldId(), this.getClass()
                                .getClassLoader());
                return DataBaseUtil.getDataBaseName(standardField.getName());
            }
        }
        throw new RuntimeException(String.format(
                "未找到ID：%s的表格字段(或该表格字段对应的标准字段)", fieldId));
    }

    protected void appendFooter(StringBuffer ddlBuffer, Table table, List<String> list) {
        ddlBuffer.append("\n").append(")");
        appendTableSpace(ddlBuffer, table);
    }

    /**
     * 表空间默认实现
     *
     * @param ddlBuffer
     * @param table
     */
    protected void appendTableSpace(StringBuffer ddlBuffer, Table table) {
        if (!StringUtils.isEmpty(table.getTableSpace())) {
            TableSpace tableSpace = DataBaseUtil.getTableSpace(getClass().getClassLoader(), table.getTableSpace());
            ddlBuffer.append(" TABLESPACE ").append(delimiter(tableSpace.getName()));
        }
    }

    private void appendHeader(StringBuffer ddlBuffer, Table table, List<String> list) {
        ddlBuffer.append(String.format("CREATE TABLE %s (", getTableName(table)));
    }

    protected String getTableName(Table table) {
        if (table.getSchema() == null || "".equals(table.getSchema())) {
            return delimiter(table.getNameWithOutSchema());
        }
        return String.format("%s.%s", delimiter(table.getSchema()), delimiter(table.getNameWithOutSchema()));
    }

    /**
     * 将tablefields组装成k-v
     *
     * @param fields
     * @return
     */
    private Map<String, TableField> getFiledDbNames(List<TableField> fields) {
        Map<String, TableField> filedDbNames = new HashMap<String, TableField>();
        for (TableField field : fields) {
            StandardField standardField = MetadataUtil.getStandardField(field
                    .getStandardFieldId(), this.getClass().getClassLoader());
            String filedDbName = DataBaseUtil.getDataBaseName(standardField
                    .getName());
            filedDbNames.put(DataBaseNameUtil.getColumnNameFormat(filedDbName),
                    field);
        }
        return filedDbNames;
    }

    public String getDropSql(Table table, String packageName) {
        return String.format("DROP TABLE %s", getTableName(table));
    }

    protected Map<String, Map<String, String>> getColumns(
            DatabaseMetaData metadata, String catalog, Table table)
            throws SQLException {
        String tableName = table.getNameWithOutSchema();
        return getColumns(metadata, catalog, getSchema(table.getSchema(), metadata.getConnection()), tableName);
    }

    private Map<String, Map<String, String>> getColumns(
            DatabaseMetaData metadata, String catalog, String schema,
            String tableName) throws SQLException {

        ResultSet colRet = null;
        if (allTableColsCache.containsKey(tableName.toUpperCase())) {
            return allTableColsCache.get(tableName.toUpperCase());
        }
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
        allTableColsCache.put(tableName.toUpperCase(), map);
        try {
            colRet = metadata.getColumns(catalog, schema, tableName, "%");
            boolean exist = false;
            while (colRet.next()) {
                getMapByResultSet(colRet, map);
                exist = true;
            }
            if (!exist) {
                colRet.close();
                colRet = metadata.getColumns(catalog, schema,
                        tableName.toUpperCase(), "%");
                while (colRet.next()) {
                    getMapByResultSet(colRet, map);
                    exist = true;
                }
                if (!exist && schema != null) {
                    colRet.close();
                    colRet = metadata.getColumns(catalog, schema.toUpperCase(),
                            tableName.toUpperCase(), "%");
                    while (colRet.next()) {
                        getMapByResultSet(colRet, map);
                        exist = true;
                    }
                }
            }
        } finally {
            if (colRet != null) {
                colRet.close();
            }
        }
        return map;
    }

    private void getMapByResultSet(ResultSet colRet,
                                   Map<String, Map<String, String>> map) throws SQLException {
        Map<String, String> attributes = new HashMap<String, String>();
        String columnName = colRet.getString(COLUMN_NAME);
        attributes.put(NULLABLE, colRet.getString(NULLABLE));
        attributes.put(TYPE_NAME, colRet.getString(TYPE_NAME));
        attributes.put(COLUMN_SIZE, colRet.getString(COLUMN_SIZE));
        attributes.put(DECIMAL_DIGITS, colRet.getString(DECIMAL_DIGITS));
        attributes.put(COLUMN_DEF, colRet.getString(COLUMN_DEF));
        attributes.put(REMARKS, colRet.getString(REMARKS));
        attributes.put(DATA_TYPE, colRet.getString(DATA_TYPE));
        map.put(columnName.toUpperCase(), attributes);
    }

    protected String getSchema(Table table, DatabaseMetaData metadata) throws SQLException {
        return DataBaseUtil.getSchema(table, metadata);
    }

    public boolean checkTableExist(Table table, Connection connection)
            throws SQLException {
        ResultSet resultset = null;
        DatabaseMetaData metadata = connection.getMetaData();
        try {
            String schema = getSchema(table, metadata);
            resultset = metadata.getTables(connection.getCatalog(), null,
                    table.getNameWithOutSchema().toLowerCase(), new String[]{"TABLE"});
            boolean hasNext = resultset.next();
            //mysql这种数据库是用catalog作为一个数据库
            if (connection.getCatalog() != null && hasNext) {
                return true;
            }
            while (hasNext) {
                String tableSchema = resultset.getString("TABLE_SCHEM");
                if (tableSchema != null && tableSchema.equalsIgnoreCase(schema)) {
                    return true;
                }
            }
//            resultset.close();// 关闭上次打开的
            resultset = metadata.getTables(connection.getCatalog(), null,
                    table.getNameWithOutSchema().toUpperCase(),
                    new String[]{"TABLE"});
            while (resultset.next()) {
                String tableSchema = resultset.getString("TABLE_SCHEM");
                if (tableSchema != null && tableSchema.equalsIgnoreCase(schema)) {
                    return true;
                }
            }
        } finally {
            if (resultset != null) {
                resultset.close();
            }
        }
        return false;
    }

    protected String getDbColumnType(Map<String, String> attributes) {
        //整形且没有精度,直接返回类型
        if (ColTypeGroupUtil.isNumberType(attributes.get(TYPE_NAME))
                && (
                attributes.get(DECIMAL_DIGITS) == null || "0".equals(attributes.get(DECIMAL_DIGITS))
        )
                ) {
            return attributes.get(TYPE_NAME);
        }
        String lengthInfo = attributes.get(COLUMN_SIZE);
        if (attributes.get(DECIMAL_DIGITS) != null) {
            lengthInfo = lengthInfo + "," + attributes.get(DECIMAL_DIGITS);
        }
        return String.format("%s(%s)", attributes.get(TYPE_NAME), lengthInfo);
    }

    /**
     * 获取注释
     *
     * @param attributes
     * @return
     */
    protected String getDbColumnRemarks(Map<String, String> attributes) {
        return attributes.get(REMARKS);
    }

    /**
     * 获取默认值
     *
     * @param attributes
     * @return
     */
    protected String getDbColumnColumnDef(Map<String, String> attributes) {
        return attributes.get(COLUMN_DEF);
    }

    /**
     *
     * @param field
     * @param standardField
     * @return
     */
    protected String getDefaultValue(TableField field, StandardField standardField) {
        String defaultId = getDefaultId(field, standardField);
        if (!StringUtils.isEmpty(defaultId)) {
            String defaultConfigValue = MetadataUtil.getDefaultValue(defaultId, getDatabaseType(), this.getClass().getClassLoader());
            return DataBaseUtil.formatByColumnType(defaultConfigValue, standardField,
                    getDatabaseType(), new SqlValueProcessor() {
                        @Override
                        public String handleDateType(String value) {
                            return dealDateType(value);
                        }
                    }, getClass().getClassLoader());
        }
        return null;
    }

    /**
     * 处理日期类型
     * 因为初始化数据是字符串类型,而实际是date类型,有些数据库需要进行转换。
     * @param value
     * @return
     */
    protected String dealDateType(String value) {
        return value;
    }

    private String getDefaultId(TableField field, StandardField standardField) {
        String fieldDefaultId = standardField.getDefaultValue();
        if (StringUtils.isEmpty(fieldDefaultId)) {
            StandardType standardType = MetadataUtil.getStandardType(standardField, getClass().getClassLoader());
            fieldDefaultId = standardType.getDefaultValueId();
        }

        return fieldDefaultId;
    }

    /**
     *
     * @param name
     * @return
     */
    protected String delimiter(String name) {
        return name;
    }


    public List<String> getClearTableSql(Table table, Connection connection) throws SQLException {
        return new ArrayList<String>();
    }

    public void initChar2Byte(String language) {
        Map<String, BusinessType> businessTypeMap =
                MetadataUtil.getAllBusinessTypes(getClass().getClassLoader());
        List<String> businessTypeList = new ArrayList<String>();
        for (BusinessType businessType : businessTypeMap.values()) {
            char2DbLen(businessTypeList, businessType, getDatabaseType());
        }
    }
}
