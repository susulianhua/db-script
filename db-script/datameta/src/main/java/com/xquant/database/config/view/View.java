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
package com.xquant.database.config.view;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.xquant.database.config.SqlBody;
import com.xquant.metadata.config.BaseObject;
import java.util.List;

/**
 * @author chenjiao
 */
@XStreamAlias("view")
public class View extends BaseObject {
    @XStreamAsAttribute
    private String schema;
    @XStreamAlias("sqls")
    private List<SqlBody> sqlBodyList;
    @XStreamAlias("ref-view-ids")
    private RefViewIds refViewIds;//关联视图

    public String getName() {
        if (getSchema() == null || "".equals(getSchema())) {
            return super.getName();
        }
        return String.format("%s.%s", getSchema(), super.getName());
    }

    public String getNameWithOutSchema() {
        return super.getName();
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public List<SqlBody> getSqlBodyList() {
        return sqlBodyList;
    }

    public void setSqlBodyList(List<SqlBody> sqlBodyList) {
        this.sqlBodyList = sqlBodyList;
    }

    public RefViewIds getRefViewIds() {
        return refViewIds;
    }

    public void setRefViewIds(RefViewIds refViewIds) {
        this.refViewIds = refViewIds;
    }
}
