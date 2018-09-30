package com.jundger.work.service;

import com.jundger.work.pojo.Repairman;

import java.util.List;
import java.util.Map;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/4/18 22:02
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public interface RepairmanService {

	// 通过ID查询用户信息
	Repairman getById(Integer id);

	// 通过ID更新用户信息
	int updateByPrimaryKeySelective(Repairman repairman);

	// 通过手机号查询用户信息
	Repairman getByphoneNumber(String phoneNumber);

	// 通过邮箱查询用户信息
	Repairman getByEmail(String email);

	// 通过手机号和密码验证登录信息
	Repairman validateLogin(String phoneNumber, String password);

	// 增加新用户
	int addRepairman(Repairman repairman);
}
