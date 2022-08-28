package com.tawilib.app.data.model;

import java.util.List;

public class BookCollection {

    private String key;
    private List<Book> bookList;

    public BookCollection(String key, List<Book> bookList) {
        this.key = key;
        this.bookList = bookList;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public String getKey() {
        return key;
    }

    public List<Book> getBookList() {
        return bookList;
    }
}
