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
package org.tinygroup.context2object.test.convert;

import org.tinygroup.context2object.TypeConverter;

import java.math.BigDecimal;

public class BigDecimalConverter implements TypeConverter<String, BigDecimal> {

    public Class<String> getSourceType() {
        // TODO Auto-generated method stub
        return String.class;
    }

    public Class<BigDecimal> getDestinationType() {
        // TODO Auto-generated method stub
        return BigDecimal.class;
    }

    public BigDecimal getObject(String value) {
        // TODO Auto-generated method stub
        return new BigDecimal(value);
    }

}
