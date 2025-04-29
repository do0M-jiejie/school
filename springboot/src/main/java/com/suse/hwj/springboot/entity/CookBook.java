package com.suse.hwj.springboot.entity;

public class CookBook {
    private String title;
    private String cookBookImage;
    private String publishTime;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCookBookImage() {
        return cookBookImage;
    }

    public void setCookBookImage(String cookBookImage) {
        this.cookBookImage = cookBookImage;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
