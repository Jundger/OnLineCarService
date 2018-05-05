package com.jundger.work.service;

import com.jundger.work.pojo.Collect;
import com.jundger.work.pojo.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    // 通过ID查询用户信息
    Customer getById(int userId);

    // 通过ID更新用户信息
    int updateByPrimaryKeySelective(Customer customer);

    // 通过手机号查询用户信息
    Customer getByphoneNumber(String phoneNumber);

    // 通过邮箱查询用户信息
    Customer getByEmail(String email);

    // 通过手机号和密码验证登录信息
    Customer validateLogin(String phoneNumber, String password);

    // 生成Token验证字符串
    String generalToken(Customer customer, long ttlMillis);

    // 增加新用户
    int addCustomer(Customer customer);

    // 拉取用户评论
    List<Map<String, Object>> getCommentByCust(String id);

    // 增加新收藏
    int addCollect(Collect record);

    // 获取用户所有收藏
    List<Collect> getAllCollect(Integer cust_id);
}
