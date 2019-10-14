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
package org.tinygroup.jdbctemplatedslsession.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.tinygroup.jdbctemplatedslsession.RowMapperHolder;

/**
 * SingleColumnRowMapper的选择器
 *
 * @author renhui
 */
public class SingleColumnRowMapperHolder implements RowMapperHolder {

    public boolean isMatch(Class requiredType) {
        return String.class.equals(requiredType) || Number.class.isAssignableFrom(requiredType);
    }

    public RowMapper getRowMapper(Class requiredType) {
        return new SingleColumnRowMapper(requiredType);
    }

}
