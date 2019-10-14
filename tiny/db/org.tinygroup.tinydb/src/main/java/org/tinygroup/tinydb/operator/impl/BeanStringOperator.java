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
package org.tinygroup.tinydb.operator.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.tinygroup.tinydb.Configuration;
import org.tinygroup.tinydb.operator.DBOperator;

/**
 * 功能说明:框架提供以String作为keytype的实现
 * <p>
 * 开发人员: renhui <br>
 * 开发时间: 2013-7-31 <br>
 * <br>
 */
public class BeanStringOperator extends GenericDbOperator<String> implements DBOperator<String> {

    public BeanStringOperator() {
        super();
    }

    public BeanStringOperator(JdbcTemplate jdbcTemplate,
                              Configuration configuration) {
        super(jdbcTemplate, configuration);
    }

}
