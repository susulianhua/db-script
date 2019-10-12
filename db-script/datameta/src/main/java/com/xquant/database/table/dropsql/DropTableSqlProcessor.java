package com.xquant.database.table.dropsql;

import java.util.List;

public interface DropTableSqlProcessor {

    String getLanguageType();

    List<String> getDropSqls();
}
