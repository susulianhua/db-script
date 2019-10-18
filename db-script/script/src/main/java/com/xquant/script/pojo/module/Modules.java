package com.xquant.script.pojo.module;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.xquant.script.pojo.module.Module;

import java.util.List;

@XStreamAlias("modules")
public class Modules {
    @XStreamImplicit
    private List<Module> moduleList;// 列列表

    public List<Module> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<Module> moduleList) {
        this.moduleList = moduleList;
    }
}
