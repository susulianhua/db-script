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
package org.tinygroup.databasebuinstaller.impl;

import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.database.util.DataBaseUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.metadata.checkupdate.MetaDataFileInfo;
import org.tinygroup.metadata.checkupdate.MetaDataFileManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能说明:数据库表新建、字段更新、删除操作
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-8-15 <br>
 * <br>
 */
public class TableInstallProcessor extends AbstractInstallProcessor {
    private TableProcessor tableProcessor;

    private MetaDataFileManager metaDataFileManager = MetaDataFileManager.getInstance();

    public void setTableProcessor(TableProcessor tableProcessor) {
        this.tableProcessor = tableProcessor;
    }

    private void deal(String language, Table table, List<String> sqls,
                      Connection connect, boolean isFull) throws SQLException {
        installTable(language, table, sqls, connect, isFull);
    }

    private void installTable(String language, Table table, List<String> sqls,
                              Connection connect, boolean isFull) throws SQLException {
        logger.logMessage(LogLevel.INFO, "开始生成表格语句,表格 包:{0},名:{1}",
                table.getPackageName(), table.getName());
        List<String> tableSqls;

        // 非全量且表格已经存在,需要生成增量sql
        if (!isFull && tableProcessor.checkTableExist(table, language, connect)) {
            tableSqls = tableProcessor.getUpdateSql(table,
                    table.getPackageName(), language, connect);
        } else {
            List<String> finalSqlList = new ArrayList<String>();
            if (!isFull) {
                finalSqlList = tableProcessor.clearRefSqls(table, language, connect);
            }
            tableSqls = tableProcessor.getCreateSql(table,
                    table.getPackageName(), language);
            finalSqlList.addAll(tableSqls);
            tableSqls = finalSqlList;
        }
        if (tableSqls.size() != 0) {
            logger.logMessage(LogLevel.INFO, "生成sql:{0}", tableSqls);
        } else {
            logger.logMessage(LogLevel.INFO, "无需生成Sql");
        }
        sqls.addAll(tableSqls);
        //最后加入到已经执行的table properties中
        logger.logMessage(LogLevel.INFO, "生成表格语句完成,表格 包:{0},名:{1}",
                table.getPackageName(), table.getName());
    }

    private List<Table> getModifiedTables() {
        List<Table> modifiedTableList = new ArrayList<Table>();
        List<Table> list = tableProcessor.getTables();
        for (Table table : list) {
            if (DataBaseUtil.isNeedCache()) {
                MetaDataFileInfo metaDataFileInfo = new MetaDataFileInfo();
                metaDataFileInfo.setType("TABLE");
                metaDataFileInfo.setResourceId(table.getId());
                String timeStr = String.valueOf(tableProcessor
                        .getLastModifiedTime(table.getId()));
                metaDataFileInfo.setModifiedTime(timeStr);
                //元数据信息表中检查需要更新,则加入
                if (metaDataFileManager.createSqlAndCheckUpdate(metaDataFileInfo)) {
                    modifiedTableList.add(table);
                }
            }
        }
        List<Table> filterTables = tableProcessor.getSortedTables(modifiedTableList);
        return filterTables;
    }

    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @Override
    public List<String> getProcessSqls(boolean isFull, String language,
                                       Connection connection) throws SQLException {
        logger.logMessage(LogLevel.INFO, "开始获取数据库表安装操作执行语句");
        String from = DataBaseUtil.fromSourceLocal.get();
        List<Table> filterTables;
        if (from != null && from.equals("processor")) {
            filterTables = getModifiedTables();
        } else {
            filterTables = tableProcessor.getTables();
        }
        tableProcessor.initChar2Byte(language);
        List<String> sqls = new ArrayList<String>();
        for (Table table : filterTables) {
            deal(language, table, sqls, connection, isFull);
        }
/*        if (from != null && from.equals("processor")) {
            writeProperties();
        }*/
        logger.logMessage(LogLevel.INFO, "获取数据库表安装操作执行语句结束");
        return sqls;
    }

}
