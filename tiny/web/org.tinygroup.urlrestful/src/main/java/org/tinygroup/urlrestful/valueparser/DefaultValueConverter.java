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
package org.tinygroup.urlrestful.valueparser;

import org.tinygroup.urlrestful.ValueConverter;

/**
 * 默认的值转换器，只有当所有转换器都匹配不到，就使用默认的转换器，直接返回参数值。
 *
 * @author renhui
 */
public class DefaultValueConverter implements ValueConverter {

    public boolean isMatch(String value) {
        return true;
    }

    public Object convert(String value) {
        return value;
    }

}
