package com.jundger.work.serviceimpl;

import com.jundger.work.dao.CommentMapper;
import com.jundger.work.dao.RepairmanMapper;
import com.jundger.work.pojo.Repairman;
import com.jundger.work.service.RepairmanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/4/18 22:03
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Service("repairmanService")
public class RepairmanServiceImpl implements RepairmanService {

	@Resource
	private RepairmanMapper repairmanMapper;

	@Override
	public Repairman getById(Integer id) {
		return repairmanMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(Repairman repairman) {
		return this.repairmanMapper.updateByPrimaryKeySelective(repairman);
	}

	@Override
	public Repairman getByphoneNumber(String phoneNumber) {
		return this.repairmanMapper.selectByPhoneNumber(phoneNumber);
	}

	@Override
	public Repairman getByEmail(String email) {
		return repairmanMapper.selectByEmail(email);
	}

	@Override
	public Repairman validateLogin(String phoneNumber, String password) {
		return this.repairmanMapper.selectByPhonePsw(phoneNumber, password);
	}

	@Override
	public int addRepairman(Repairman repairman) {
		return this.repairmanMapper.insertSelective(repairman);
	}

}
