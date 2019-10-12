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
package com.xquant.database.initdata.impl;

import com.xquant.database.ProcessorManager;
import com.xquant.database.config.initdata.InitData;
import com.xquant.database.config.initdata.InitDatas;
import com.xquant.database.config.table.Table;
import com.xquant.database.initdata.InitDataProcessor;
import com.xquant.database.initdata.InitDataSqlProcessor;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.metadata.checkupdate.MetaDataFileInfo;
import com.xquant.metadata.checkupdate.MetaDataFileManager;
import com.xquant.metadata.util.MetadataUtil;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author chenjiao
 */
public class InitDataProcessorImpl implements InitDataProcessor {
    private static InitDataProcessor initDataProcessor = new InitDataProcessorImpl();
    private static Logger logger = LoggerFactory
            .getLogger(InitDataProcessorImpl.class);
    protected MetaDataFileManager metaDataFileManager = MetaDataFileManager.getInstance();
    /**
     * Map<表名,List<InitData>>
     */
    private Map<String, List<InitData>> initDatasIdMap = new HashMap<String, List<InitData>>();
    private Map<String, List<InitData>> initDatasNameMap = new HashMap<String, List<InitData>>();
    private Map<String, Long> initDataModifiedTimeMap = new HashMap<String, Long>();
    private ProcessorManager processorManager;

    public static InitDataProcessor getInitDataProcessor() {
        return initDataProcessor;
    }

    public ProcessorManager getProcessorManager() {
        return processorManager;
    }

    public void setProcessorManager(ProcessorManager processorManager) {
        this.processorManager = processorManager;
    }

    public List<String> getInitSql(String packageName, String tableName,
                                   String language) {
        List<InitData> tableInitDataList = getInitDataList(packageName, tableName); // 肯定不为空，为空的情况该函数会直接抛异常
        return getInitSql(tableInitDataList, language);
    }

    public List<String> getInitSql(String tableName, String language) {
        return getInitSql(null, tableName, language);
    }

    public List<String> getInitSqlByTableId(String tableId, String language) {
        List<InitData> tableInitDataList = getInitDataListByTableId(tableId);
        return getInitSql(tableInitDataList, language);
    }

    /**
     * 获取初始化sql(新增和删除语句)
     * @param language
     * @param connection
     * @param isFull
     * @return
     * @throws SQLException
     */
    public List<String> getInitSql(String language, Connection connection, boolean isFull) throws SQLException {
        List<String> doList = new ArrayList<String>();
        /*if(isFull){
            //全量需要生成删除语句
            doList.addAll(getDeInitSql(language));
        }*/
        InitDataSqlProcessor sqlProcessor = (InitDataSqlProcessor) processorManager
                .getProcessor(language, "initData");
        sqlProcessor.init(connection, isFull);
        //根据table的顺序遍历初始化数据
        List<Table> allOrderTables = DataBaseUtil.getTables(getClass().getClassLoader());
        for (Table table : allOrderTables) {
            List<InitData> tableInitDataList = initDatasIdMap.get(table.getId());
            if (tableInitDataList == null) {
                continue;
            }
            for (InitData initData : tableInitDataList) {
                if (isNeedUpdate(initData)) {
                    doList.addAll(sqlProcessor.getInitSql(initData));
                }
            }
        }
        List<String> list = new ArrayList<String>();
        if (doList.size() > 0) {
            list.addAll(sqlProcessor.getPreInitSql(allOrderTables));
            list.addAll(doList);
            list.addAll(sqlProcessor.getPostInitSql(allOrderTables));
        }
        return list;
    }

    /**
     * 是否需要更新initData
     *
     * @param initData
     * @return
     */
    private boolean isNeedUpdate(InitData initData) {
        if (DataBaseUtil.isNeedCache()) {
            MetaDataFileInfo metaDataFileInfo = new MetaDataFileInfo();
            metaDataFileInfo.setType("INIT_DATA");
            metaDataFileInfo.setResourceId(initData.getId());
            String timeStr = String.valueOf(initDataModifiedTimeMap.get(initData.getId()));
            metaDataFileInfo.setModifiedTime(timeStr);
            //元数据信息表中检查不需要更新,则false
            return metaDataFileManager.createSqlAndCheckUpdate(metaDataFileInfo);
        }
        return true;
    }

