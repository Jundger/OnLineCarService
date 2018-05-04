package com.jundger.work.serviceimpl;

import com.jundger.work.dao.FaultCodeMapper;
import com.jundger.work.pojo.FaultCode;
import com.jundger.work.service.CodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/5/3 22:57
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Service("codeService")
public class CodeServiceImpl implements CodeService {

	@Resource
	private FaultCodeMapper faultCodeMapper;

	@Override
	public List<FaultCode> queryFaultCode(List<String> codes, String brand) {
		return faultCodeMapper.selectByCode(codes, brand);
	}
}
