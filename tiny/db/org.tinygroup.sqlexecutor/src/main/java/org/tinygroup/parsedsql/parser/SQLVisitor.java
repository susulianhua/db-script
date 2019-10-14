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
package org.tinygroup.parsedsql.parser;

import org.tinygroup.context.Context;
import org.tinygroup.parsedsql.SQLBuilder;
import org.tinygroup.parsedsql.SqlParsedResult;
import org.tinygroup.parsedsql.base.DatabaseType;

/**
 * SQL解析基础访问器接口.
 *
 * @author renhui
 */
public interface SQLVisitor {

    /**
     * 获取数据库类型.
     *
     * @return 数据库类型
     */
    DatabaseType getDatabaseType();

    /**
     * 获取上下文对象.
     *
     * @return 上下文对象
     */
    Context getContext();

    void setContext(Context context);
    
    /**
     * 获取解析结果集对象
     * @return
     */
    SqlParsedResult getParsedResult();

    /**
     * 获取SQL构建器.
     *
     * @return SQL构建器
     */
    SQLBuilder getSQLBuilder();

    /**
     * 打印替换标记.
     *
     * @param token 替换标记
     */
    void printToken(String token);

    /**
     * 打印替换标记.
     *
     * @param label 标签
     * @param token 替换标记
     */
    void printToken(String label, String token);
}