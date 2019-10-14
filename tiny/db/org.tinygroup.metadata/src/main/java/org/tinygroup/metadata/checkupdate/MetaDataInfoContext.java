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
package org.tinygroup.metadata.checkupdate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwy11342 on 2016/10/8.
 */
public class MetaDataInfoContext {
    private static MetaDataInfoContext metaDataInfoContext = new MetaDataInfoContext();

    //元数据表对应列表
    private List<MetaDataFileInfo> metaDataFileInfoList = new ArrayList<MetaDataFileInfo>();

    //key list
    private List<String> keys = new ArrayList<String>();

    private MetaDataInfoContext() {

    }

    public static MetaDataInfoContext getInstance() {
        return metaDataInfoContext;
    }

    public List<MetaDataFileInfo> getMetaDataFileInfoList() {
        return metaDataFileInfoList;
    }

    public void setMetaDataFileInfoList(List<MetaDataFileInfo> metaDataFileInfoList) {
        this.metaDataFileInfoList = metaDataFileInfoList;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public void put(MetaDataFileInfo metaDataFileInfo) {
        if (!metaDataFileInfoList.contains(metaDataFileInfo)) {
            metaDataFileInfoList.add(metaDataFileInfo);
            keys.add(metaDataFileInfo.getResourceId() + metaDataFileInfo.getType());
        }
    }

}
