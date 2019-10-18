package com.xquant.script.pojo.tablereturn;

public class IndexReturn {
    private String index_name;
    private String index_unique;
    private String index_description;

    public IndexReturn(){}

    public IndexReturn(String index_name, String index_unique, String index_description){
        this.index_name = index_name;
        this.index_unique = index_unique;
        this.index_description = index_description;
    }

    public String getIndex_name() {
        return index_name;
    }

    public void setIndex_name(String index_name) {
        this.index_name = index_name;
    }

    public String getIndex_unique() {
        return index_unique;
    }

    public void setIndex_unique(String index_unique) {
        this.index_unique = index_unique;
    }

    public String getIndex_description() {
        return index_description;
    }

    public void setIndex_description(String index_description) {
        this.index_description = index_description;
    }
}
