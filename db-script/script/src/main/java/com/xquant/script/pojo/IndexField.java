package com.xquant.script.pojo;

public class IndexField {
    private String field;
    private String index_name;
    private String direction;

    public IndexField() {
    }

    public IndexField(String field, String index_name, String direction){
        this.direction = direction;
        this.index_name = index_name;
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getIndex_name() {
        return index_name;
    }

    public void setIndex_name(String index_name) {
        this.index_name = index_name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
