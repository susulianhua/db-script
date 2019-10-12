package com.xquant.script.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookReturn {
    private int price;
    private String book_name;
    private int book_id;
    private String publish_time;
    private String description;
    private String author;
    private int state;

    public BookReturn(Book book){
        this.book_id = book.getBook_id();
        this.author = book.getauthor();
        this.book_name = book.getBook_name();
        this.description = book.getdescription();
        this.price = book.getPrice();
        this.state = book.getState();
        Date date1 = book.getPublish_time();
        SimpleDateFormat date2 = new SimpleDateFormat("yyyy/MM/dd");
        this.publish_time = date2.format(date1);

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
