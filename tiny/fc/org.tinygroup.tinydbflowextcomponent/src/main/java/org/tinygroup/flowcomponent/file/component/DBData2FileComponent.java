package org.tinygroup.flowcomponent.file.component;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowcomponent.db.DataSourceHold;
import org.tinygroup.vfs.VFS;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 数据库查询数据导出到文件
 *
 * @author qiucn
 */
public class DBData2FileComponent implements ComponentInterface, InitializingBean {
    private static final String lINE_SEPARATOR = System
            .getProperty("line.separator");
    //默认的col分隔符
    private static final String DEFAULT_COL_SEPARATOR = ",";
    /**
     * 数据查询语句
     */
    private String sql;

    /**
     * 列分隔符
     */
    private String colSeparator;
    /**
     * 数据存放文件路径
     */
    private String filePath;

    private JdbcTemplate jdbcTemplate;

    private DataSourceHold dataSourceHold;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getColSeparator() {
        return colSeparator;
    }

    public void setColSeparator(String colSeparator) {
        if (StringUtil.isEmpty(colSeparator)) {
            colSeparator = DEFAULT_COL_SEPARATOR;
        } else {
            this.colSeparator = colSeparator;
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public DataSourceHold getDataSourceHold() {
        return dataSourceHold;
    }

    public void setDataSourceHold(DataSourceHold dataSourceHold) {
        this.dataSourceHold = dataSourceHold;
    }

    @SuppressWarnings("rawtypes")
    public void execute(Context context) {
        List<Map<String, Object>> resultMaps = jdbcTemplate.queryForList(sql);
        StringBuffer sb = new StringBuffer();
        for (Map<String, Object> map : resultMaps) {
            Iterator iterator = map.values().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                if (i > 0) {
                    sb.append(colSeparator);
                }
                sb.append(iterator.next());
                i++;
            }
            sb.append(lINE_SEPARATOR);
        }
        try {
            StreamUtil.writeText(sb, new FileOutputStream(VFS.resolveFile(filePath).getAbsolutePath()), "UTF-8", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void afterPropertiesSet() throws Exception {
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSourceHold.getDataSource());
    }

}
