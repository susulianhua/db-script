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

@XStreamAlias("c-standard-field")
public class ComplexStandardField extends BaseObject {

    @XStreamAsAttribute
    private String ref;//引用标准字段的ID

    @XStreamAsAttribute
    @XStreamAlias("ref-type")
    private String refType;//引用DTO的ID

    @XStreamAsAttribute
    @XStreamAlias("is-array")
    private boolean isArray;//是否是数组

    @XStreamAsAttribute
    @XStreamAlias("collection-type")
    private String collectionType;//集合类型

    @XStreamAlias("nick-names")
    private List<NickName> nickNames;// 别名列表

    public List<NickName> getNickNames() {
        return nickNames;
    }

    public void setNickNames(List<NickName> nickNames) {
        this.nickNames = nickNames;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }
}
