package com.xquant.script.pojo.module;

import com.xquant.database.config.procedure.Procedure;

public class ProcedureWithModuleName {
    private Procedure procedure;

    private String moduleName;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }
}
