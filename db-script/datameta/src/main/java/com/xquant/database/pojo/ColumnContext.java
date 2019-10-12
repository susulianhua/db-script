/**
 * Copyright (c) 2012-2017, www.tinygroup.org (luo_guo@icloud.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xquant.database.pojo;

/**
 * Created by wangwy on 2017/3/1.
 */
public class ColumnContext {

    private String tableDataType;

    private String standardFieldName;

    private String dbColumnType;

    private String remarks;

    private String standardComment;

    private boolean dbNullAble;

    private boolean fieldNotNull;

    private String standardDefault;

    private String dbColumnDef;

    private String tableName;

    private String TableNameWithSchema;

    private String dbDataType;

    public String getTableDataType() {
        return tableDataType;
    }

    public void setTableDataType(String tableDataType) {
        this.tableDataType = tableDataType;
    }

    public String getStandardFieldName() {
        return standardFieldName;
    }

    public void setStandardFieldName(String standardFieldName) {
        this.standardFieldName = standardFieldName;
    }

    public String getDbColumnType() {
        return dbColumnType;
    }

    public void setDbColumnType(String dbColumnType) {
        this.dbColumnType = dbColumnType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStandardComment() {
        return standardComment;
    }

    public void setStandardComment(String standardComment) {
        this.standardComment = standardComment;
    }

    public boolean isDbNullAble() {
        return dbNullAble;
    }

    public void setDbNullAble(boolean dbNullAble) {
        this.dbNullAble = dbNullAble;
    }

    public boolean isFieldNotNull() {
        return fieldNotNull;
    }

    public void setFieldNotNull(boolean fieldNotNull) {
        this.fieldNotNull = fieldNotNull;
    }

    public String getStandardDefault() {
        return standardDefault;
    }

    public void setStandardDefault(String standardDefault) {
        this.standardDefault = standardDefault;
    }

    public String getDbColumnDef() {
        return dbColumnDef;
    }

    public void setDbColumnDef(String dbColumnDef) {
        this.dbColumnDef = dbColumnDef;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDbDataType() {
        return dbDataType;
    }

    public void setDbDataType(String dbDataType) {
        this.dbDataType = dbDataType;
    }

    public String getTableNameWithSchema() {
        return TableNameWithSchema;
    }

    public void setTableNameWithSchema(String tableNameWithSchema) {
        TableNameWithSchema = tableNameWithSchema;
    }
}
