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
package org.tinygroup.jdbctemplatedslsession;

import javax.sql.DataSource;

/**
 * 根据metadata获取表格相关的元数据信息
 *
 * @author renhui
 */
public interface TableMetaDataProvider {

    /**
     * @param dataSource  数据源对象
     * @param catalogName 分类名
     * @param schemaName  schema名
     * @param tableName   表名
     * @return 返回数据库表对象
     */
    TableMetaData generatedKeyNamesWithMetaData(DataSource dataSource,
                                                String catalogName, String schemaName, String tableName);

    /**
     * 获取数据库类型
     *
     * @param dataSource 数据源对象
     * @return 返回数据源对应的数据库类型
     */
    String getDbType(DataSource dataSource);

}
