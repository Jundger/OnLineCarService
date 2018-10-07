package com.jundger.work.dao;

import com.jundger.work.pojo.FaultCode;
import com.jundger.work.pojo.OrderCode;

import java.util.List;

public interface OrderCodeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_code
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_code
     *
     * @mbggenerated
     */
    int insert(OrderCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_code
     *
     * @mbggenerated
     */
    int insertSelective(OrderCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_code
     *
     * @mbggenerated
     */
    OrderCode selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_code
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(OrderCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table order_code
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(OrderCode record);

    /**
     * 一次性插入多条数据
     * @param list 数据集合
     * @return
     */
    int insertList(List<OrderCode> list);
}