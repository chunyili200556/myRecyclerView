package com.example.chunyili.myrecyclerview.Model;

public class Magazine {
    String id;
    String title;
    String magazine_period;
    String image_url;
    String publish_time;
    String preface;

    public Magazine(String id, String title, String periods, String image_url, String publish_time, String preface) {
        this.id = id;
        this.title = title;
        this.magazine_period = periods;
        this.image_url = image_url;
        this.publish_time = publish_time;
        this.preface = preface;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPeriods() {
        return magazine_period;
    }

    public void setPeriods(String periods) {
        this.magazine_period = periods;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }

    public String getPreface() {
        return preface;
    }

    public void setPreface(String preface) {
        this.preface = preface;
    }
}
