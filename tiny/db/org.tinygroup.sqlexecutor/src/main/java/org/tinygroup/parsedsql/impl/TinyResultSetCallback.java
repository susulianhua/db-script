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
package org.tinygroup.parsedsql.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.tinygroup.parsedsql.ResultSetCallback;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TinyResultSetCallback implements
        ResultSetExtractor<Object> {

    private ResultSetCallback callback;

    public TinyResultSetCallback(ResultSetCallback callback) {
        super();
        this.callback = callback;
    }

    public Object extractData(ResultSet rs) throws SQLException,
            DataAccessException {
        callback.callback(rs);
        return null;
    }

}