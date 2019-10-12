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
package com.xquant.database.initdata;

import com.xquant.database.ProcessorManager;
import com.xquant.database.config.initdata.InitData;
import com.xquant.database.config.initdata.InitDatas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface InitDataProcessor {

    /**
     * 根据指定的包名和表名获取初始化数据的插入sql列表,若该不存在对应的表,则抛出异常
     *
     * @param packageName
     * @param tableName
     * @return
     */
    List<String> getInitSql(String packageName, String tableName,
                            String language);

    /**
     * 根据表名获取对应的初始化数据的插入sql列表,若不存在,则抛出异常
     *
     * @param tableName
     * @return
     */
    List<String> getInitSql(String tableName, String language);

    /**
     * 根据表Id获取对应的初始化数据的插入sql列表,若不存在,则抛出异常
     *
     * @param tableId
     * @return
     */
    List<String> getInitSqlByTableId(String tableId, String language);

    /**
     * 获取所有的表初始化数据的插入和删除sql列表)
     * @param language
     * @param connection
     * @param isFull
     * @return
     * @throws SQLException
     */
    List<String> getInitSql(String language, Connection connection, boolean isFull) throws SQLException;

    /**
     * 根据指定的包名和表名获取初始化数据的删除sql列表,若该不存在对应的表,则抛出异常
     *
     * @param packageName
     * @param tableName
     * @return
     */
    List<String> getDeInitSql(String packageName, String tableName,
                              String language);

    /**
     * 根据表名获取对应的初始化数据的删除sql列表,若不存在,则抛出异常
     *
     * @param tableName
     * @return
     */
    List<String> getDeInitSql(String tableName, String language);

    /**
     * 根据表id获取对应的初始化数据的删除sql列表,若不存在,则抛出异常
     *
     * @param tableId
     * @return
     */
    List<String> getDeInitSqlByTableId(String tableId, String language);

    /**
     * 获取所有的表初始化数据的删除sql列表
     *
     * @return
     */
    List<String> getDeInitSql(String language);


    /**
     * 根据表名获取对应的初始化数据,若不存在,则抛出异常
     *
     * @param tableName
     * @return
     */
    List<InitData> getInitDataList(String tableName);

    /**
     * 根据表Id取对应的初始化数据,若不存在,则抛出异常
     *
     * @param tableId
     * @return
     */
    List<InitData> getInitDataListByTableId(String tableId);

    /**
     * 根据指定的包名和表名获取初始化数据,若该不存在对应的表,则抛出异常
     *
     * @param packageName
     * @param tableName
     * @return
     */
    List<InitData> getInitDataList(String packageName, String tableName);

    /**
     * 添加表格初始化数据信息
     *
     * @param initDatas
     */
    void addInitDatas(InitDatas initDatas);

    /**
     * 移除表格初始化数据信息
     *
     * @param initDatas
     */
    void removeInitDatas(InitDatas initDatas);

    /**
     * 获取所有初始化记录
     *
     * @return
     */
    List<InitData> getInitDatas();

    ProcessorManager getProcessorManager();

    void setProcessorManager(ProcessorManager processorManager);

    void registerModifiedTime(InitDatas initDatas, long lastModify);

}
