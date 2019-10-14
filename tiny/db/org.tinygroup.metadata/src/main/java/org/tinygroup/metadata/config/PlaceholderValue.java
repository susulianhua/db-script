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
package org.tinygroup.metadata.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 占位符对应的值
 *
 * @author luoguo
 */
@XStreamAlias("placeholder-value")
public class PlaceholderValue {

    @XStreamAsAttribute
    private String id;
    @XStreamAsAttribute
    private String name;
    @XStreamAsAttribute
    private String value;

    private Integer expendSize = 1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        if (expendSize != null &&
                expendSize > 1) {
            return String.valueOf(Integer.valueOf(value) * expendSize);
        }
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setExpendSize(Integer expendSize) {
        this.expendSize = expendSize;
    }
}
