package com.jundger.work.service;

import com.jundger.work.pojo.Customer;
import com.jundger.work.pojo.FaultCode;

import java.util.List;

public interface CustomerService {
    // 通过ID查询用户信息
    Customer getById(int userId);

    // 通过ID更新用户信息
    int updateByPrimaryKeySelective(Customer customer);

    // 通过手机号查询用户信息
    Customer getByphoneNumber(String phoneNumber);

    // 通过手机号和密码验证登录信息
    Customer validateLogin(String phoneNumber, String password);

    // 生成Token验证字符串
    String generalToken(Customer customer, long ttlMillis);

    // 注册用户
    int register(Customer customer);

    // 根据故障码和汽车型号查询故障信息
    List<FaultCode> queryFaultCode(List<String> codes, String brand);
}
