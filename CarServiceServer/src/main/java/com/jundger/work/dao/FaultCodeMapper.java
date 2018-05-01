package com.jundger.work.dao;

import com.jundger.work.pojo.FaultCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FaultCodeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fault_code
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fault_code
     *
     * @mbggenerated
     */
    int insert(FaultCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fault_code
     *
     * @mbggenerated
     */
    int insertSelective(FaultCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fault_code
     *
     * @mbggenerated
     */
    FaultCode selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fault_code
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(FaultCode record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table fault_code
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(FaultCode record);

    List<FaultCode> selectByCode(@Param("codes") List<String> codes, @Param("brand") String brand);
}