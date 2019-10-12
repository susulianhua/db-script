package com.xquant.script.pojo;

import java.util.Date;


public class Record {
    private int id;
    private String sno;
    private int book_id;
    private Date record_date;
    private Date back_date;
    private int state;

    public Record(int id, String Sno,int book_id, Date record_date, Date back_date, int state){
        this.id = id;
        this.sno = Sno;
        this.book_id = book_id;
        this.record_date = record_date;
        this.back_date = back_date;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public Date getRecord_date() {
        return record_date;
    }

    public void setRecord_date(Date record_date) {
        this.record_date = record_date;
    }

    public Date getBack_date() {
        return back_date;
    }

    public void setBack_date(Date back_date) {
        this.back_date = back_date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString(){
        return "Record{" +
                "id= " + id +
                ", Sno=" + sno +
                ", book_id=" + book_id +
                ", 借阅时间=" + record_date +
                ", 归还时间=" + back_date +
                ", 归还状态=" + state + "}\n";

    }
}
