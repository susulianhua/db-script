package com.xquant.script.pojo.module;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("module")
public class Module {

    @XStreamImplicit
    private List<TableName> tablelist;
    @XStreamAsAttribute
    private String id;
    @XStreamAlias("view")
    private String view;
    @XStreamAlias("standardfield")
    private String standardfield;
    @XStreamAlias("businessType")
    private String businessType;

    @XStreamImplicit
    private List<ProcedureNameInModule> procedureNameInModuleList;

    @XStreamAlias("trigger")
    private String trigger;
    @XStreamAlias("sequence")
    private String sequence;


    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getStandardfield() {
        return standardfield;
    }

    public void setStandardfield(String standardfield) {
        this.standardfield = standardfield;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
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
}
