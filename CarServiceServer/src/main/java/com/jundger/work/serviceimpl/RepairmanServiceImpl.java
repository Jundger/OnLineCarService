package com.jundger.work.serviceimpl;

import com.jundger.work.dao.RepairmanMapper;
import com.jundger.work.pojo.Repairman;
import com.jundger.work.service.RepairmanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
