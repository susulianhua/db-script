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
package org.tinygroup.metadata.config.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by wangwy11342 on 2016/5/19.
 * 嵌套对象属性
 */
@XStreamAlias("object-reference")
public class ObjectReference {
    private String ref;

    private String isArray;

    private String collectionType;

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
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }
}
