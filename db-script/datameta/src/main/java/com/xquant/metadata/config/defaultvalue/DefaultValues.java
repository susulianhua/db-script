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
package com.xquant.metadata.config.defaultvalue;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.xquant.metadata.config.ExtendProperties;

import java.util.List;

/**
 * Created by wangwy11342 on 2016/7/15.
 */
@XStreamAlias("default-values")
public class DefaultValues {
    @XStreamImplicit
    private List<DefaultValue> defaultValueList;

    @XStreamAlias("extend-properties")
    private ExtendProperties extendProperties;

    public List<DefaultValue> getDefaultValueList() {
        return defaultValueList;
    }

    public void setDefaultValueList(List<DefaultValue> defaultValueList) {
        this.defaultValueList = defaultValueList;
    }

    public ExtendProperties getExtendProperties() {
        return extendProperties;
    }

    public void setExtendProperties(ExtendProperties extendProperties) {
        this.extendProperties = extendProperties;
    }
}
