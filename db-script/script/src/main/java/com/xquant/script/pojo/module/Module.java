package com.xquant.script.pojo.module;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("module")
public class Module {

    @XStreamAsAttribute
    private String id;

    @XStreamAlias("tables")
    private List<TableName> tablelist;
    @XStreamAlias("views")
    private List<ViewName> viewNameList;
    @XStreamAlias("triggers")
    private List<TriggerName> triggerNameList;
    @XStreamAlias("procedures")
    private List<ProcedureName> procedureNameList;
    @XStreamAlias("sequences")
    private List<SequenceName> sequenceNameList;

    @XStreamAlias("standardfield")
    private String standardfield;
    @XStreamAlias("businessType")
    private String businessType;
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


    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public List<ProcedureName> getProcedureNameList() {
        return procedureNameList;
    }

    public void setProcedureNameList(List<ProcedureName> procedureNameList) {
        this.procedureNameList = procedureNameList;
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

    public List<SequenceName> getSequenceNameList() {
        return sequenceNameList;
    }

    public void setSequenceNameList(List<SequenceName> sequenceNameList) {
        this.sequenceNameList = sequenceNameList;
    }

    public void setList(String fileName){
        if(fileName.equals("table")) this.setTablelist(new ArrayList<TableName>());
        else if(fileName.equals("trigger")) this.setTriggerNameList(new ArrayList<TriggerName>());
        else if(fileName.equals("view")) this.setViewNameList(new ArrayList<ViewName>());
        else if(fileName.equals("procedure")) this.setProcedureNameList(new ArrayList<ProcedureName>());
        else this.setSequenceNameList(new ArrayList<SequenceName>());
    }

}
