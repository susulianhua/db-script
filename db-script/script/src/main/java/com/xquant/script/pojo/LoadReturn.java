package com.xquant.script.pojo;

public class LoadReturn {
    boolean success;
    Object data;
    int total;

    public LoadReturn(Object data,int total){
        this.total = total;
        this.data = data;
        this.success = true;
    }
}
