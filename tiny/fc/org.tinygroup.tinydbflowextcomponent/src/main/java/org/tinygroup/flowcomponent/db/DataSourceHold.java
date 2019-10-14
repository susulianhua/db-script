package org.tinygroup.flowcomponent.db;

import org.tinygroup.commons.tools.CollectionUtil;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class DataSourceHold {

    private DataSource dataSource;

    private List<DataSource> dataSources = new ArrayList<DataSource>();

    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        //没有配置dataSource则取datasources第一条记录
        if (dataSource == null && !CollectionUtil.isEmpty(dataSources)) {
            dataSource = dataSources.get(0);
        }
        this.dataSource = dataSource;
    }
}
