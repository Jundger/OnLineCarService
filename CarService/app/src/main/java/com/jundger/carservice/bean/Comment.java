package com.jundger.carservice.bean;

/**
 * Title: CarService
 * Date: Create in 2018/5/2 15:59
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class Comment {

    private String id;

    private String username;

    private String portrait;

    private Float score;

    private String content;

    private String time;

    public Comment(String id, String username, String portrait, Float score, String content, String time) {
        this.id = id;
        this.username = username;
        this.portrait = portrait;
        this.score = score;
        this.content = content;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
