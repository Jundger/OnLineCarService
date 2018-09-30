package com.jundger.work.serviceimpl;

import com.jundger.work.dao.OrderMapper;
import com.jundger.work.pojo.Order;
import com.jundger.work.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/9/29 15:24
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public int addOrder(Order order) {
        return this.orderMapper.insertSelective(order);
    }
}
