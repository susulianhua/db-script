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
package org.tinygroup.validate.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("property-validator")
public class PropertyValidatorConfig {
    @XStreamAsAttribute
    @XStreamAlias("name")
    private String propertyName;
    @XStreamAsAttribute
    @XStreamAlias("title")
    private String propertyTitle;
    @XStreamImplicit
    private List<ValidatorConfig> validatorConfigList;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public List<ValidatorConfig> getValidatorConfigList() {
        if (validatorConfigList == null)
            validatorConfigList = new ArrayList<ValidatorConfig>();
        return validatorConfigList;
    }

    public void setValidatorConfigList(List<ValidatorConfig> validatorConfigList) {
        this.validatorConfigList = validatorConfigList;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }

}
