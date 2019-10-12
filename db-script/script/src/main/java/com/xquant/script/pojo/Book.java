package com.xquant.script.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


public class Book {
    private int book_id;
    private int state;
    private String book_name;
    private String description;
    private String author;
    private int price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publish_time;




    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String getauthor() {
        return author;
    }

    public void setauthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(Date publish_time) {
        this.publish_time = publish_time;
    }

    @Override
    public String toString(){
        return "Book{" +
                "Sno= " + book_id +
                ", 书名=" + book_name +
                ", 作者=" + author +
                ", 价格=" + price +
                ", 描述=" + description +
                ", 出版日期=" + publish_time + "}\n";

    }
}
