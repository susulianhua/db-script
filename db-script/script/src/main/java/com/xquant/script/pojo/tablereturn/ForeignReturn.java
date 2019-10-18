package com.xquant.script.pojo.tablereturn;

public class ForeignReturn {
    private String key_name;
    private String main_table;
    private String foreign_field;
    private String reference_field;

    public ForeignReturn() {
    }

    public ForeignReturn(String key_name, String main_table, String foreign_field, String reference_field) {
        this.key_name = key_name;
        this.main_table = main_table;
        this.foreign_field = foreign_field;
        this.reference_field = reference_field;
    }

    public String getKey_name() {
        return key_name;
    }

    public void setKey_name(String key_name) {
        this.key_name = key_name;
    }

    public String getMain_table() {
        return main_table;
    }

    public void setMain_table(String main_table) {
        this.main_table = main_table;
    }

    public String getForeign_field() {
        return foreign_field;
    }

    public void setForeign_field(String foreign_field) {
        this.foreign_field = foreign_field;
    }

    public String getReference_field() {
        return reference_field;
    }

    public void setReference_field(String reference_field) {
        this.reference_field = reference_field;
    }
}
