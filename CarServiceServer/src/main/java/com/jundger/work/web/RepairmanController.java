package com.jundger.work.web;

import com.jundger.common.util.ToMap;
import com.jundger.work.pojo.Article;
import com.jundger.work.pojo.Repairman;
import com.jundger.work.service.RepairmanService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
@Controller
@RequestMapping(value = "repairman")
public class RepairmanController {

	@Resource
	private RepairmanService repairmanService;

	private Logger logger = Logger.getLogger(RepairmanController.class);

	@ResponseBody
	@RequestMapping(value = "getinfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String getSiteList(HttpServletRequest request, Model model,
							  @RequestParam(value = "id") Integer id) {

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		Repairman repairman = repairmanService.getById(id);
		returnMsg.put("return_code", "1");
		returnMsg.put("msg", "SUCCESS");
		returnMsg.put("nickname", repairman.getNickname());
		returnMsg.put("phone", repairman.getPhone());
		returnMsg.put("email", repairman.getEmail());
		returnMsg.put("sex", repairman.getSex());
		returnMsg.put("age", repairman.getAge());
		returnMsg.put("portrait", repairman.getPortrait());

		return ToMap.MapToJson(returnMsg);
	}
}
