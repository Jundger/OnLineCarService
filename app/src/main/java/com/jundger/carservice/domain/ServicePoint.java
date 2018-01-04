package com.jundger.carservice.domain;

/**
 * Created by 14246 on 2018/1/5.
 */

public class ServicePoint {
    // 图片资源ID
    private Integer imageId;

    // 服务点名称
    private String name;

    // 服务点评价分数
    private Float score;

    // 评价数
    private Integer evaluationCount;

    // 地址
    private String address;

    // 距离
    private Float distance;

    // 服务点类型（汽车维修、汽车配件）
    private String type;

    public ServicePoint(Integer imageId, String name, Float score, Integer evaluationCount, String address, Float distance) {
        this(imageId, name, score, evaluationCount, address, distance, "汽车维修");
    }

    public ServicePoint(Integer imageId, String name, Float score, Integer evaluationCount, String address, Float distance, String type) {
        this.imageId = imageId;
        this.name = name;
        this.score = score;
        this.evaluationCount = evaluationCount;
        this.address = address;
        this.distance = distance;
        this.type = type;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
