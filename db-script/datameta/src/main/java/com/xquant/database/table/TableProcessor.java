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
package com.xquant.database.table;

import com.xquant.database.ProcessorManager;
import com.xquant.database.config.table.Table;
import com.xquant.database.config.table.Tables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 与数据表相关的处理
 *
 * @author luoguo
 */
public interface TableProcessor {
    String BEAN_NAME = "tableProcessor";

    void addTables(Tables tables);

    void removeTables(Tables tables);

    void addTable(Table table);

    void removeTable(Table table);

    Table getTable(String packageName, String name);

    Table getTable(String name);

    Table getTableById(String id);

    List<Table> getTables();

    List<String> getCreateSql(String name, String packageName, String language);

    List<String> getCreateSql(String name, String language);

    List<String> getCreateSql(Table table, String packageName, String language);

    List<String> getCreateSql(Table table, String language);

    List<String> getCreateSqls(String language);

    List<String> getUpdateSql(String name, String packageName, String language, Connection connection) throws SQLException;

    List<String> getUpdateSql(Table table, String packageName, String language, Connection connection) throws SQLException;

    String getDropSql(String name, String packageName, String language);

    String getDropSql(Table table, String packageName, String language);

    boolean checkTableExist(Table table, String language, Connection connection) throws SQLException;

    ProcessorManager getProcessorManager();

    void setProcessorManager(ProcessorManager processorManager);

    void registerModifiedTime(Tables tables, long lastModify);

    long getLastModifiedTime(String tableId);

    List clearRefSqls(Table table, String language, Connection connection) throws SQLException;

    List<Table> getSortedTables(List<Table> tableList);

    void initChar2Byte(String language);
}
