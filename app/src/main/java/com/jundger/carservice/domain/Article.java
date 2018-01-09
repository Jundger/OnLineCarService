package com.jundger.carservice.domain;

import java.io.Serializable;

/**
 * Created by 14246 on 2018/1/5.
 */

public class Article implements Serializable{
    // 图片资源ID
    private Integer imageId;

    // 文章标题
    private String title;

    // 文章来源
    private String from;

    // 发布时间
    private String time;

    public Article(Integer imageId, String title, String from) {
        this(imageId, title, from, "01-04 22:28");
    }

    public Article(Integer imageId, String title, String from, String time) {
        this.imageId = imageId;
        this.title = title;
        this.from = from;
        this.time = time;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
