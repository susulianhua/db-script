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
package com.xquant.metadata.checkupdate;


/**
 * Created by wangwy11342 on 2016/10/8.
 */
public class MetaDataFileInfo {
    //资源id
    private String resourceId;

    //资源类型(TABLE/VIEW/PROCEDURE/SEQUENCE/CUSTOM_SQL/TRIGGER/INIT_DATA)
    private String type;

    //修改时间
    private String modifiedTime;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MetaDataFileInfo metaDataFileInfo = (MetaDataFileInfo) obj;
        if (!resourceId.equals(metaDataFileInfo.getResourceId())) {
            return false;
        }
        if (!type.equals(metaDataFileInfo.getType())) {
            return false;
        }
        if (!modifiedTime.equals(metaDataFileInfo.getModifiedTime())) {
            return false;
        }
        return true;
    }
}
