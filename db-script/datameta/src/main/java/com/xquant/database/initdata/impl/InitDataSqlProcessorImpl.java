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

import com.xquant.database.config.initdata.InitData;
import com.xquant.database.config.initdata.Record;
import com.xquant.database.config.initdata.ValuePair;
import com.xquant.database.config.table.Table;
import com.xquant.database.config.table.TableField;
import com.xquant.database.initdata.InitDataSqlProcessor;
import com.xquant.database.sqlvalue.SqlValueProcessor;
import com.xquant.database.table.TableProcessor;
import com.xquant.database.util.DataBaseUtil;
import com.xquant.metadata.config.stdfield.StandardField;
import com.xquant.metadata.util.MetadataUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InitDataSqlProcessorImpl implements InitDataSqlProcessor {
    private Logger logger = LoggerFactory
            .getLogger(InitDataSqlProcessorImpl.class);

    private Connection connection;

    /**
     * 是否全量标志
     */
    private boolean fullSql = true;


    public List<String> getInitSql(InitData initData) {
        List<String> initSqlList = new ArrayList<String>();
        Table table = DataBaseUtil.getTableById(initData.getTableId(), this
                .getClass().getClassLoader());
        if (initData.getRecordList() != null) {
            for (Record record : initData.getRecordList()) {
                try {
                    valuePairsToAddString(initSqlList, record, table);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return initSqlList;
    }

    private void valuePairsToAddString(List<String> initSqlList, Record record, Table table) throws SQLException {
        List<ValuePair> valuePairs = record.getValuePairs();
        if (valuePairs == null || valuePairs.size() == 0) {
            return;
        } else {
            boolean hasAutoIncrement = false;
            List<String> autoIncreaseFields = new ArrayList<String>();
            for (TableField tableField : table.getFieldList()) {
                if (tableField.isAutoIncrease()) {
                    autoIncreaseFields.add(tableField.getId());
                }
            }

            StringBuffer keys = new StringBuffer();
            StringBuffer values = new StringBuffer();
            List<String> pks = getPrimaryKeys(table);
            //每张表共用一个preparedStatement
            PreparedStatement preparedStatement = null;
            boolean tableExists = false;
            if (!fullSql && pks.size() > 0) {
                TableProcessor tableProcessor = DataBaseUtil.getTableProcessor(getClass().getClassLoader());
                tableExists = tableProcessor.checkTableExist(table, getDbType(), connection);
                //表格存在,则做查询
                if (tableExists) {
                    preparedStatement = connection
                            .prepareStatement(
                                    "SELECT count(1) FROM " + table.getNameWithOutSchema() +
                                            " WHERE " + pks.get(0) + " = ?");
                }
            }

            try {
                for (ValuePair valuePair : valuePairs) {
                    //值为空
                    if (StringUtils.isBlank(valuePair.getValue())) {
                        continue;
                    }

                    //增量且表格已存在,且字段已经存在,则return
                    if (!fullSql && tableExists && recordExists(valuePair, table, preparedStatement)) {
                        return;
                    }

                    if (hasAutoIncrement == false && autoIncreaseFields.contains(valuePair.getTableFieldId())) {
                        hasAutoIncrement = true;
                    }
                    String value = valuePair.getValue();
                    StandardField standField = DataBaseUtil.getStandardField(
                            valuePair.getTableFieldId(), table, this.getClass()
                                    .getClassLoader());
                    standField.getName();
                    String standFieldName = DataBaseUtil.getDataBaseName(standField
                            .getName());
                    // 数据由用户手动填写，包括如''之类的信息
                    keys = keys.append(",").append(standFieldName);
                    //需要替换自定义函数
                    MetadataUtil.getBusinessType(standField.getId(), getClass().getClassLoader());
                    values = values.append(",").append(dealValue(value, standField));
                }
            } finally {
                //无增量且preparedStatement不为空
                if (!fullSql && preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        logger.error("preparedStatement关闭失败", e);
                    }
                }
            }
            //没有字段则不插入
            if (keys.length() == 0) {
                return;
            }
            //字段插入
            String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                    table.getNameWithOutSchema(), keys.substring(1), values.substring(1));
            if (!initSqlList.contains(sql)) {
                addInitRecord(initSqlList, sql, hasAutoIncrement, table);
            }
        }
    }

    private boolean recordExists(ValuePair valuePair, Table table, PreparedStatement preparedStatement) {
        List pks = getPrimaryKeys(table);
        StandardField standField = DataBaseUtil
                .getStandardField(valuePair.getTableFieldId(), table, this
                        .getClass().getClassLoader());

        String standFieldName = DataBaseUtil.getDataBaseName(standField
                .getName());
        if (!pks.contains(standFieldName)) {
            return false;
        }
        try {
            preparedStatement.setObject(1, valuePair.getValue());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next() && resultSet.getLong(1) > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 值处理
     * @param value
     * @param standardField
     * @return
     */
    private String dealValue(String value, StandardField standardField) {
        return DataBaseUtil.formatByColumnType(value, standardField, getDbType(), new SqlValueProcessor() {
            @Override
            public String handleDateType(String value) {
                return dealDateType(value);
            }
        }, getClass().getClassLoader());
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



    protected void addInitRecord(List<String> initSqlList, String sql, boolean hasAutoIncrement, Table table) {
        initSqlList.add(sql);
        logger.info( "添加sql:{0}", sql);
    }

    public List<String> getDeInitSql(InitData initData) {
        List<String> initSqlList = new ArrayList<String>();
        Table table = DataBaseUtil.getTableById(initData.getTableId(), this
                .getClass().getClassLoader());
        List<String> pks = getPrimaryKeys(table);
        if (initData.getRecordList() != null) {
            for (Record record : initData.getRecordList()) {
                String sql = valuePairsToDelString(record, table, pks);
                if ("".equals(sql)) {
                    continue;
                }
                logger.info( "添加sql:{0}", sql);
                if (!initSqlList.contains(sql)) {
                    initSqlList.add(sql);
                }
            }
        }
        return initSqlList;
    }

    public List<String> getPreInitSql(List<Table> tableList) {
        return new ArrayList<String>();
    }

    public List<String> getPostInitSql(List<Table> tableList) throws SQLException {
        return new ArrayList<String>();
    }

    @Override
    public void init(Connection connection, boolean fullSql) {
        this.connection = connection;
        this.fullSql = fullSql;
    }

    private List<String> getPrimaryKeys(Table table) {
        List<String> keys = new ArrayList<String>();
        for (TableField field : table.getFieldList()) {
            if (field.getPrimary()) {
                StandardField stdField = MetadataUtil
                        .getStandardField(field.getStandardFieldId(), this
                                .getClass().getClassLoader());
                keys.add(DataBaseUtil.getDataBaseName(stdField.getName())

                );
            }
        }
        return keys;
    }

    protected String delimiter(String name) {
        return name;
    }


    private String valuePairsToDelString(Record record, Table table,
                                         List<String> keys) {
        List<ValuePair> valuePairs = record.getValuePairs();
        if (valuePairs == null || valuePairs.size() == 0) {
            return "";
        }

        String where = "";
        int times = 0;// 当单条数据的delete语句生成时,times的次数应该等于keys，即主键列表的长度
        for (ValuePair valuePair : valuePairs) {
            StandardField standField = DataBaseUtil
                    .getStandardField(valuePair.getTableFieldId(), table, this
                            .getClass().getClassLoader());

            String standFieldName = DataBaseUtil.getDataBaseName(standField
                    .getName());
            String value = valuePair.getValue();
            if (!keys.contains(standFieldName)) {
                continue;// 不是主键
            }
            if (StringUtils.isBlank(value)) {
                //主键也为空直接跳过
                continue;
            }

            // 数据由用户手动填写，包括如''之类的信息
            where = where
                    + String.format(" AND %s=%s", standFieldName, value);
            times++;
        }
        if (times != keys.size()) {
            /*logger.info(LogLevel.WARN,
                    "不生成删除语句。原因:解析生成delete语句时主键数不匹配,应有主键数{0},实际{1}", keys.size(), times);
*/
            return "";
        }
        int index = where.indexOf("AND");
        where = where.substring(index + 3);
        if (StringUtils.isBlank(where)) {
            return "";
        }
        logger.info(
                "表{0}因设置主键值而生成delete语句", table.getName());
        return String.format("DELETE FROM %s WHERE %s ", table.getNameWithOutSchema(), where);
    }


    public String getDbType() {
        return null;
    }

}
