package com.xquant.script.pojo.tablereturn;

public class FieldReturn {

    private String standardFieldId;// 标准字段Id

    private boolean primary;

    private boolean unique;

    private boolean notNull;

    private String tableFieldId;

    public FieldReturn(String standardFieldId, boolean primary, boolean unique,
                       boolean notNull, String tableFieldId){
        this.standardFieldId = standardFieldId;
        this.primary = primary;
        this.unique = unique;
        this.notNull = notNull;
        this.tableFieldId =tableFieldId;
    }

    public String getStandardFieldId() {
        return standardFieldId;
    }

    public void setStandardFieldId(String standardFieldId) {
        this.standardFieldId = standardFieldId;
    }

    public boolean getPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public boolean getUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    public String getTableFieldId() {
        return tableFieldId;
    }

    public void setTableFieldId(String tableFieldId) {
        this.tableFieldId = tableFieldId;
    }
}


