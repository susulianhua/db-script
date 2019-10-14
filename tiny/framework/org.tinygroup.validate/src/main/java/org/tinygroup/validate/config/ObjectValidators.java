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
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("object-validators")
public class ObjectValidators {
    @XStreamImplicit
    private List<ObjectValidator> validatorConfigList;
    @XStreamImplicit
    private List<BasicValidator> basicConfigList;

    public List<ObjectValidator> getValidatorConfigList() {
        if (validatorConfigList == null)
            validatorConfigList = new ArrayList<ObjectValidator>();
        return validatorConfigList;
    }

    public void setValidatorConfigList(
            List<ObjectValidator> validatorConfigList) {
        this.validatorConfigList = validatorConfigList;
    }

    public List<BasicValidator> getBasicConfigList() {
        if (basicConfigList == null)
            basicConfigList = new ArrayList<BasicValidator>();
        return basicConfigList;
    }

    public void setBasicConfigList(List<BasicValidator> basicConfigList) {
        this.basicConfigList = basicConfigList;
    }


}
