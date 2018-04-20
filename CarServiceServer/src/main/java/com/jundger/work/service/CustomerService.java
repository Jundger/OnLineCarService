package com.jundger.work.service;

import com.jundger.work.pojo.Customer;

public interface CustomerService {
    // 通过ID查询用户信息
    Customer getById(int userId);

    // 通过ID更新用户信息
    int updateByPrimaryKeySelective(Customer customer);

    // 通过手机号查询用户信息
    Customer getByphoneName(String phoneNumber);

    // 通过手机号和密码验证登录信息
    Customer validateLogin(String phoneNumber, String password);

    // 生成Token验证字符串
    String generalToken(Customer customer, long ttlMillis);

    // 注册用户
    int register(Customer customer);
}
