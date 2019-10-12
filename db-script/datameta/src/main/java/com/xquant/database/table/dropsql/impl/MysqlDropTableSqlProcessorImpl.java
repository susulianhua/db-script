package com.xquant.database.table.dropsql.impl;


/**
 *
 */
public class MysqlDropTableSqlProcessorImpl extends DropTableSupportExistsSqlProcessorImpl {

    @Override
    public String getLanguageType() {
        return "mysql";
    }

    @Override
    protected String delimiter(String name) {
        return "`" + name + "`";
    }

}
