package com.xquant.script.pojo.saveWithModuleName;

import com.xquant.database.config.trigger.Trigger;

public class TriggerWithModuleName {
    private String moduleName;

    private Trigger trigger;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }
}
