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
import com.xquant.metadata.util.ConfigUtil;
import com.xquant.metadata.util.MetadataUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wangwy11342 on 2016/8/15.
 */
public class OracleInitDataSqlProcessorImpl extends InitDataSqlProcessorImpl {

    private static final String FORMAT_FUNCTION_STR = "to_date(%s,'yyyy-mm-dd hh24:mi:ss')";
    //确保自增长安全调用的存储过程
    private static final String PROC_AUTO_INCREMENT_SQL = "CREATE OR REPLACE PROCEDURE proc_seq_to_safe" +
            "(v_seqname varchar2,v_tablename varchar2,v_auto_key varchar2) as\n" +
            "  cur_next number(10);\n" +
            "  tsql varchar2(100);\n" +
            "  db_now_max number(10);\n" +
            "begin\n" +
            "  execute immediate 'select max(' || v_auto_key || ') from ' || v_tablename\n" +
            "    into db_now_max;\n" +
            "  execute immediate 'select ' || v_seqname || '.nextval from dual'\n" +
            "      into cur_next;\n" +
            "  if (db_now_max > cur_next) then\n" +
            "    tsql := 'alter sequence ' || v_seqname || ' increment by ' || (db_now_max-cur_next);\n" +
            "    execute immediate tsql;\n" +
            "    execute immediate 'select ' || v_seqname || '.nextval from dual'\n" +
            "      into cur_next;\n" +
            "    tsql := 'alter sequence ' || v_seqname || ' increment by 1';\n" +
            "    execute immediate tsql;\n" +
            "  end if;\n" +
            "END proc_seq_to_safe;";
    private String procSql = "";

    public String getDbType() {
        return "oracle";
    }

    private List<String> getPostInitSql(Table table) throws SQLException {
        List<String> postSqlList = new ArrayList<String>();
        //如果使用数据库触发器自增长
        if (ConfigUtil.isUseDbTrigger()) {
            postSqlList.addAll(createProcForSeq(table));
        }
        postSqlList.addAll(createEnableForeignSql(table));
        return postSqlList;
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

    private List<String> getPreInitSql(Table table) {
        List<String> preSqlList = new ArrayList<String>();
        for (ForeignReference foreignReference : table.getForeignReferences()) {
            String baseSql = "ALTER TABLE %s DISABLE CONSTRAINT %s";
            String preSql = String.format(baseSql, delimiter(table.getNameWithOutSchema()), delimiter(foreignReference.getName()));
            preSqlList.add(preSql);
        }
        return preSqlList;
    }


    private Collection<? extends String> createEnableForeignSql(Table table) {
        List<String> preSqlList = new ArrayList<String>();
        for (ForeignReference foreignReference : table.getForeignReferences()) {
            String baseSql = "ALTER TABLE %s ENABLE CONSTRAINT %s";
            String preSql = String.format(baseSql, delimiter(table.getNameWithOutSchema()),
                    delimiter(foreignReference.getName()));
            preSqlList.add(preSql);
        }
        return preSqlList;
    }

    private Collection<? extends String> createProcForSeq(Table table) {
        List<String> list = new ArrayList<String>();
        //存储过程只加一次
        if (procSql.length() == 0) {
            procSql = PROC_AUTO_INCREMENT_SQL;
            String from = DataBaseUtil.fromSourceLocal.get();
            if ("tool".equals(from)) {
                procSql += "\n/\n";
            }
            list.add(procSql);
        }

        String primaryValField = "";
        for (TableField field : table.getFieldList()) {
            //自增长主键
            if (field.getPrimary() && field.isAutoIncrease()) {
                StandardField stdField = MetadataUtil
                        .getStandardField(field.getStandardFieldId(), this
                                .getClass().getClassLoader());
                primaryValField = DataBaseUtil.getDataBaseName(stdField.getName());
                break;
            }
        }
        if (primaryValField.length() > 0) {
            String seqName = "SEQ_" + table.getNameWithOutSchema().toUpperCase();
            //将序列调整为安全序列
            list.add(String.format("CALL proc_seq_to_safe('%s','%s','%s')", seqName,
                    table.getNameWithOutSchema().toUpperCase(), primaryValField.toUpperCase()));
        }
        return list;
    }

    protected String dealDateType(String value) {
        return String.format(FORMAT_FUNCTION_STR, value);
    }


}
