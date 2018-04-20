package com.jundger.carservice.pojo;

/**
 * Created by 14246 on 2018/1/4.
 */

public class FaultInfo {

    // 故障码
    private String code;

    // 部件系统
    private String system;

    // 适用范围
    private String scope;

    // 描述
    private String describe;

    public FaultInfo() {
    }

    public FaultInfo(String code, String system, String scope, String describe) {
        this.code = code;
        this.system = system;
        this.scope = scope;
        this.describe = describe;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
