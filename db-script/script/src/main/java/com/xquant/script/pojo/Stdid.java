package com.xquant.script.pojo;

public class Stdid {
    private String standardFieldId;
    private String name;

    public Stdid(String standardFieldId, String name){
        this.standardFieldId = standardFieldId;
        this.name = name;
    }

    public String getStandardFieldId() {
        return standardFieldId;
    }

    public void setStandardFieldId(String standardFieldId) {
        this.standardFieldId = standardFieldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
