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

import com.xquant.database.config.initdata.InitData;
import com.xquant.database.config.table.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface InitDataSqlProcessor {

    /**
     * 根据初始化数据生成插入语句集合
     * @param initData
     * @return
     */
    List<String> getInitSql(InitData initData);

    /**
     * 根据初始化数据生成删除语句集合
     *
     * @param initData
     * @return
     */
    List<String> getDeInitSql(InitData initData);

    /**
     * 初始化sql执行前操作
     *
     * @param tableList
     * @return
     */
    List<String> getPreInitSql(List<Table> tableList);

    /**
     * 初始化数据执行后操作
     *
     * @param tableList
     * @return
     */
    List<String> getPostInitSql(List<Table> tableList) throws SQLException;

    /**
     *
     * @param connection
     * @param fullSql
     */
    void init(Connection connection, boolean fullSql);

}
