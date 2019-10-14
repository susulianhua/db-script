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
package org.tinygroup.service.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("service-parameter")
public class ServiceParameter {
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    @XStreamAlias("local-name")
    private String localName;
    @XStreamAsAttribute
    private String type;
    @XStreamAlias("collection-type")
    @XStreamAsAttribute
    private String collectionType;// 参数类型
    @XStreamAsAttribute
    private boolean required;
    @XStreamAsAttribute
    @XStreamAlias("is-array")
    private boolean isArray;
    @XStreamAsAttribute
    @XStreamAlias("validate-scene")
    private String validatorScene;

    private String description;

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getValidatorScene() {
        return validatorScene;
    }

    public void setValidatorScene(String validatorScene) {
        this.validatorScene = validatorScene;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean isArray) {
        this.isArray = isArray;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
