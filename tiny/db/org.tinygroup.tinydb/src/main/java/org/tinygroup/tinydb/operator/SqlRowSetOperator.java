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
package org.tinygroup.tinydb.operator;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.tinygroup.tinydb.exception.TinyDbException;

import java.util.List;
import java.util.Map;

/**
 * 返回结果集的操作
 *
 * @author renhui
 */
public interface SqlRowSetOperator {

    SqlRowSet getSqlRowSet(String sql) throws TinyDbException;

    SqlRowSet getSqlRowSet(String sql, Object... parameters) throws TinyDbException;

    SqlRowSet getSqlRowSet(String sql, List<Object> parameters) throws TinyDbException;

    SqlRowSet getSqlRowSet(String sql, Map<String, Object> parameters) throws TinyDbException;


}
