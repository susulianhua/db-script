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
package org.tinygroup.xstream.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 属性别名配置
 *
 * @author luoguo
 */
@XStreamAlias("xstream-class-property-alias")
public class XStreamClassPropertyAlias {
    @XStreamAsAttribute
    @XStreamAlias("property-name")
    private String propertyName;
    @XStreamAsAttribute
    @XStreamAlias("alias-name")
    private String aliasName;
    @XStreamAsAttribute
    @XStreamAlias("as-attribute")
    private boolean asTttribute;
    @XStreamAsAttribute
    private boolean implicit;
    @XStreamAsAttribute
    private boolean omit;


    public boolean isAsTttribute() {
        return asTttribute;
    }

    public void setAsTttribute(boolean asTttribute) {
        this.asTttribute = asTttribute;
    }

    public boolean isImplicit() {
        return implicit;
    }

    public void setImplicit(boolean implicit) {
        this.implicit = implicit;
    }

    public boolean isOmit() {
        return omit;
    }

    public void setOmit(boolean omit) {
        this.omit = omit;
    }

//	public String getAttributeName() {
//        return attributeName;
//    }
//
//    public void setAttributeName(String attributeName) {
//        this.attributeName = attributeName;
//    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

}
