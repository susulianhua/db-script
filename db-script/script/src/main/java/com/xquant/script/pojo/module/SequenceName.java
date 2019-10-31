package com.xquant.script.pojo.module;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("sequence")
public class SequenceName {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
