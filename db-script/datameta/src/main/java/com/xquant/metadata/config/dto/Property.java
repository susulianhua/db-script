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
package com.xquant.metadata.config.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by wangwy11342 on 2016/5/19.
 */
@XStreamAlias("property")
public class Property {
    @XStreamAsAttribute
    @XStreamAlias("ref")
    private String ref;

    @XStreamAsAttribute
    @XStreamAlias("is-array")
    private String isArray;

    @XStreamAsAttribute
    @XStreamAlias("collection-type")
    private String CollectionType;

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getIsArray() {
        return isArray;
    }

    public void setIsArray(String isArray) {
        this.isArray = isArray;
    }

    public String getCollectionType() {
        return CollectionType;
    }

    public void setCollectionType(String collectionType) {
        CollectionType = collectionType;
    }
}
