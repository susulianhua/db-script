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

import com.xquant.database.ProcessorManager;
import com.xquant.database.config.table.ForeignReference;
import com.xquant.database.config.table.Table;
import com.xquant.database.config.table.Tables;
import com.xquant.database.exception.DatabaseRuntimeException;
import com.xquant.database.table.TableProcessor;
import com.xquant.database.table.TableSqlProcessor;
import com.xquant.metadata.util.MetadataUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xquant.database.exception.DatabaseErrorCode.TABLE__ADD_ALREADY_ERROR;

@Component("tableProcessor")
public class TableProcessorImpl implements TableProcessor {

    private static TableProcessor tableProcessor = new TableProcessorImpl();
    // 存储所有表信息
    private Map<String, Map<String, Table>> tableMap = new HashMap<String, Map<String, Table>>();
    private ProcessorManager processorManager;
    private List<Table> orderTables = new ArrayList<Table>();
    private Map<String, Table> idMap = new HashMap<String, Table>();
    private Map<String, Boolean> tableInited = new HashMap<String, Boolean>();
    private Map<String, Long> tableModifiedTimeMap = new HashMap<String, Long>();

    public static TableProcessor getTableProcessor() {
        return tableProcessor;
    }

    public ProcessorManager getProcessorManager() {
        return processorManager;
    }

    public void setProcessorManager(ProcessorManager processorManager) {
        this.processorManager = processorManager;
    }

    public void registerModifiedTime(Tables tables, long lastModifiedTime) {
        for (Table table : tables.getTableList()) {
            tableModifiedTimeMap.put(table.getId(), lastModifiedTime);
        }
    }

    public long getLastModifiedTime(String tableId) {
        return tableModifiedTimeMap.get(tableId);
    }

    public void addTables(Tables tables) {
        String packageName = MetadataUtil.passNull(tables.getPackageName());
        if (!tableMap.containsKey(packageName)) {
            tableMap.put(packageName, new HashMap<String, Table>());
        }
        for (Table table : tables.getTableList()) {
            addTable(table);
        }
    }

    public void removeTables(Tables tables) {
        String packageName = MetadataUtil.passNull(tables.getPackageName());
        Map<String, Table> nameMap = tableMap.get(packageName);
        if (!MapUtils.isEmpty(nameMap)) {
            for (Table table : tables.getTableList()) {
                removeTable(table);
            }
        }
    }

    public void addTable(Table table) {
        String packageName = MetadataUtil.passNull(table.getPackageName());
        if (!tableMap.containsKey(packageName)) {
            tableMap.put(packageName, new HashMap<String, Table>());
        }
        Map<String, Table> nameMap = tableMap.get(packageName);
        nameMap.put(table.getName(), table);
        if (idMap.containsKey(table.getId())) {
            //重复表格异常
            throw new DatabaseRuntimeException(TABLE__ADD_ALREADY_ERROR, table.getName(), table.getId());
        }
        idMap.put(table.getId(), table);
    }

    public void removeTable(Table table) {
        String packageName = MetadataUtil.passNull(table.getPackageName());
        Map<String, Table> nameMap = tableMap.get(packageName);
        if (!MapUtils.isEmpty(nameMap)) {
            nameMap.remove(table.getName());
        }
        idMap.remove(table.getId());
    }

    public Table getTable(String packageName, String name) {
        if (packageName != null) {
            Map<String, Table> packageMap = tableMap.get(packageName);
            if (packageMap != null) {
                Table table = packageMap.get(name);
                if (table != null) {
                    return table;
                }
            }
        }
        for (String pkgName : tableMap.keySet()) {
            Map<String, Table> packageMap = tableMap.get(pkgName);
            if (packageMap != null) {
                Table table = packageMap.get(name);
                if (table != null) {
                    return table;
                }
            }
        }
        throw new RuntimeException("未找到package:" + packageName + ",name:"
                + name + "的表格");
    }

    public Table getTable(String name) {
        return getTable(null, name);
    }

    public List<String> getCreateSql(String name, String language) {
        return getCreateSql(name, null, language);
    }

