package com.xquant.database.table.dropsql.impl;


public class InformixDropTableSqlProcessorImpl extends DropTableSupportExistsSqlProcessorImpl {

    @Override
    public String getLanguageType() {
        return "informix";
    }

}
