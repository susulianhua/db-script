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

import com.xquant.database.config.SqlBody;
import com.xquant.database.config.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 生成view 创建、删除语句
 *
 * @author renhui
 */
public class ViewSqlCreator {

    protected View view;

    protected Logger logger = LoggerFactory.getLogger(ViewSqlCreator.class);

    public ViewSqlCreator(View view) {
        this.view = view;
    }

    public String getCreateSql(String language) {
        StringBuffer buffer = new StringBuffer();
        List<SqlBody> sqlBodyList = view.getSqlBodyList();
        String defaultSql = "";
        String dialectSql = null;
        for (SqlBody sqlBody : sqlBodyList) {
            if ("default".equals(sqlBody.getDialectTypeName())) {
                defaultSql = sqlBody.getContent();
            } else if (sqlBody.getDialectTypeName().equalsIgnoreCase(language)) {
                dialectSql = sqlBody.getContent();
            }
        }
        if (dialectSql != null) {
            buffer.append(dialectSql);
        } else {
            buffer.append(defaultSql);
        }

        logger.info( "新建视图sql:{1}", buffer.toString());

        return buffer.toString();

    }

    public String getDropSql() {
        return String.format("DROP VIEW %s", view.getName());
    }

    protected String getLanguage() {
        return null;
    }
}
