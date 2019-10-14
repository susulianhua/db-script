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
package org.tinygroup.database.view.impl;

import org.tinygroup.database.config.view.View;
import org.tinygroup.database.view.ViewSqlProcessor;

import java.sql.Connection;
import java.sql.SQLException;

public class ViewSqlProcessorImpl implements ViewSqlProcessor {
    protected String getDatabaseType() {
        return "oracle";
    }

    public String getCreateSql(View view) {
        ViewSqlCreator creator = new ViewSqlCreator(view);
        return creator.getCreateSql(getDatabaseType());
    }

    public String getDropSql(View view) {
        return String.format("DROP VIEW %s ", view.getName());
    }

    public boolean checkViewExists(View view, Connection conn) throws SQLException {
        /*
         * 默认返回false，大部分数据库可以使用create or replace语法，
         * 不支持的数据库可以重写此方法，然后通过drop->create的步骤来创建view
         */
        return false;
    }

    /**
     * 默认获取全量
     * @param view
     * @return
     */
    public String getUpdateSql(View view) {
        return getCreateSql(view);
    }
}
