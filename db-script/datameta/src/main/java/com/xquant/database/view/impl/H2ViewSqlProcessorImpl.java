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
package com.xquant.database.view.impl;

import com.xquant.database.config.view.View;

public class H2ViewSqlProcessorImpl extends ViewSqlProcessorImpl {

    protected String getDatabaseType() {
        return "h2";
    }

    /*public boolean checkViewExists(View view, Connection conn) throws SQLException {
        boolean checkResult = false;
        String checkSql = buildCheckViewSql(view);

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = conn.prepareStatement(checkSql);
            preparedStatement.setString(1, view.getName());
            resultSet.next();
            long count = resultSet.getLong(1);
            checkResult = count > 0 ? true : false;
        } finally {
            if (null != resultSet) {
                resultSet.close();
            }
            if (null != preparedStatement) {
                preparedStatement.close();
            }
        }
        return checkResult;
    }*/

    /**
     * 检测视图是否存在的sql语句
     *
     * @param view 视图构建元数据
     * @return 检测视图是否存在的sql语句
     */
    private String buildCheckViewSql(View view) {
        StringBuffer checkSql = new StringBuffer();
        checkSql.append("SELECT count(0) from information_schema.views where table_name=upper(?)");
        return checkSql.toString();
    }
}
