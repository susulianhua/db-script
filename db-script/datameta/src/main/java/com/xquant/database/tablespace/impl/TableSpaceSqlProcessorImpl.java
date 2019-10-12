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
package com.xquant.database.tablespace.impl;

import com.xquant.database.config.table.Table;
import com.xquant.database.tablespace.TableSpaceProcessor;
import com.xquant.database.tablespace.TableSpaceSqlProcessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by wangwy11342 on 2016/8/2.
 */
public abstract class TableSpaceSqlProcessorImpl implements TableSpaceSqlProcessor {
    private TableSpaceProcessor tableSpaceProcessor;


    public List<String> getCreateSql(Table table, String packageName) {
        return null;
    }

    public List<String> getUpdateSql(Table table, String packageName, Connection connection) throws SQLException {
        return null;
    }
}
