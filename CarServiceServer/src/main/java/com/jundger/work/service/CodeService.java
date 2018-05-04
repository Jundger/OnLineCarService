package com.jundger.work.service;

import com.jundger.work.pojo.FaultCode;

import java.util.List;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/5/3 22:57
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public interface CodeService {

	// 根据故障码和汽车型号查询故障信息
	List<FaultCode> queryFaultCode(List<String> codes, String brand);
}
