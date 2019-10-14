package org.tinygroup.database.table.dropsql.impl;

import org.tinygroup.database.config.table.Table;
import org.tinygroup.database.table.TableProcessor;
import org.tinygroup.database.table.dropsql.DropTableSqlProcessor;
import org.tinygroup.database.table.impl.TableProcessorImpl;

import java.util.ArrayList;
import java.util.List;

public class DropTableSqlProcessorImpl implements DropTableSqlProcessor {

    public static DropTableSqlProcessor dropTableSqlProcessor;


    public static DropTableSqlProcessor getInstance(String languageType) {
        if (dropTableSqlProcessor == null) {
            List<DropTableSqlProcessor> dropTableSqlProcessorList = new ArrayList<DropTableSqlProcessor>();
            DropTableSqlProcessor mysqlDropTableSqlProcessor = new MysqlDropTableSqlProcessorImpl();
            DropTableSqlProcessor informixDropTableSqlProcessor
                    = new InformixDropTableSqlProcessorImpl();
            DropTableSqlProcessor h2DropTableSqlProcessor = new H2DropTableSqlProcessorImpl();
            dropTableSqlProcessorList.add(mysqlDropTableSqlProcessor);
            dropTableSqlProcessorList.add(informixDropTableSqlProcessor);
            dropTableSqlProcessorList.add(h2DropTableSqlProcessor);
            for (DropTableSqlProcessor dropSqlProcessor : dropTableSqlProcessorList) {
                if (languageType.equalsIgnoreCase(dropSqlProcessor.getLanguageType())) {
                    return dropSqlProcessor;
                }
            }
            return new DropTableSqlProcessorImpl();
        }
        return dropTableSqlProcessor;
    }

    @Override
    public String getLanguageType() {
        return null;
    }

    @Override
    public List<String> getDropSqls() {
        TableProcessor tableProcessor = TableProcessorImpl.getTableProcessor();
        List<Table> list = tableProcessor.getTables();
        List<Table> sortedTableList = tableProcessor.getSortedTables(list);
        List<String> dropTableSqls = new ArrayList<String>();
        for (Table table : sortedTableList) {
            String sql = getSingleDropSql(table);
            dropTableSqls.add(sql);
        }
        return dropTableSqls;
    }

    protected String getSingleDropSql(Table table) {
        return "DROP TABLE " + delimiter(table.getNameWithOutSchema());
    }


    /**
     * @param name
     * @return
     */
    protected String delimiter(String name) {
        return name;
    }
}