    public List<String> getCreateSql(String name, String packageName,
                                     String language) {
        Table table = getTable(packageName, name);
        return getCreateSql(table, language);
    }

    public List<String> getCreateSql(Table table, String packageName,
                                     String language) {
        table.setPackageName(packageName);
        return getCreateSql(table, language);
    }

    public List<String> getCreateSql(Table table, String language) {
        TableSqlProcessor sqlProcessor = getSqlProcessor(language);
        return sqlProcessor.getCreateSql(table, table.getPackageName());
    }

    public Table getTableById(String id) {
        return idMap.get(id);
    }

    public List<Table> getTables() {
        if (!CollectionUtils.isEmpty(orderTables)) {
            return orderTables;
        }
        for (Table table : idMap.values()) {
            addOrderTable(table);
        }
        return orderTables;
    }

    /**
     * 外部table列表排序方法
     *
     * @param tableList
     * @return
     */
    public List<Table> getSortedTables(List<Table> tableList) {
        List<Table> result = new ArrayList<Table>();
        if (!CollectionUtils.isEmpty(tableList)) {
            for (Table table : tableList) {
                sortTable(table, result);
            }
        }
        return result;
    }

    public void initChar2Byte(String language) {
        TableSqlProcessor sqlProcessor = getSqlProcessor(language);
        sqlProcessor.initChar2Byte(language);
    }

    private void sortTable(Table table, List<Table> sortTableList) {
        List<ForeignReference> references = table.getForeignReferences();
        for (ForeignReference foreignReference : references) {
            Table foreignTable = idMap.get(foreignReference.getMainTable());
            if (foreignTable.equals(table)) {
                continue;
            }
            sortTable(foreignTable, sortTableList);
        }
        if (!sortTableList.contains(table)) {
            sortTableList.add(table);
        }
    }

    private void addOrderTable(Table table) {
        List<ForeignReference> references = table.getForeignReferences();
        for (ForeignReference foreignReference : references) {
            Table foreignTable = idMap.get(foreignReference.getMainTable());
            if (foreignTable.equals(table)) {
                continue;
            }
            addOrderTable(foreignTable);
        }
        Boolean inited = tableInited.get(table.getId());
        if (inited == null || inited.equals(Boolean.FALSE)) {
            orderTables.add(table);
            tableInited.put(table.getId(), true);
        }
    }


    public List<String> getUpdateSql(String name, String packageName,
                                     String language, Connection connection) throws SQLException {
        Table table = getTable(packageName, name);
        return getUpdateSql(table, packageName, language, connection);
    }

    public List<String> getUpdateSql(Table table, String packageName,
                                     String language, Connection connection) throws SQLException {
        TableSqlProcessor sqlProcessor = getSqlProcessor(language);
        return sqlProcessor.getUpdateSql(table, packageName, connection);
    }

    public String getDropSql(String name, String packageName, String language) {
        Table table = getTable(packageName, name);
        return getDropSql(table, packageName, language);
    }

    public String getDropSql(Table table, String packageName, String language) {
        TableSqlProcessor sqlProcessor = getSqlProcessor(language);
        return sqlProcessor.getDropSql(table, packageName);
    }

    public boolean checkTableExist(Table table, String language,
                                   Connection connection) throws SQLException {
        TableSqlProcessor sqlProcessor = getSqlProcessor(language);
        return sqlProcessor.checkTableExist(table, connection);
    }

    private TableSqlProcessor getSqlProcessor(String language) {
        TableSqlProcessor sqlProcessor = (TableSqlProcessor) processorManager
                .getProcessor(language, "table");
        return sqlProcessor;
    }

    public List<String> getCreateSqls(String language) {
        List<String> sqls = new ArrayList<String>();
        List<Table> tables = getTables();
        for (Table table : tables) {
            sqls.addAll(getCreateSql(table, language));
        }
        return sqls;
    }

    /**
     * 清理关联资源
     */
    public List<String> clearRefSqls(Table table, String language, Connection connection) throws SQLException {
        TableSqlProcessor sqlProcessor = getSqlProcessor(language);
        return sqlProcessor.getClearTableSql(table, connection);
    }
}
