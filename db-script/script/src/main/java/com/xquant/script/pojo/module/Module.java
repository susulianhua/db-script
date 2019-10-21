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
    @XStreamAlias("procedure")
    private String procedure;
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

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
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
}
