package org.tinygroup.flowcomponent.file.component;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.flow.ComponentInterface;
import org.tinygroup.flowcomponent.db.DataSourceHold;
import org.tinygroup.vfs.VFS;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入文件到数据库
 *
 * @author qiucn
 */
public class File2DBDataComponent implements ComponentInterface, InitializingBean {
    //默认的col分隔符
    private static final String DEFAULT_COL_SEPARATOR = ",";

    /**
     * 列分隔符
     */
    private String colSeparator;
    /**
     * 数据存放文件路径
     */
    private String filePath;

    /**
     * 插入sql
     */
    private String sql;

    private JdbcTemplate jdbcTemplate;

    private DataSourceHold dataSourceHold;

    public DataSourceHold getDataSourceHold() {
        return dataSourceHold;
    }

    public void setDataSourceHold(DataSourceHold dataSourceHold) {
        this.dataSourceHold = dataSourceHold;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
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

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void execute(Context context) {
        List<Object[]> params = resolverParams();
        jdbcTemplate.batchUpdate(sql, params);
    }


    private List<Object[]> resolverParams() {
        List<Object[]> params = new ArrayList<Object[]>();
        BufferedReader br = null;
        String lineStr;
        try {
            String code = getCode();
            InputStream is = VFS.resolveFile(filePath).getInputStream();
            br = new BufferedReader(new InputStreamReader(is, code));
            while ((lineStr = br.readLine()) != null) {
                String[] strs = lineStr.split(colSeparator);
                params.add(strs);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                //do nothing
            }
        }
        return params;
    }

    private String getCode() {
        int p;
        BufferedInputStream bin = null;
        String code = null;
        try {
            bin = new BufferedInputStream(new FileInputStream(filePath));
            p = (bin.read() << 8) + bin.read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (bin != null) {
                try {
                    bin.close();
                } catch (IOException e) {
                }
            }
        }

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        return code;
    }

    public void afterPropertiesSet() throws Exception {
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSourceHold.getDataSource());
    }
}
