package com.xquant.script.pojo.MetaDataReturn;

public class SqlResult {
    private String databaseObject;
    private String databaseType;
    private String differentType;
    private String differentDetail;

    public String getDatabaseObject() {
        return databaseObject;
    }

    public void setDatabaseObject(String databaseObject) {
        this.databaseObject = databaseObject;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDifferentType() {
        return differentType;
    }

    public void setDifferentType(String differentType) {
        this.differentType = differentType;
    }

    public String getDifferentDetail() {
        return differentDetail;
    }

    public void setDifferentDetail(String differentDetail) {
        this.differentDetail = differentDetail;
    }
}
