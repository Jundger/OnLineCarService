package com.jundger.carservice.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by 14246 on 2018/1/5.
 */

public class ServicePoint implements Serializable {

    private String id;

    // 服务点名称
    private String name;

    // 服务点评价分数
    private Float score;

    // 评价数
    @SerializedName("count")
    private Integer evaluationCount;

    // 地址
    private String address;

    // 图片网络地址
    @SerializedName("picture")
    private String image;

    // 距离
    private Float distance;

    public ServicePoint(String id, String name, Float score, Integer evaluationCount, String address, String image, Float distance) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.evaluationCount = evaluationCount;
        this.address = address;
        this.image = image;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getEvaluationCount() {
        return evaluationCount;
    }

    public void setEvaluationCount(Integer evaluationCount) {
        this.evaluationCount = evaluationCount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
