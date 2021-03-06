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
package org.tinygroup.metadata.config.defaultvalue;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.tinygroup.metadata.config.BaseObject;
import org.tinygroup.metadata.config.stddatatype.DialectType;

import java.util.List;

/**
 * 默认值
 * Created by wangwy11342 on 2016/7/15.
 */
@XStreamAlias("default-value")
public class DefaultValue extends BaseObject {
    @XStreamAlias("dialect-type-list")
    private List<DialectType> dialectTypeList;

    public List<DialectType> getDialectTypeList() {
        return dialectTypeList;
    }

    public void setDialectTypeList(List<DialectType> dialectTypeList) {
        this.dialectTypeList = dialectTypeList;
    }
}
