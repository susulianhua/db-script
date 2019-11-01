package com.xquant.script.pojo.saveWithModuleName;

import com.xquant.database.config.sequence.Sequence;

public class SequenceWithModuleName {
    private String moduleName;
    private Sequence sequence;


    public Sequence getSequence() {
        return sequence;
    }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
