package com.jundger.carservice.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Title: CarService
 * Date: Create in 2018/4/28 21:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class User implements Serializable {

    @SerializedName("phone_number")
    private String phone;

    private String email;

    private String nickname;

    private String portrait;

    @SerializedName("car_brand")
    private String brand;

    @SerializedName("car_id")
    private String brand_no;

    private String token;

    public User(String phone, String email, String nickname, String portrait, String brand, String brand_no, String token) {
        this.phone = phone;
        this.email = email;
        this.nickname = nickname;
        this.portrait = portrait;
        this.brand = brand;
        this.brand_no = brand_no;
        this.token = token;
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
