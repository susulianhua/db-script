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

import com.xquant.database.config.table.ForeignReference;
import com.xquant.database.config.table.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wangwy11342 on 2016/8/25.
 */
public class SqlserverInitDataSqlProcessorImpl extends InitDataSqlProcessorImpl {
    private static Logger logger = LoggerFactory
            .getLogger(SqlserverInitDataSqlProcessorImpl.class);

    protected String delimiter(String name) {
        return name;
    }

    @Override
    public String getDbType() {
        return "sqlserver";
    }

    protected void addInitRecord(List<String> initSqlList, String sql, boolean hasAutoIncrement, Table table) {
        if (hasAutoIncrement) {
            String offSql = postInitSql(table.getNameWithOutSchema());
            if (initSqlList.size() > 1 &&
                    offSql.equalsIgnoreCase(initSqlList.get(initSqlList.size() - 1))) {
                initSqlList.add(initSqlList.size() - 2, sql);
            } else {
                String onSql = preInitSql(table.getNameWithOutSchema());
                initSqlList.add(onSql);
                logger.info( "添加sql:{0}", onSql);
                super.addInitRecord(initSqlList, sql, hasAutoIncrement, table);
                initSqlList.add(offSql);
                logger.info( "添加sql:{0}", offSql);
            }
        } else {
            super.addInitRecord(initSqlList, sql, hasAutoIncrement, table);
        }
    }

    private String postInitSql(String tableName) {
        return String.format("SET IDENTITY_INSERT %s OFF", delimiter(tableName));
    }

    private String preInitSql(String tableName) {
        return String.format("SET IDENTITY_INSERT %s ON", delimiter(tableName));
    }

    public List<String> getPostInitSql(List<Table> tableList) throws SQLException {
        List<String> list = new ArrayList<String>();
        for (Table table : tableList) {
            list.addAll(getPostInitSql(table));
        }
        return list;
    }

    public List<String> getPreInitSql(List<Table> tableList) {
        List<String> list = new ArrayList<String>();
        for (Table table : tableList) {
            list.addAll(getPreInitSql(table));
        }
        return list;
    }

    private List<String> getPostInitSql(Table table) throws SQLException {
        List<String> postSqlList = new ArrayList<String>();
        postSqlList.addAll(createEnableForeignSql(table));
        return postSqlList;
    }

    private List<String> getPreInitSql(Table table) {
        List<String> preSqlList = new ArrayList<String>();
        for (ForeignReference foreignReference : table.getForeignReferences()) {
            String baseSql = "ALTER TABLE %s NOCHECK CONSTRAINT ALL";
            String preSql = String.format(baseSql, delimiter(table.getNameWithOutSchema()), delimiter(foreignReference.getName()));
            preSqlList.add(preSql);
        }
        return preSqlList;
    }

    private Collection<? extends String> createEnableForeignSql(Table table) {
        List<String> preSqlList = new ArrayList<String>();
        for (ForeignReference foreignReference : table.getForeignReferences()) {
            String baseSql = "ALTER TABLE %s CHECK CONSTRAINT ALL";
            String preSql = String.format(baseSql, delimiter(table.getNameWithOutSchema()),
                    delimiter(foreignReference.getName()));
            preSqlList.add(preSql);
        }
        return preSqlList;
    }

}
