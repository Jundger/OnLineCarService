package com.jundger.work.pojo.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jundger.work.pojo.Customer;
import com.jundger.work.pojo.FaultCode;
import com.jundger.work.pojo.Repairman;

import java.util.Date;
import java.util.List;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/9/29 15:49
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class OrderJson {

    private String orderNo;
    private List<FaultCode> faultCodeList;
    private String describe;
    private Customer customer;
    private Repairman repairman;
    private Double longitude;
    private Double latitude;
    private Date createTime;

    public OrderJson() {
    }

    public OrderJson(String orderNo, List<FaultCode> faultCodeList, String describe, Customer customer, Repairman repairman, Double longitude, Double latitude, Date createTime) {
        this.orderNo = orderNo;
        this.faultCodeList = faultCodeList;
        this.describe = describe;
        this.customer = customer;
        this.repairman = repairman;
        this.longitude = longitude;
        this.latitude = latitude;
        this.createTime = createTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<FaultCode> getFaultCodeList() {
        return faultCodeList;
    }

    public void setFaultCodeList(List<FaultCode> faultCodeList) {
        this.faultCodeList = faultCodeList;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Repairman getRepairman() {
        return repairman;
    }

    public void setRepairman(Repairman repairman) {
        this.repairman = repairman;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "OrderJson{" +
                "orderNo='" + orderNo + '\'' +
                ", faultCodeList=" + faultCodeList +
                ", describe='" + describe + '\'' +
                ", customer=" + customer +
                ", repairman=" + repairman +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", createTime=" + createTime +
                '}';
    }
}
