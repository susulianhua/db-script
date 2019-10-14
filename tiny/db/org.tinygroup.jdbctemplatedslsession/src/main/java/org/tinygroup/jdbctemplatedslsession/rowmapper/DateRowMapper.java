package org.tinygroup.jdbctemplatedslsession.rowmapper;

import org.springframework.dao.TypeMismatchDataAccessException;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.NumberUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

public class DateRowMapper implements RowMapper {

    public Date mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Validate column count.
        ResultSetMetaData rsmd = rs.getMetaData();
        int nrOfColumns = rsmd.getColumnCount();
        if (nrOfColumns != 1) {
            throw new IncorrectResultSetColumnCountException(1, nrOfColumns);
        }
        // Extract column value from JDBC ResultSet.
        Object result = getColumnValue(rs, 1, Date.class);
        return (Date) result;
    }

    protected Object getColumnValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
            return JdbcUtils.getResultSetValue(rs, index, requiredType);
    }
}
