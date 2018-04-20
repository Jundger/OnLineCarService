package com.jundger.carservice.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 14246 on 2018/1/5.
 */

public class Article implements Serializable{

    // 文章标题
    private String title;

    // 文章来源
    @SerializedName("origin")
    private String from;

    // 发布时间
    @SerializedName("createTime")
    private String time;

    // 图片网络路径
    @SerializedName("picture")
    private String image;

    private String content;

    private Integer liked;

    public Article(String title, String from, String time, String image, String content, Integer liked) {
        this.title = title;
        this.from = from;
        this.time = time;
        this.image = image;
        this.content = content;
        this.liked = liked;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLiked() {
        return liked;
    }

    public void setLiked(Integer liked) {
        this.liked = liked;
    }
}