    private List<String> getInitSql(List<InitData> tableInitDataList, String language) {
        List<String> sqls = new ArrayList<String>();
        InitDataSqlProcessor sqlProcessor = (InitDataSqlProcessor) processorManager
                .getProcessor(language, "initData");
        for (InitData initData : tableInitDataList) {
            sqls.addAll(sqlProcessor.getInitSql(initData));
        }
        return sqls;
    }

    public void addInitDatas(InitDatas initDatas) {
        logger.info( "开始添加表格初始数据");
        if (initDatas == null || initDatas.getInitDataList() == null) {
            logger.info( "传入的初始数据为空，数据添加结束。");
            return;
        }
        for (InitData initData : initDatas.getInitDataList()) {
            addInitData(initData);
        }
        logger.info( "表格初始数据添加完毕");
    }

    public void removeInitDatas(InitDatas initDatas) {
        logger.info( "开始添加表格初始数据");
        if (initDatas == null || initDatas.getInitDataList() == null) {
            logger.info("传入的初始数据为空，数据添加结束。");
            return;
        }
        for (InitData initData : initDatas.getInitDataList()) {
            removeInitData(initData);
        }
        logger.info("表格初始数据添加完毕");
    }

    /**
     * 添加初始化sql
     *
     * @param initData
     */
    private void removeInitData(InitData initData) {
        String tableId = MetadataUtil.passNull(initData.getTableId());
        String tableName = DataBaseUtil.getTableById(tableId, this.getClass().getClassLoader()).getName();
        logger.info( "开始移除表格[表名:{0},表ID:{1}]的初始化数据", tableName, tableId);

        if (!MapUtils.isEmpty(initDatasIdMap)) {
            List<InitData> tableInitDataList = initDatasIdMap.get(tableName);
            if (tableInitDataList != null) {
                for (InitData tableInitData : tableInitDataList) {
                    tableInitData.getRecordList().removeAll(initData.getRecordList());
                }
            }
        }
        initDatasNameMap.remove(tableName);
        initDatasIdMap.remove(tableId);
        logger.info( "移除表格[表名:{0},表ID:{1}]的初始化数据完毕", tableName, tableId);
    }

    /**
     * 添加初始化sql
     *
     * @param initData
     */
    private void addInitData(InitData initData) {
        String tableId = MetadataUtil.passNull(initData.getTableId());
        Table table = DataBaseUtil.getTableById(tableId, this.getClass().getClassLoader());
        if (table == null) {
            throw new RuntimeException("表格" + initData.getTableId() + "不存在");
        }
        String tableName = table.getName();
        logger.info( "开始为表格[表名:{0},表ID:{1}]添加初始化数据", tableName, tableId);
        List<InitData> initDataList = new ArrayList<InitData>();
        if (initDatasIdMap.containsKey(tableId)) {
            initDataList = initDatasIdMap.get(tableId);
        }
        InitData tableInitData = new InitData();
        tableInitData.setTableId(tableId);
        tableInitData.setId(initData.getId());
        tableInitData.setRecordList(initData.getRecordList());
        initDataList.add(tableInitData);
        initDatasIdMap.put(tableId, initDataList);
        initDatasNameMap.put(tableName, initDataList);
        tableInitData.getRecordList().addAll(initData.getRecordList());


        logger.info( "表格[表名:{0},表ID:{1}]添加初始化数据完毕", tableName, tableId);
    }

    public List<InitData> getInitDataList(String tableName) {
        return getInitDataList("", tableName);
    }

    public List<InitData> getInitDataListByTableId(String tableId) {
        return initDatasIdMap.get(tableId);
    }

