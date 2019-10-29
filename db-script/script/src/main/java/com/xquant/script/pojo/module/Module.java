package com.xquant.script.pojo.module;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("module")
public class Module {

    @XStreamAlias("tables")
    private List<TableName> tablelist;
    @XStreamAsAttribute
    private String id;
    @XStreamAlias("views")
    private List<ViewName> viewNameList;
    @XStreamAlias("triggers")
    private List<TriggerName> triggerNameList;
    @XStreamAlias("standardfield")
    private String standardfield;
    @XStreamAlias("businessType")
    private String businessType;
    @XStreamAlias("procedures")
    private List<ProcedureNameInModule> procedureNameInModuleList;
    @XStreamAlias("sequence")
    private String sequence;

    public String getStandardfield() {
        return standardfield;
    }

    public void setStandardfield(String standardfield) {
        this.standardfield = standardfield;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TableName> getTablelist() {
        return tablelist;
    }

    public void setTablelist(List<TableName> tablelist) {
        this.tablelist = tablelist;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public List<ProcedureNameInModule> getProcedureNameInModuleList() {
        return procedureNameInModuleList;
    }

    public void setProcedureNameInModuleList(List<ProcedureNameInModule> procedureNameInModuleList) {
        this.procedureNameInModuleList = procedureNameInModuleList;
    }

    public List<ViewName> getViewNameList() {
        return viewNameList;
    }

    public void setViewNameList(List<ViewName> viewNameList) {
        this.viewNameList = viewNameList;
    }

    public List<TriggerName> getTriggerNameList() {
        return triggerNameList;
    }

    public void setTriggerNameList(List<TriggerName> triggerNameList) {
        this.triggerNameList = triggerNameList;
    }
}
