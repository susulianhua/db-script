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
package com.xquant.database.util;


import com.xquant.database.config.table.Table;
import com.xquant.database.config.table.TableField;
import com.xquant.database.config.tablespace.TableSpace;
import com.xquant.database.config.view.View;
import com.xquant.database.sqlvalue.SqlValueProcessor;
import com.xquant.database.table.TableProcessor;
import com.xquant.database.table.impl.TableProcessorImpl;
import com.xquant.database.tablespace.TableSpaceProcessor;
import com.xquant.database.tablespace.impl.TableSpaceProcessorImpl;
import com.xquant.database.view.ViewProcessor;
import com.xquant.metadata.config.stddatatype.StandardType;
import com.xquant.metadata.config.stdfield.StandardField;
import com.xquant.metadata.util.ConfigUtil;
import com.xquant.metadata.util.MetadataUtil;
import com.xquant.namestrategy.NameStrategy;
import com.xquant.namestrategy.NormalCaseStrategy;
import com.xquant.springbean.SpringBeanUtils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.sql.Types.*;

public class DataBaseUtil {
    /* 数据库类型 */
    public static final String DB_TYPE_ORACLE = "oracle";
    public static final String DB_TYPE_DB2 = "db2";
    public static final String DB_TYPE_MYSQL = "mysql";
    public static final String DB_TYPE_SQLSERVER = "sqlserver";
    public static final String DB_TYPE_INFORMIX = "informix";
    public static final String DB_TYPE_SYBASE = "sybase";
    public static final String DB_TYPE_DERBY = "derby";
    public static final String DIALECT_FUNCTION_PROCESSOR_BEAN = "dialectFunctionProcessor";
    protected static final Pattern pattern = Pattern.compile("^'.*'$");
    public static String DATABASE_XSTREAM = "database";
    public static String INITDATA_XSTREAM = "initdata";
    public static String PROCESSOR_XSTREAM = "processor";
    public static String PROCESSORMANAGER_BEAN = "processorManager";
    public static String TABLEPROCESSOR_BEAN = "tableProcessor";
    public static String CUSTOMESQL_BEAN = "customSqlProcessor";
    public static String FUNCTION_BEAN = "dialectFunctionProcessor";
    public static String INITDATA_BEAN = "initDataProcessor";
    public static String PROCEDURE_BEAN = "procedureProcessor";
    public static String VIEW_BEAN = "viewProcessor";
    public static String TRIGGER_BEAN = "triggerProcessor";
    public static String SEQUENCE_BEAN = "sequenceProcessor";
    public static String TABLE_SPACE_PROCESSOR_BEAN = "tableSpaceProcessor";
    public static ThreadLocal<String> fromSourceLocal = new ThreadLocal<String>();

    public static StandardField getStandardField(String tableFieldId,
                                                 Table table, ClassLoader loader) {
        for (TableField field : table.getFieldList()) {
            if (field.getId().equals(tableFieldId)) {
                return MetadataUtil
                        .getStandardField(field.getStandardFieldId(), loader);
            }
        }
        return null;
    }

    public static Table getTableById(String id, ClassLoader loader) {
        return getTableProcessor(loader).getTableById(id);
    }

    public static List<Table> getTables(ClassLoader loader) {
        return getTableProcessor(loader).getTables();
    }

    public static TableProcessor getTableProcessor(ClassLoader loader) {
        try {
            return SpringBeanUtils.getBean(
                    DataBaseUtil.TABLEPROCESSOR_BEAN);
        } catch (Exception e) {

        }
        return TableProcessorImpl.getTableProcessor();
    }

    private static TableSpaceProcessor getTableSpaceProcessor(ClassLoader loader) {
        try {
            return SpringBeanUtils.getBean(
                    DataBaseUtil.TABLE_SPACE_PROCESSOR_BEAN);
        } catch (Exception e) {

        }
        return TableSpaceProcessorImpl.getTableSpaceProcessor();
    }

    public static TableSpace getTableSpace(ClassLoader loader, String spaceId) {
        TableSpaceProcessor tableSpaceProcessor = getTableSpaceProcessor(loader);
        return tableSpaceProcessor.getTableSpace(spaceId);
    }

    public static View getViewById(String id, ClassLoader loader) {
        ViewProcessor viewProcessor = SpringBeanUtils.getBean(
                DataBaseUtil.VIEW_BEAN);
        return viewProcessor.getViewById(id);
    }


    public static TableField getPrimaryField(Table table) {
        for (TableField field : table.getFieldList()) {
            if (field.getPrimary()) {
                return field;
            }
        }
        throw new RuntimeException("表格" + table.getName() + "主键不存在");
    }

    public static NameStrategy getNameStrategy() {
        return new NormalCaseStrategy();
    }

    public static String getDataBaseName(String name) {
        return getNameStrategy().getFieldName(name);
    }

    public static String getSchema(Table table, DatabaseMetaData metadata) throws SQLException {
        String schema = table.getSchema();
        if (schema == null || "".equals(schema)) {
            schema = metadata.getUserName();
        }
        return schema;
    }


    public static void closeResultSet(ResultSet r) {
        if (r != null) {
            try {
                r.close();
            } catch (SQLException e) {
            }
        }
    }

    public static boolean isNeedCache() {
        String from = fromSourceLocal.get();
        //从processor且检查更新则需要缓存
        return ("processor".equals(from) && ConfigUtil.isCheckModified());
    }



    public static String formatByColumnType(String value, StandardField standardField, String dbType, SqlValueProcessor sqlValueProcessor,
                                            ClassLoader classLoader) {
        StandardType standardType = MetadataUtil.getStandardType(standardField, classLoader);
        int sqlType = standardType.getDataType();
        String result = value;
        boolean hasQuotes = pattern.matcher(value).matches();
        switch (sqlType) {
            case VARCHAR:
            case NVARCHAR:
            case LONGNVARCHAR:
            case LONGVARCHAR:
            case CHAR:
            case NCHAR:
                break;
            case DATE:
            case TIME:
            case TIMESTAMP:
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 自定义处理插入value
     * @param value
     * @param dbType
     * @param sqlType
     * @return
     */

    /**
     * 简单判断是否是函数类型
     * 去除符号后不是数字的都认为是函数
     * @return
     */
    private static boolean isSqlFunction(String value) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher matcher = pattern.matcher(value.replaceAll("[-|:| |']", ""));
        return !matcher.matches();
    }

}
