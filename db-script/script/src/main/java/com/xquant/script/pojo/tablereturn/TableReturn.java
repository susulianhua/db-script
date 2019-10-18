package com.xquant.script.pojo.tablereturn;

import java.util.List;

public class TableReturn {

    private TableBase tablebase;
    private List<TableFieldReturn> tableFieldReturnList;
    private List<ForeignReturn> foreignReturnReferences;
    private List<IndexReturn> indexReturnList;
    private List<IndexFieldReturn> indexFieldReturnList;



    public List<TableFieldReturn> getTableFieldReturnList() {
        return tableFieldReturnList;
    }

    public void setTableFieldReturnList(List<TableFieldReturn> tableFieldReturnList) {
        this.tableFieldReturnList = tableFieldReturnList;
    }


    public List<IndexReturn> getIndexReturnList() {
        return indexReturnList;
    }

    public void setIndexReturnList(List<IndexReturn> indexReturnList) {
        this.indexReturnList = indexReturnList;
    }

    public List<IndexFieldReturn> getIndexFieldReturnList() {
        return indexFieldReturnList;
    }

    public void setIndexFieldReturnList(List<IndexFieldReturn> indexFieldReturnList) {
        this.indexFieldReturnList = indexFieldReturnList;
    }

    public TableBase getTablebase() {
        return tablebase;
    }

    public void setTablebase(TableBase tablebase) {
        this.tablebase = tablebase;
    }

    public List<ForeignReturn> getForeignReturnReferences() {
        return foreignReturnReferences;
    }

    public void setForeignReturnReferences(List<ForeignReturn> foreignReturnReferences) {
        this.foreignReturnReferences = foreignReturnReferences;
    }
}
