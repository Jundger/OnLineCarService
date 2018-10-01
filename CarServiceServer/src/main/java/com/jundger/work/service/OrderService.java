package com.jundger.work.service;

import com.jundger.work.pojo.Order;
import com.jundger.work.pojo.OrderCode;
import com.jundger.work.pojo.OrderNotify;
import com.jundger.work.pojo.json.OrderJson;

import java.util.List;

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

    // 更新订单
    int updateOrder(Order order);

    // 更新
    int updateOrderNotify(OrderNotify orderNotify);

    // 记录每个订单中包含的故障码
    int addOrderCode(List<OrderCode> list);

    // 记录每个订单发送的对象
    int addOrderNotify(List<OrderNotify> list);

    // 通过别名（即用户电话号码）向客户端推送通知
    Boolean pushNotify(List<String> alias, OrderJson extras);
}
