package com.xquant.script.pojo;

import java.util.List;

public class TableReturn {

    private TableBase tablebase;
    private List<TableField> tableFieldList;
    private List<Foreign> foreignReferences;
    private List<Index> indexList;
    private List<IndexField> indexFieldList;



    public List<TableField> getTableFieldList() {
        return tableFieldList;
    }

    public void setTableFieldList(List<TableField> tableFieldList) {
        this.tableFieldList = tableFieldList;
    }


    public List<Index> getIndexList() {
        return indexList;
    }

    public void setIndexList(List<Index> indexList) {
        this.indexList = indexList;
    }

    public List<IndexField> getIndexFieldList() {
        return indexFieldList;
    }

    public void setIndexFieldList(List<IndexField> indexFieldList) {
        this.indexFieldList = indexFieldList;
    }

    public TableBase getTablebase() {
        return tablebase;
    }

    public void setTablebase(TableBase tablebase) {
        this.tablebase = tablebase;
    }

    public List<Foreign> getForeignReferences() {
        return foreignReferences;
    }

    public void setForeignReferences(List<Foreign> foreignReferences) {
        this.foreignReferences = foreignReferences;
    }
}
