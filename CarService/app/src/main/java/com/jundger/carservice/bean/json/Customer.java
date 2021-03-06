package com.jundger.carservice.bean.json;

import com.jundger.carservice.bean.User;

import java.io.Serializable;
import java.util.Date;

public class Customer implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.cust_id
     *
     * @mbggenerated
     */
    private Integer custId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.cust_phone
     *
     * @mbggenerated
     */
    private String custPhone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.cust_password
     *
     * @mbggenerated
     */
    private String custPassword;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.cust_email
     *
     * @mbggenerated
     */
    private String custEmail;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.cust_name
     *
     * @mbggenerated
     */
    private String custName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.car_brand
     *
     * @mbggenerated
     */
    private String carBrand;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.car_id
     *
     * @mbggenerated
     */
    private String carId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.cust_portrait
     *
     * @mbggenerated
     */
    private String custPortrait;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.login_ip
     *
     * @mbggenerated
     */
    private String loginIp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.taken
     *
     * @mbggenerated
     */
    private String taken;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.verification
     *
     * @mbggenerated
     */
    private String verification;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column customer.other
     *
     * @mbggenerated
     */
    private String other;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer
     *
     * @mbggenerated
     */
    public Customer(Integer custId, String custPhone, String custPassword, String custEmail, String custName, String carBrand, String carId, String custPortrait, Date createTime, Date updateTime, String loginIp, String taken, String verification, String other) {
        this.custId = custId;
        this.custPhone = custPhone;
        this.custPassword = custPassword;
        this.custEmail = custEmail;
        this.custName = custName;
        this.carBrand = carBrand;
        this.carId = carId;
        this.custPortrait = custPortrait;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.loginIp = loginIp;
        this.taken = taken;
        this.verification = verification;
        this.other = other;
    }

    public Customer(User user) {

        this.custId = user.getCustId();
        this.custPhone = user.getPhone();
        this.custEmail = user.getEmail();
        this.custName = user.getNickname();
        this.carBrand = user.getBrand();
        this.carId = user.getBrand_no();
        this.custPortrait = user.getPortrait();
        this.taken = user.getToken();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table customer
     *
     * @mbggenerated
     */
    public Customer() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.cust_id
     *
     * @return the value of customer.cust_id
     *
     * @mbggenerated
     */
    public Integer getCustId() {
        return custId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.cust_id
     *
     * @param custId the value for customer.cust_id
     *
     * @mbggenerated
     */
    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.cust_phone
     *
     * @return the value of customer.cust_phone
     *
     * @mbggenerated
     */
    public String getCustPhone() {
        return custPhone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.cust_phone
     *
     * @param custPhone the value for customer.cust_phone
     *
     * @mbggenerated
     */
    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone == null ? null : custPhone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.cust_password
     *
     * @return the value of customer.cust_password
     *
     * @mbggenerated
     */
    public String getCustPassword() {
        return custPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.cust_password
     *
     * @param custPassword the value for customer.cust_password
     *
     * @mbggenerated
     */
    public void setCustPassword(String custPassword) {
        this.custPassword = custPassword == null ? null : custPassword.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.cust_email
     *
     * @return the value of customer.cust_email
     *
     * @mbggenerated
     */
    public String getCustEmail() {
        return custEmail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.cust_email
     *
     * @param custEmail the value for customer.cust_email
     *
     * @mbggenerated
     */
    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail == null ? null : custEmail.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.cust_name
     *
     * @return the value of customer.cust_name
     *
     * @mbggenerated
     */
    public String getCustName() {
        return custName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.cust_name
     *
     * @param custName the value for customer.cust_name
     *
     * @mbggenerated
     */
    public void setCustName(String custName) {
        this.custName = custName == null ? null : custName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.car_brand
     *
     * @return the value of customer.car_brand
     *
     * @mbggenerated
     */
    public String getCarBrand() {
        return carBrand;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.car_brand
     *
     * @param carBrand the value for customer.car_brand
     *
     * @mbggenerated
     */
    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand == null ? null : carBrand.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.car_id
     *
     * @return the value of customer.car_id
     *
     * @mbggenerated
     */
    public String getCarId() {
        return carId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.car_id
     *
     * @param carId the value for customer.car_id
     *
     * @mbggenerated
     */
    public void setCarId(String carId) {
        this.carId = carId == null ? null : carId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.cust_portrait
     *
     * @return the value of customer.cust_portrait
     *
     * @mbggenerated
     */
    public String getCustPortrait() {
        return custPortrait;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.cust_portrait
     *
     * @param custPortrait the value for customer.cust_portrait
     *
     * @mbggenerated
     */
    public void setCustPortrait(String custPortrait) {
        this.custPortrait = custPortrait == null ? null : custPortrait.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.create_time
     *
     * @return the value of customer.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.create_time
     *
     * @param createTime the value for customer.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.update_time
     *
     * @return the value of customer.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.update_time
     *
     * @param updateTime the value for customer.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.login_ip
     *
     * @return the value of customer.login_ip
     *
     * @mbggenerated
     */
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.login_ip
     *
     * @param loginIp the value for customer.login_ip
     *
     * @mbggenerated
     */
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp == null ? null : loginIp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.taken
     *
     * @return the value of customer.taken
     *
     * @mbggenerated
     */
    public String getTaken() {
        return taken;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.taken
     *
     * @param taken the value for customer.taken
     *
     * @mbggenerated
     */
    public void setTaken(String taken) {
        this.taken = taken == null ? null : taken.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.verification
     *
     * @return the value of customer.verification
     *
     * @mbggenerated
     */
    public String getVerification() {
        return verification;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.verification
     *
     * @param verification the value for customer.verification
     *
     * @mbggenerated
     */
    public void setVerification(String verification) {
        this.verification = verification == null ? null : verification.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column customer.other
     *
     * @return the value of customer.other
     *
     * @mbggenerated
     */
    public String getOther() {
        return other;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column customer.other
     *
     * @param other the value for customer.other
     *
     * @mbggenerated
     */
    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
    }
}