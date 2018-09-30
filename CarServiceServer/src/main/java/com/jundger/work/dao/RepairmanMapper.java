package com.jundger.work.dao;

import com.jundger.work.pojo.Repairman;
import org.apache.ibatis.annotations.Param;

public interface RepairmanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table repairman
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table repairman
     *
     * @mbggenerated
     */
    int insert(Repairman record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table repairman
     *
     * @mbggenerated
     */
    int insertSelective(Repairman record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table repairman
     *
     * @mbggenerated
     */
    Repairman selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table repairman
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Repairman record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table repairman
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Repairman record);

    Repairman selectByPhoneNumber(@Param("phone") String phoneNumber);

    Repairman selectByPhonePsw(@Param("phone") String phoneNumber, @Param("password") String password);

    Repairman selectByEmail(@Param("email") String email);
}