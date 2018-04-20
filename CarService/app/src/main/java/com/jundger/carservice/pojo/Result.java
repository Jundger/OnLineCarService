package com.jundger.carservice.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 14246 on 2018/4/19.
 */

public class Result<T> {

    private String code;

    private String msg;

    private List<T> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
