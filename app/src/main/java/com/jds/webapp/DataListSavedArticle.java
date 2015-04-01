package com.jds.webapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class DataListSavedArticle extends RealmObject {

    @PrimaryKey
    private String id;
    private String key;
    private String title;
    private String date;
    private String author;
    private String pv;
    private String thumbnail;

    public DataListSavedArticle(){

    }
    public DataListSavedArticle(String id, String key, String title, String date, String author, String pv, String thumbnail) {
        this.id = id;
        this.key = key;
        this.title = title;
        this.date = date;
        this.author = author;
        this.pv = pv;
        this.thumbnail = thumbnail;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
