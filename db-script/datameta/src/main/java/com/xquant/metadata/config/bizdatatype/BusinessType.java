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
package com.xquant.metadata.config.bizdatatype;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.xquant.metadata.config.BaseObject;
import com.xquant.metadata.config.PlaceholderValue;

import java.util.List;

/**
 * 业务类型
 *
 * @author luoguo
 */
@XStreamAlias("business-type")
public class BusinessType extends BaseObject {
    @XStreamAsAttribute
    @XStreamAlias("standard-type-id")
    private String typeId;// 对应的标准类型
    @XStreamAsAttribute
    @XStreamAlias("dict-id")
    private String dictTypeId;
    @XStreamAlias("placeholder-value-list")
    private List<PlaceholderValue> placeholderValueList;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<PlaceholderValue> getPlaceholderValueList() {
        return placeholderValueList;
    }

    public void setPlaceholderValueList(
            List<PlaceholderValue> placeholderValueList) {
        this.placeholderValueList = placeholderValueList;
    }

    public String getDictTypeId() {
        return dictTypeId;
    }

    public void setDictTypeId(String dictTypeId) {
        this.dictTypeId = dictTypeId;
    }
}
