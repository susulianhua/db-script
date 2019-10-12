package com.xquant.script.pojo;

public class TableField {
    private boolean autoIncrease;

    private String standardFieldId;// 标准字段Id

    private boolean primary;

    private boolean unique;

    private boolean notNull;

    private String id;

    public TableField() {
    }

    public TableField(String standardFieldId, boolean primary, boolean unique, String id, boolean notNull, boolean autoIncrease){
        this.autoIncrease = autoIncrease;
        this.standardFieldId = standardFieldId;
        this.notNull = notNull;
        this.primary = primary;
        this.unique = unique;
        this.id = id;
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
    public boolean isAutoIncrease() {
        return autoIncrease;
    }

    public void setAutoIncrease(boolean autoIncrease) {
        this.autoIncrease = autoIncrease;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
