package org.tinygroup.parsedsql.base;

import javax.sql.DataSource;

/**
 * 根据metadata获取表格相关的元数据信息
 *
 * @author renhui
 */
public interface TableMetaDataProvider {

    /**
     * @param dataSource  数据源对象
     * @param catalogName 分类名
     * @param schemaName  schema名
     * @param tableName   表名
     * @return 返回数据库表对象
     */
    TableMetaData generatedKeyNamesWithMetaData(DataSource dataSource,
                                                String catalogName, String schemaName, String tableName);

}
