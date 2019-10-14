package org.tinygroup.jdbctemplatedslsession.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import org.tinygroup.jdbctemplatedslsession.RowMapperHolder;

public class DateRowMapperHolder implements RowMapperHolder{


    @Override
    public boolean isMatch(Class requiredType) {
        return java.util.Date.class.isAssignableFrom(requiredType);
    }

    @Override
    public RowMapper getRowMapper(Class requiredType) {
        return new DateRowMapper();
    }
}
