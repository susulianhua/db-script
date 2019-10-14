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
package org.tinygroup.tinydb.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.tinygroup.commons.tools.StringUtil;

/**
 * @author renhui
 */
@XStreamAlias("order-by-config")
public class OrderByConfig {

    private static final String DEFAULT_ORDER_MODE = "asc";
    @XStreamAsAttribute
    @XStreamAlias("order-mode")
    String orderMode;
    @XStreamAlias("property-name")
    @XStreamAsAttribute
    private String propertyName;

    public OrderByConfig() {
        super();
    }

    public OrderByConfig(String propertyName, String orderMode) {
        super();
        this.propertyName = propertyName;
        this.orderMode = orderMode;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getOrderMode() {
        if (StringUtil.isBlank(orderMode)) {
            orderMode = DEFAULT_ORDER_MODE;
        }
        return orderMode;
    }

    public void setOrderMode(String orderMode) {
        this.orderMode = orderMode;
    }

}
