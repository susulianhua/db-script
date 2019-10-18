package com.xquant.script.pojo.module;

import java.util.List;

public class Child {
    private String text;

    private List<Child> children;

    private boolean leaf;

    public Child(){}

    public Child(String text, boolean leaf){
        this.text = text;
        this.leaf = leaf;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

}
