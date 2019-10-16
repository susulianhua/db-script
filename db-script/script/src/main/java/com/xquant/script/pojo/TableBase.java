package com.xquant.script.pojo;

public class TableBase {
    private String fileName;
    private String package_name;
    private String table_id;
    private String table_name;
    private String table_title;
    private String table_description;

    public TableBase() {
    }

    public TableBase(String fileName, String package_name, String table_id){
        this.fileName = fileName;
        this.package_name = package_name;
        this.table_id = table_id;
    }

    public TableBase(String fileName, String package_name, String table_id, String table_name,
                     String table_title, String table_description){
        this.fileName = fileName;
        this.package_name = package_name;
        this.table_id = table_id;
        this.table_name = table_name;
        this.table_title = table_title;
        this.table_description = table_description;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getTable_title() {
        return table_title;
    }

    public void setTable_title(String table_title) {
        this.table_title = table_title;
    }

    public String getTable_description() {
        return table_description;
    }

    public void setTable_description(String table_description) {
        this.table_description = table_description;
    }
}
