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

public class Db2ViewSqlProcessorImpl extends ViewSqlProcessorImpl {

    protected String getDatabaseType() {
        return "db2";
    }

    /**
     * 获取mysql中检测视图是否存在的sql语句
     *
     * @param view 视图构建元数据
     * @return 检测视图是否存在的sql语句
     */
    private String buildCheckViewSql(View view) {
        StringBuffer checkSql = new StringBuffer();
        checkSql.append("SELECT count(0) FROM information_schema.VIEWS WHERE information_schema.VIEWS.TABLE_NAME = ?")
                .append(" and information_schema.VIEWS.TABLE_SCHEMA=?");
        return checkSql.toString();
    }
}
