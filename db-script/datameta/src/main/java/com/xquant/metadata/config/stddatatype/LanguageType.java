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
package com.xquant.metadata.config.stddatatype;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * 数据库语言
 *
 * @author yancheng11334
 */
@XStreamAlias("language-type")
public class LanguageType {
    /**
     * 方言类型 0：编程语言 1：数据库方言
     */
    @XStreamAsAttribute
    int type;
    @XStreamAsAttribute
    private String id;
    @XStreamAsAttribute
    private String name;
    @XStreamImplicit
    private List<LanguageField> languageFieldList;

    public List<LanguageField> getLanguageFieldList() {
        return languageFieldList;
    }

    public void setLanguageFieldList(List<LanguageField> languageFieldList) {
        this.languageFieldList = languageFieldList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
