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
package org.tinygroup.database.initdata.impl;

import org.tinygroup.database.config.table.ForeignReference;
import org.tinygroup.database.config.table.Table;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by wangwy11342 on 2016/8/15.
 */
public class Db2InitDataSqlProcessorImpl extends InitDataSqlProcessorImpl {

    public String getDbType() {
        return "db2";
    }

    private List<String> getPostInitSql(Table table) throws SQLException {
        List<String> postSqlList = new ArrayList<String>();
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
            String baseSql = "ALTER TABLE %s ALTER FOREIGN KEY %s NOT ENFORCED";
            String preSql = String.format(baseSql, delimiter(table.getNameWithOutSchema()), delimiter(foreignReference.getName()));
            preSqlList.add(preSql);
        }
        return preSqlList;
    }


    private Collection<? extends String> createEnableForeignSql(Table table) {
        List<String> preSqlList = new ArrayList<String>();
        for (ForeignReference foreignReference : table.getForeignReferences()) {
            String baseSql = "ALTER TABLE %s ALTER FOREIGN KEY %s ENFORCED";
            String preSql = String.format(baseSql, delimiter(table.getNameWithOutSchema()),
                    delimiter(foreignReference.getName()));
            preSqlList.add(preSql);
        }
        return preSqlList;
    }


}