    public List<InitData> getInitDataList(String packageName, String tableName) {
        String realTableName = MetadataUtil.passNull(tableName);
        logger.info( "获取表格[表名:{0}]初始化数据", realTableName);
        if (initDatasNameMap.containsKey(tableName)) {
            logger.info( "成功获取表格[表名:{0}]初始化数据", realTableName);
            return initDatasNameMap.get(realTableName);
        }
        logger.info( "[包:{0}]下未找到[表名:{0}]的初始化数据", realTableName);
        if (initDatasNameMap.containsKey(realTableName)) {
            List<InitData> initDataList = initDatasNameMap.get(realTableName);
            logger.info(
                    "成功获取表格[包:{0},表名:{1}]初始化数据", realTableName);
            return initDataList;
        }
        throw new RuntimeException(String.format("获取表格[表名:%s]初始化数据失败", realTableName));
    }

    public List<String> getDeInitSql(String packageName, String tableName,
                                     String language) {
        List<InitData> tableInitDataList = getInitDataList(packageName, tableName); // 肯定不为空，为空的情况该函数会直接抛异常
        List<String> sqls = new ArrayList<String>();
        for (InitData initData : tableInitDataList) {
            sqls.addAll(getDeInitSql(initData, language));
        }
        return sqls;
    }

    public List<String> getDeInitSql(String tableName, String language) {
        return getDeInitSql(null, tableName, language);
    }

    /**
     * 初始化数据删除语句
     *
     * @param language
     * @return
     */
    public List<String> getDeInitSql(String language) {
        List<String> list = new ArrayList<String>();
        //是否在发生冲突是否删除
/*        if( !ConfigUtil.isInitDataDel()){
            logger.info("因为【init-data-conflict-delete】参数未设置为true而不产生delete语句");
            return list;
        }*/
        InitDataSqlProcessor sqlProcessor = (InitDataSqlProcessor) processorManager
                .getProcessor(language, "initData");

        List<Table> allOrderTables = DataBaseUtil.getTables(getClass().getClassLoader());
        //没有表格信息直接返回空
        if (allOrderTables.size() == 0) {
            return list;
        }
/*        for(Table table:allOrderTables){
            list.addAll(sqlProcessor.getPreInitSql(table));
        }*/

        //根据table倒序遍历初始化数据
        for (int i = allOrderTables.size() - 1; i >= 0; i--) {
            Table table = allOrderTables.get(i);
            List<InitData> tableInitDataList = initDatasIdMap.get(table.getId());
            if (tableInitDataList == null) {
                continue;
            }
            for (InitData tableInitData : tableInitDataList) {
                if (isNeedUpdate(tableInitData)) {
                    list.addAll(sqlProcessor.getDeInitSql(tableInitData));
                }
            }
        }
        return list;
    }

    private List<String> getDeInitSql(InitData tableInitData, String language) {
        List<String> sqls = new ArrayList<String>();
        InitDataSqlProcessor sqlProcessor = (InitDataSqlProcessor) processorManager
                .getProcessor(language, "initData");
        sqls.addAll(sqlProcessor.getDeInitSql(tableInitData));
        return sqls;
    }

    public List<String> getDeInitSqlByTableId(String tableId, String language) {
        List<InitData> dataList = getInitDataListByTableId(tableId);
        List<String> sqls = new ArrayList<String>();
        for (InitData initData : dataList) {
            sqls.addAll(getDeInitSql(initData, language));
        }
        return sqls;
    }

    public List<InitData> getInitDatas() {
        List<InitData> initDataList = new ArrayList<InitData>();
        for (List<InitData> tbInitDataList : initDatasIdMap.values()) {
            initDataList.addAll(tbInitDataList);
        }
        return initDataList;
    }

    public void registerModifiedTime(InitDatas initDatas, long lastModify) {
        for (InitData initData : initDatas.getInitDataList()) {
            initDataModifiedTimeMap.put(initData.getId(), lastModify);
        }
    }

}
