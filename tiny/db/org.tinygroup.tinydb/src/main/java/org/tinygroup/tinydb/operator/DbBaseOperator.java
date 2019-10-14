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

import org.springframework.jdbc.core.JdbcTemplate;
import org.tinygroup.tinydb.BeanDbNameConverter;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.Configuration;
import org.tinygroup.tinydb.dialect.Dialect;
import org.tinygroup.tinydb.exception.TinyDbException;

import java.util.UUID;


public interface DbBaseOperator extends DbRelationOperator {

    String DEFAULT_BEAN_TYPE = "TINYDB";

    BeanOperatorManager getManager();

    void setManager(BeanOperatorManager manager);

    /**
     * 获取schame
     */
    String getSchema();

    /**
     * 设置schema
     *
     * @param schema
     */
    void setSchema(String schema);

    BeanDbNameConverter getBeanDbNameConverter();

    void setBeanDbNameConverter(BeanDbNameConverter beanDbNameConverter);

    Dialect getDialect();

    void setDialect(Dialect dialect);

    /**
     * 如果是自增长类型的Key返回新增加的值
     *
     * @return
     */
    int getAutoIncreaseKey() throws TinyDbException;

    UUID getUniqueCode();

    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    void setConfiguration(Configuration configuration);
}
