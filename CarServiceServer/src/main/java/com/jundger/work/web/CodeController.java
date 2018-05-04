package com.jundger.work.web;

import com.jundger.work.pojo.FaultCode;
import com.jundger.work.service.CodeService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/5/3 22:55
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

@Controller
@RequestMapping(value = "/code")
public class CodeController {

	@Resource
	private CodeService codeService;

	private static Logger logger = Logger.getLogger(CustomerController.class);

	@ResponseBody
	@RequestMapping(value = "/queryCode", method = RequestMethod.POST)
	public Map<String, Object> queryFaultMsg(@RequestBody Map<String, Object> json) {

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		String brand = (String) json.get("brand");
		List<String> codes = (List<String>) json.get("code");
		logger.info("brand-->" + brand);
		for (String str : codes) {
			logger.info("code-->" + str);
		}

		if (codes.isEmpty()) {
			returnMsg.put("code", "0");
			returnMsg.put("msg", "CODE_IS_EMPTY");
		}

		try {
			List<FaultCode> faultCode = codeService.queryFaultCode(codes, brand);
			if (null == faultCode) {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "CODE_NOT_EXIST");
				return returnMsg;
			} else {
				returnMsg.put("code", "1");
				returnMsg.put("msg", "QUERY_SUCCESS");
				returnMsg.put("data", faultCode);
			}
		} catch (Exception e) {
			returnMsg.put("code", "0");
			returnMsg.put("msg", "UNKNOWN_ERROR");
			e.printStackTrace();
		}

		return returnMsg;
	}


	@ResponseBody
	@RequestMapping(value = "/record", method = RequestMethod.POST)
	public Object recordFaultCode(@RequestBody String[] codes) {




		return null;
	}
}
