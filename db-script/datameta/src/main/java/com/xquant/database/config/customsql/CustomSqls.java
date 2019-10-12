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
package com.xquant.database.config.customsql;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.xquant.database.config.UsePackage;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义SQL
 *
 * @author luoguo
 */
@XStreamAlias("custom-sqls")
public class CustomSqls {
    List<UsePackage> usePackages;
    @XStreamImplicit
    List<CustomSql> customSqlList;

    public List<UsePackage> getUsePackages() {
        if (usePackages == null) {
            usePackages = new ArrayList<UsePackage>();
        }
        return usePackages;
    }

    public void setUsePackages(List<UsePackage> usePackages) {
        this.usePackages = usePackages;
    }

    public List<CustomSql> getCustomSqlList() {
        if (customSqlList == null) {
            customSqlList = new ArrayList<CustomSql>();
        }
        return customSqlList;
    }

    public void setCustomSqlList(List<CustomSql> customSqlList) {
        this.customSqlList = customSqlList;
    }

}
