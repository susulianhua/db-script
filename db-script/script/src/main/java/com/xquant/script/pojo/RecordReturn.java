package com.xquant.script.pojo;

import java.text.SimpleDateFormat;

public class RecordReturn {
    private int id;
    private int book_id;
    private String book_name;
    private String record_date;
    private String back_date;
    private String sno;
    private int state;

    public RecordReturn(RecordResult recordResult){
        this.id = recordResult.getId();
        this.book_id = recordResult.getBook_id();
        this.book_name = recordResult.getBook_name();
        this.sno = recordResult.getSno();
        this.state = recordResult.getState();
        SimpleDateFormat date2 = new SimpleDateFormat("yyyy/MM/dd");
        this.record_date = date2.format(recordResult.getRecord_date());
        this.back_date = date2.format(recordResult.getBack_date());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public String getBack_date() {
        return back_date;
    }

    public void setBack_date(String back_date) {
        this.back_date = back_date;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
