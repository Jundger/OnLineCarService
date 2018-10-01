package com.jundger.work.constant;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/10/1 14:22
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public enum OrderStatusEnum {

    // 订单创建后等待接受
    WAITTING,

    // 订单已被接受，正在进行中
    RUNNING,

    // 订单结束
    FINISH
}
