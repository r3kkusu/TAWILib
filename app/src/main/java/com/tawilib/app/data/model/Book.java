package com.tawilib.app.data.model;

public class Book {

    private String id;
    private String title;
    private String author;
    private String about;
    private String date;
    private String cover;

    public Book(String title,
                String author,
                String about,
                String date,
                String cover
    ) {
        this.title = title;
        this.author = author;
        this.about = about;
        this.date = date;
        this.cover = cover;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAbout() {
        return about;
    }

    public String getDate() {
        return date;
    }

    public String getCover() {
        return cover;
    }
}
