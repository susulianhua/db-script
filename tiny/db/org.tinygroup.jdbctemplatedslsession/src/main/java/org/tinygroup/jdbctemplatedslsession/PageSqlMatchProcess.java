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

import org.tinygroup.tinysqldsl.ComplexSelect;
import org.tinygroup.tinysqldsl.Select;

/**
 * 分页sql接口处理
 *
 * @author renhui
 */
public interface PageSqlMatchProcess {

    /**
     * 判断是否是该数据库类型的分页sql处理器
     *
     * @param dbType 数据库类型
     * @return 匹配返回true，否则返回false
     */
    boolean isMatch(String dbType);

    /**
     * 对查询语句进行分页sql处理，返回具有分页信息的sql语句
     *
     * @param select 原生sql，未带分页信息的sql语句
     * @param start  分页参数 每页的起始数
     * @param limit  分页参数，每页返回的条数
     * @return
     */
    String sqlProcess(Select select, int start, int limit);

    /**
     * 对查询语句进行分页sql处理，返回具有分页信息的sql语句
     *
     * @param complexSelect 原生sql，未带分页信息的sql语句
     * @param start  分页参数 每页的起始数
     * @param limit  分页参数，每页返回的条数
     * @return
     */
    String sqlProcess(ComplexSelect complexSelect, int start, int limit);

    /**
     *
     * @param originSql 原有sql
     * @return
     */
    String getCountSql(String originSql);

}
