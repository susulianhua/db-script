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
package org.tinygroup.tinydb.datasource.unpooled;

import org.tinygroup.tinydb.datasource.DataSourceFactory;
import org.tinygroup.tinydb.util.TinyDBUtil;

import javax.sql.DataSource;
import java.util.Map;

public class UnpooledDataSourceFactory implements DataSourceFactory {


    protected DataSource dataSource;

    public UnpooledDataSourceFactory() {
        this.dataSource = new UnpooledDataSource();
    }

    public void setProperties(Map<String, String> properties) {
        TinyDBUtil.setProperties(dataSource, properties);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

}
