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
package org.tinygroup.tinydb.spring;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class BatchPreparedStatementSetterImpl implements
        BatchPreparedStatementSetter {
    private List<List<Object>> params;

    private int[] columnTypes;

    public BatchPreparedStatementSetterImpl(List<List<Object>> params,
                                            int[] columnTypes) {
        this.params = params;
        this.columnTypes = columnTypes;
    }

    public List<List<Object>> getParams() {
        return params;
    }

    public void setParams(List<List<Object>> params) {
        this.params = params;
    }

    public void setValues(PreparedStatement ps, int i) throws SQLException {
        List<Object> sqlParams = params.get(i);
        doSetStatementParameters(sqlParams, ps, columnTypes);
    }

    public int getBatchSize() {
        return params.size();
    }


    private void doSetStatementParameters(List<Object> values, PreparedStatement ps, int[] columnTypes) throws SQLException {
        int colIndex = 0;
        for (Object value : values) {
            colIndex++;
            if (value instanceof SqlParameterValue) {
                SqlParameterValue paramValue = (SqlParameterValue) value;
                StatementCreatorUtils.setParameterValue(ps, colIndex, paramValue, paramValue.getValue());
            } else {
                int colType;
                if (columnTypes == null || columnTypes.length < colIndex) {
                    colType = SqlTypeValue.TYPE_UNKNOWN;
                } else {
                    colType = columnTypes[colIndex - 1];
                }
                StatementCreatorUtils.setParameterValue(ps, colIndex, colType, value);
            }
        }
    }
}
