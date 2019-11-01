package com.xquant.script.pojo.saveWithModuleName;

import com.xquant.dialectfunction.DialectFunction;

public class FunctionWithModuleName {
    private String moduleName;

    private DialectFunction dialectFunction;

    public DialectFunction getDialectFunction() {
        return dialectFunction;
    }

    public void setDialectFunction(DialectFunction dialectFunction) {
        this.dialectFunction = dialectFunction;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
