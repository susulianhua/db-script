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
package com.xquant.metadata.config.stdfield;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.xquant.metadata.config.BaseObject;

import java.util.List;

@XStreamAlias("standard-field")
public class StandardField extends BaseObject {
    @XStreamAsAttribute
    @XStreamAlias("business-type-id")
    private String typeId;// 标准数据类型
    @XStreamAsAttribute
    @XStreamAlias("default-value-id")
    private String defaultValue;
    @XStreamAlias("nick-names")
    private List<NickName> nickNames;// 别名列表


    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public List<NickName> getNickNames() {
        return nickNames;
    }

    public void setNickNames(List<NickName> nickNames) {
        this.nickNames = nickNames;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
