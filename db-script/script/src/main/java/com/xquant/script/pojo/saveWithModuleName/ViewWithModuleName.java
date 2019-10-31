package com.xquant.script.pojo.saveWithModuleName;

import com.xquant.database.config.view.View;

public class ViewWithModuleName {
    private View view;

    private String moduleName;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
