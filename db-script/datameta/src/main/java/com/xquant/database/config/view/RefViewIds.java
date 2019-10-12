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
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by wangwy11342 on 2016/5/26.
 */
@XStreamAlias("ref-view-ids")
public class RefViewIds {
    @XStreamImplicit(itemFieldName = "ref-view-id")
    private List<String> refViewIdList;

    public List<String> getRefViewIdList() {
        return refViewIdList;
    }

    public void setRefViewIdList(List<String> refViewIdList) {
        this.refViewIdList = refViewIdList;
    }
}
