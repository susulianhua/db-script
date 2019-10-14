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
package org.tinygroup.flow.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;


/**
 * 用于定义一个组件执行完毕后，会在环境变量中放置的值
 *
 * @author luoguo
 */
@XStreamAlias("result")
public class Result implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8169256692971248803L;
    @XStreamAsAttribute
    private String name;// 参数名称
    @XStreamAsAttribute
    private String title;// 参数本地名称,i18n键值,如果i18n找不到，则原样显示
    @XStreamAsAttribute
    private String type;// 类型
    @XStreamAsAttribute
    private Boolean array;// 是否是数组，默认是false
    @XStreamAsAttribute
    private Boolean required;// 是否是必须，默认是true
    @XStreamAlias("collection-type")
    @XStreamAsAttribute
    // 参数类型
    private String collectionType;
    private String description;// 描述

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getArray() {
        return array;
    }

    public void setArray(Boolean array) {
        this.array = array;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
