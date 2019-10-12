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
import com.xquant.database.config.table.TableField;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.metadata.config.stdfield.StandardField;
import com.xquant.metadata.util.MetadataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wangwy on 2017/2/25.
 */
public class SybaseInitDataSqlProcessorImpl extends InitDataSqlProcessorImpl {
    private static Logger logger = LoggerFactory
            .getLogger(SybaseInitDataSqlProcessorImpl.class);

    @Override
    public String getDbType() {
        return "sybase";
    }

    protected String delimiter(String name) {
        return name;
    }

    private String postInitSql(String tableName) {
        return String.format("SET IDENTITY_INSERT %s OFF", delimiter(tableName));
    }

    private String preInitSql(String tableName) {
        return String.format("SET IDENTITY_INSERT %s ON", delimiter(tableName));
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

    public List<String> getPostInitSql(List<Table> tableList) throws SQLException {
        List<String> list = new ArrayList<String>();
        for (Table table : tableList) {
            list.addAll(getPostInitSql(table));
        }
        return list;
    }

    private List<String> getPostInitSql(Table table) throws SQLException {
        List<String> postSqlList = new ArrayList<String>();
        postSqlList.addAll(createEnableForeignSql(table));
        return postSqlList;
    }

    public List<String> getPreInitSql(List<Table> tableList) {
        List<String> list = new ArrayList<String>();
        for (Table table : tableList) {
            list.addAll(getPreInitSql(table));
        }
        return list;
    }

    private List<String> getPreInitSql(Table table) {
        List<String> preSqlList = new ArrayList<String>();
        for (ForeignReference foreignReference : table.getForeignReferences()) {
            String baseSql = "ALTER TABLE %s DROP CONSTRAINT %s";
            String preSql = String.format(baseSql, delimiter(table.getNameWithOutSchema()), delimiter(foreignReference.getName()));
            preSqlList.add(preSql);
        }
        return preSqlList;
    }


    private Collection<? extends String> createEnableForeignSql(Table table) {
        List<String> preSqlList = new ArrayList<String>();
        for (ForeignReference foreignReference : table.getForeignReferences()) {
            Table foreignTable = DataBaseUtil.getTableById(foreignReference.getMainTable(), this
                    .getClass().getClassLoader());
            String preSql = getOneFkSql(table, foreignTable, foreignReference);
            preSqlList.add(preSql);
        }
        return preSqlList;
    }

    private String getOneFkSql(Table table, Table foreignTable, ForeignReference foreignReference) {
        return String.format("ALTER TABLE %s ADD CONSTRAINT %s FOREIGN KEY (%s) REFERENCES %s(%s)",
                delimiter(table.getNameWithOutSchema()),
                delimiter(foreignReference.getName()),
                delimiter(getFieldStdFieldName(
                        foreignReference.getForeignField(),
                        table)),
                delimiter(foreignTable.getNameWithOutSchema()),
                delimiter(getFieldStdFieldName(
                        foreignReference.getReferenceField(),
                        foreignTable)));
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
}
