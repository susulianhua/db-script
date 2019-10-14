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
package org.tinygroup.sqlindexsource.impl;

import org.tinygroup.context.Context;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

public abstract class AbstractResultSetOperator {

    public void updateContext(ResultSet data, int i, String name,
                              Context context) throws SQLException {
        ResultSetMetaData rsmd = data.getMetaData();
        int type = rsmd.getColumnType(i);
        if (isString(type)) {
            context.put(name, data.getString(i));
        } else if (isInt(type)) {
            context.put(name, data.getInt(i));
        } else if (isLong(type)) {
            context.put(name, data.getLong(i));
        } else if (isFloat(type)) {
            context.put(name, data.getFloat(i));
        } else if (isDouble(type)) {
            context.put(name, data.getDouble(i));
        } else if (isBigDecimal(type)) {
            context.put(name, data.getBigDecimal(i));
        } else if (isDate(type)) {
            updateDate(type, data, i, name, context);
        } else if (isByte(type)) {
            context.put(name, data.getBytes(i));
        } else {
            context.put(name, data.getObject(i));
        }
    }

    private void updateDate(int type, ResultSet data, int i, String name,
                            Context context) throws SQLException {
        if (type == Types.DATE) {
            if (data.getDate(i) != null) {
                context.put(name, new Date(data.getDate(i).getTime()));
            }
        } else if (type == Types.TIME) {
            if (data.getTime(i) != null) {
                context.put(name, new Date(data.getTime(i).getTime()));
            }
        } else {
            if (data.getTimestamp(i) != null) {
                context.put(name, new Date(data.getTimestamp(i).getTime()));
            }
        }
    }

    private boolean isString(int type) {
        if (type == Types.CHAR || type == Types.VARCHAR
                || type == Types.LONGVARCHAR) {
            return true;
        }
        return false;
    }

    private boolean isByte(int type) {
        if (type == Types.BINARY || type == Types.VARBINARY
                || type == Types.LONGVARBINARY) {
            return true;
        }
        return false;
    }

    private boolean isInt(int type) {
        if (type == Types.BIT || type == Types.BOOLEAN
                || type == Types.SMALLINT || type == Types.TINYINT
                || type == Types.INTEGER) {
            return true;
        }
        return false;
    }

    private boolean isLong(int type) {
        if (type == Types.BIGINT) {
            return true;
        }
        return false;
    }

    private boolean isFloat(int type) {
        if (type == Types.REAL || type == Types.FLOAT) {
            return true;
        }
        return false;
    }

    private boolean isDouble(int type) {
        if (type == Types.DOUBLE) {
            return true;
        }
        return false;
    }

    private boolean isBigDecimal(int type) {
        if (type == Types.DECIMAL || type == Types.NUMERIC) {
            return true;
        }
        return false;
    }

    private boolean isDate(int type) {
        if (type == Types.DATE || type == Types.TIME || type == Types.TIMESTAMP) {
            return true;
        }
        return false;
    }
}
