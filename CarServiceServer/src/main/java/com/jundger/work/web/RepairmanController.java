package com.jundger.work.web;

import com.alibaba.fastjson.JSON;
import com.jundger.common.util.MD5Util;
import com.jundger.common.util.ToMap;
import com.jundger.work.constant.Consts;
import com.jundger.work.pojo.Article;
import com.jundger.work.pojo.Customer;
import com.jundger.work.pojo.Repairman;
import com.jundger.work.service.RepairmanService;
import com.jundger.work.util.JavaEmailSender;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Map<String, Object> loginVerify(HttpServletRequest request,
										   @RequestParam(value = "phone") String username,
										   @RequestParam(value = "password") String password) {
		Map<String, Object> returnMsg = new HashMap<String, Object>();

		Repairman repairman;
		try {
			repairman = this.repairmanService.validateLogin(username, MD5Util.encode(password));
		} catch (Exception e) {
			returnMsg.put("code", "0");
			returnMsg.put("msg", "QUERY_FAIL");
			logger.error("数据库查询失败！", e);
			return returnMsg;
		}

		if (null != repairman) {
			returnMsg.put("code", "1");
			returnMsg.put("msg", "USER_LOGIN_SUCCESS");
			Map<String, String> data = new HashMap<>();
			data.put("id", String.valueOf(repairman.getId()));
			data.put("phone", repairman.getPhone());
			data.put("nickname", repairman.getNickname());
			data.put("email", repairman.getEmail());
			data.put("portrait", repairman.getPortrait());
//			data.put("token", this.customerService.generalToken(customer, 60 * 60 * 1000));
			returnMsg.put("data", data);

			repairman.setLoginIp(request.getRemoteAddr());
			repairmanService.updateByPrimaryKeySelective(repairman);
		} else {
			returnMsg.put("code", "0");
			returnMsg.put("msg", "USERNAME_OR_PASSWORD_ERROR");
		}

		return returnMsg;
	}

	@ResponseBody
	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Map<String, Object> registerCustomer(@RequestParam(value = "phone") String phone,
												@RequestParam(value = "email") String email,
												@RequestParam(value = "password") String password,
												@RequestParam(value = "code") String code) {

		Map<String, Object> returnMsg = new HashMap<>();

		Repairman repairman;

		try {
			repairman = repairmanService.getByEmail(email);
			if (repairman != null && MD5Util.encode(code).equals(repairman.getVerification())) {
				repairman.setPhone(phone);
				repairman.setPassword(MD5Util.encode(password));
				repairman.setPortrait(Consts.DEFAULT_USER_PORTRAIT);
				repairmanService.updateByPrimaryKeySelective(repairman);
				returnMsg.put("code", "1");
				returnMsg.put("msg", "REGISTER_SUCCESS");
			} else {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "CODE_ERROR");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("code", "0");
			returnMsg.put("msg", "UNKNOWN_ERROR");
		}
		return returnMsg;
	}

	/**
	 * 给目标用户发送验证码
	 * @param email 电子邮箱
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/sendEmail", method = RequestMethod.POST)
	public Object sendEmail(@RequestParam(value = "email") String email,
							@RequestParam(value = "type") String type) {

		logger.info("==================发送邮件-->" + email + "==================");

		Map<String, Object> returnMsg = new HashMap<>();

		Repairman repairman = repairmanService.getByEmail(email);
		try {
			if (Consts.EMAIL_REGISTER.equals(type)) {
				if (repairman != null) {
					returnMsg.put("code", "0");
					returnMsg.put("msg", "USER_EXIST");
					return returnMsg;
				}
				repairman = new Repairman();
				repairman.setEmail(email);
				repairmanService.addRepairman(repairman);

				logger.info("插入新用户后返回的自增id值： " + repairman.getId());

			} else if (Consts.EMAIL_FORGET_PSW.equals(type)) {
				if (repairman == null) {
					returnMsg.put("code", "0");
					returnMsg.put("msg", "EMAIL_IS_NOT_REGISTER");
					return returnMsg;
				}
			} else {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "EMAIL_TYPE_ERROR");
				return returnMsg;
			}

			//随机生成六位0-9的正整数作为验证码
			StringBuilder builder = new StringBuilder();
			Random random = new Random();
			for (int i = 0; i < 6; i++) {
				builder.append(random.nextInt(10));
			}
			String verifCode = builder.toString();
			repairman.setVerification(MD5Util.encode(verifCode));
			repairmanService.updateByPrimaryKeySelective(repairman);

			JavaEmailSender.sendEmail(email, verifCode, type);

			returnMsg.put("code", "1");
			returnMsg.put("msg", "SEND_SUCCESS");
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("code", "0");
			returnMsg.put("msg", "SEND_FAIL");
		}

		return returnMsg;
	}

	@ResponseBody
	@RequestMapping(value = "/forgetPsw", method = RequestMethod.POST)
	public Object forgetPsw(@RequestParam(value = "email") String email,
							@RequestParam(value = "code") String code,
							@RequestParam(value = "newPassword") String newPsw) {

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			Repairman repairman = repairmanService.getByEmail(email);
			if (MD5Util.encode(code).equals(repairman.getVerification())) {
				repairman.setPassword(MD5Util.encode(newPsw));
				repairmanService.updateByPrimaryKeySelective(repairman);
				returnMsg.put("code", "1");
				returnMsg.put("msg", "FORGET_SUCCESS");
			} else {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "CODE_ERROR");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("code", "0");
			returnMsg.put("msg", "FORGET_FAIL");
		}

		return returnMsg;
	}

	@ResponseBody
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Object modifyInfo(@RequestBody Repairman repairman) {

		logger.info("======================修改资料接口调用=====================");
		logger.info(JSON.toJSONString(repairman));

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			if (repairman != null) {
				int i = repairmanService.updateByPrimaryKeySelective(repairman);
				logger.info("update return: " + i);
				if (i == 1) {
					returnMsg.put("code", "1");
					returnMsg.put("msg", "MODIFY_SUCCESS");
				} else {
					returnMsg.put("code", "0");
					returnMsg.put("msg", "MODIFY_FAIL");
				}
			} else {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "NULL");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("code", "0");
			returnMsg.put("msg", "UNKNOWN_ERROR");
		}

		return returnMsg;
	}

	@ResponseBody
	@RequestMapping(value = "getinfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String getSiteList(@RequestParam(value = "id") Integer id) {

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		Repairman repairman = repairmanService.getById(id);
		returnMsg.put("code", "1");
		returnMsg.put("msg", "SUCCESS");
		returnMsg.put("nickname", repairman.getNickname());
		returnMsg.put("phone", repairman.getPhone());
		returnMsg.put("email", repairman.getEmail());
		returnMsg.put("portrait", repairman.getPortrait());
		returnMsg.put("regist_time", repairman.getRegistTime());

		return ToMap.MapToJson(returnMsg);
	}
}
