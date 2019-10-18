package com.xquant.script.pojo.module;

import java.util.ArrayList;
import java.util.List;

public class TreeReturn {
    private String text;
    public List<Child> children;
    private boolean leaf;

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

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
