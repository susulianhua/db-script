package com.xquant.database.table.dropsql.impl;

import com.xquant.database.config.table.Table;

/**
 * 支持if exists语句的实现
 */
public class DropTableSupportExistsSqlProcessorImpl extends DropTableSqlProcessorImpl {

    protected String getSingleDropSql(Table table) {
        return "DROP TABLE IF EXISTS " + delimiter(table.getNameWithOutSchema());
    }
}
