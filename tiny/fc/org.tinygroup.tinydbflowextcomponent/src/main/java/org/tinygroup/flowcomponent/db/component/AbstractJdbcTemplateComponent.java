package org.tinygroup.flowcomponent.db.component;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowcomponent.db.DataSourceHold;


/**
 * Created by wangwy11342 on 2016/8/21.
 */
public abstract class AbstractJdbcTemplateComponent implements ComponentInterface, InitializingBean {
    protected JdbcTemplate jdbcTemplate;
    String resultKey;
    String sql;
    private DataSourceHold dataSourceHold;

    public DataSourceHold getDataSourceHold() {
        return dataSourceHold;
    }

    public void setDataSourceHold(DataSourceHold dataSourceHold) {
        this.dataSourceHold = dataSourceHold;
    }

    public String getResultKey() {
        return resultKey;
    }

    public void setResultKey(String resultKey) {
        this.resultKey = resultKey;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void afterPropertiesSet() throws Exception {
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSourceHold.getDataSource());
    }
}
