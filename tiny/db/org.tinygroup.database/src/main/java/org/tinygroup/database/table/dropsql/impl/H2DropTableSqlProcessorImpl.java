package org.tinygroup.database.table.dropsql.impl;


/**
 *
 */
public class H2DropTableSqlProcessorImpl extends DropTableSupportExistsSqlProcessorImpl {

    @Override
    public String getLanguageType() {
        return "h2";
    }

}
