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
package org.tinygroup.tinydb.service.impl;

import org.tinygroup.context.Context;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.service.TinyDBService;
import org.tinygroup.tinydb.util.TinyDBUtil;

import java.util.List;

public class TinyDBServiceImpl implements TinyDBService {

    public TableConfiguration getTableConfig(String tableName, String schema) {
        return TinyDBUtil.getTableConfig(tableName, schema, this.getClass().getClassLoader());
    }

    public TableConfiguration getTableConfigByBean(String beanType, String schema) {
        return TinyDBUtil.getTableConfigByBean(beanType, schema, this.getClass().getClassLoader());
    }

    public List<String> getBeanProperties(String beanType, String schema) {
        return TinyDBUtil.getBeanProperties(beanType, schema, this.getClass().getClassLoader());
    }

    public Bean context2Bean(Context c, String beanType, String schema) {
        return TinyDBUtil.context2Bean(c, beanType, schema, this.getClass().getClassLoader());
    }

    public Bean context2Bean(Context c, String beanType, List<String> properties, String schema) {
        return TinyDBUtil.context2Bean(c, beanType, properties, schema);
    }

}
