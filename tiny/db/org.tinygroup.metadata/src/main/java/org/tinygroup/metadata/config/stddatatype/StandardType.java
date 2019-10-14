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
package org.tinygroup.metadata.config.stddatatype;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.tinygroup.metadata.config.BaseObject;
import org.tinygroup.metadata.config.Placeholder;
import org.tinygroup.metadata.config.stdfield.NickName;

import java.util.List;

/**
 * 标准数据类型
 *
 * @author luoguo
 */
@XStreamAlias("standard-type")
public class StandardType extends BaseObject {
    @XStreamAlias("placeholder-list")
    private List<Placeholder> placeholderList;
    @XStreamAlias("dialect-type-list")
    private List<DialectType> dialectTypeList;// 方言对应的数据类型

    @XStreamAlias("data-type")
    @XStreamAsAttribute
    private int dataType;//用于指定java.sql.Types 的 SQL 类型

    @XStreamAlias("default-value-id")
    @XStreamAsAttribute
    private String defaultValueId;//引用默认值唯一标识

    @XStreamImplicit
    private List<NickName> nickNames;

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public List<Placeholder> getPlaceholderList() {
        return placeholderList;
    }

    public void setPlaceholderList(List<Placeholder> placeholderList) {
        this.placeholderList = placeholderList;
    }

    public List<DialectType> getDialectTypeList() {
        return dialectTypeList;
    }

    public void setDialectTypeList(List<DialectType> dialectTypeList) {
        this.dialectTypeList = dialectTypeList;
    }

    public String getDefaultValueId() {
        return defaultValueId;
    }

    public void setDefaultValueId(String defaultValueId) {
        this.defaultValueId = defaultValueId;
    }

    public List<NickName> getNickNames() {
        return nickNames;
    }

    public void setNickNames(List<NickName> nickNames) {
        this.nickNames = nickNames;
    }
}
