package com.jundger.carservice.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Title: CarService
 * Date: Create in 2018/4/28 21:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class User extends DataSupport implements Serializable {

    private int custId;

    @SerializedName("custPhone")
    private String phone;

    @SerializedName("custEmail")
    private String email;

    @SerializedName("custName")
    private String nickname;

    @SerializedName("custPortrait")
    private String portrait;

    @SerializedName("carBrand")
    private String brand;

    @SerializedName("carId")
    private String brand_no;

    private String token;

    public User() {
    }

    public User(int custId, String phone, String email, String nickname, String portrait, String brand, String brand_no, String token) {
        this.custId = custId;
        this.phone = phone;
        this.email = email;
        this.nickname = nickname;
        this.portrait = portrait;
        this.brand = brand;
        this.brand_no = brand_no;
        this.token = token;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand_no() {
        return brand_no;
    }

    public void setBrand_no(String brand_no) {
        this.brand_no = brand_no;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
