package com.jundger.work.service;

import com.jundger.work.pojo.Order;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/9/29 15:23
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public interface OrderService {

    // 添加订单
    int addOrder(Order order);
}
