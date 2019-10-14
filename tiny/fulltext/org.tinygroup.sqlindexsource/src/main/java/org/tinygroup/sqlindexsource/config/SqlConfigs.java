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
package org.tinygroup.sqlindexsource.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.tinygroup.templateindex.config.BaseIndexConfig;
import org.tinygroup.templateindex.config.IndexFieldConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XStreamAlias("sql-source")
public class SqlConfigs extends BaseIndexConfig {

    @XStreamAlias("data-source-bean")
    @XStreamAsAttribute
    private String dataSourceBean;

    @XStreamAsAttribute
    private String url;

    @XStreamAsAttribute
    private String driver;

    @XStreamAsAttribute
    private String user;

    @XStreamAsAttribute
    private String password;

    @XStreamImplicit
    private List<SqlConfig> sqlConfigList;

    public String getDataSourceBean() {
        return dataSourceBean;
    }

    public void setDataSourceBean(String dataSourceBean) {
        this.dataSourceBean = dataSourceBean;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SqlConfig> getSqlConfigList() {
        return sqlConfigList;
    }

    public void setSqlConfigList(List<SqlConfig> sqlConfigList) {
        this.sqlConfigList = sqlConfigList;
    }

    public String getBeanName() {
        return "sqlConfigsIndexOperator";
    }

    public Set<String> getQueryFields() {
        Set<String> fields = new HashSet<String>();
        if (sqlConfigList != null) {
            for (SqlConfig config : sqlConfigList) {
                if (config.getFieldConfigList() != null) {
                    for (IndexFieldConfig fieldConfig : config.getFieldConfigList()) {
                        fields.add(fieldConfig.getIndexName());
                    }
                }
            }
        }
        return fields;
    }

}
