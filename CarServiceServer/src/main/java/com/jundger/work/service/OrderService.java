package com.jundger.work.service;

import com.jundger.work.pojo.FaultCode;
import com.jundger.work.pojo.Order;
import com.jundger.work.pojo.OrderCode;
import com.jundger.work.pojo.OrderNotify;
import com.jundger.work.pojo.json.OrderJson;

import java.util.List;
import java.util.Map;

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

    // 通知订单号查询订单
    Order selectByOrderNo(String orderNo);

    // 通过别名（即用户电话号码）向客户端推送通知（OrderJson格式）
    Map<String, Object> pushNotifyOrder(List<String> alias, OrderJson extras, String title, String alert);

    // 通过别名（即用户电话号码）向客户端推送通知
    Map<String, Object> pushNotify(List<String> alias, Map<String, Object> extras, String title, String alert);

    // 通过别名（即用户电话号码）向客户端推送自定义消息（OrderJson格式）
    Map<String, Object> pushCustomMsgOrder(List<String> alias, OrderJson extras, String title);

    // 通过别名（即用户电话号码）向客户端推送自定义消息
    Map<String, Object> pushCustomMsg(List<String> alias, Map<String, Object> extras, String title);

    // 提取故障码集合中的描述信息组合成一个字符串
    String FormDescribeStr(List<FaultCode> list);

    // 提取故障码集合中的故障码组合成一个字符串
    String FormFaultCodeStr(List<FaultCode> list);

    // 根据订单号查询订单
    List<Map<String, Object>> queryOrderByOrderNo(String orderNo);

    // 根据维修人员id查询所有已结束的历史订单
    List<Map<String, Object>> queryHistoryOrderByReprmId(Integer id);

    // 根据普通用户id查询所有订单
    List<Order> queryAllOrderByCustomerId(Integer id);

    // 根据订单id查询其所包含的故障信息
    List<FaultCode> getCodeOrderByOrderId(Integer id);
}
