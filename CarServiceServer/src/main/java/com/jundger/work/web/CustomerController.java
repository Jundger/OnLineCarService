package com.jundger.work.web;

import com.alibaba.fastjson.JSON;
import com.jundger.common.util.MD5Util;
import com.jundger.work.constant.Consts;
import com.jundger.work.pojo.Customer;
import com.jundger.work.service.CustomerService;
import com.jundger.work.util.JavaEmailSender;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * Title: CarServiceServer
 * Date: Create in 2018/4/18 17:00
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Controller
@RequestMapping(value = "/customer")
public class CustomerController {

	@Resource
	private CustomerService customerService;

	private static Logger logger = Logger.getLogger(CustomerController.class);

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Map<String, Object> loginVerify(HttpServletRequest request, Model model) {
		Map<String, Object> returnMsg = new HashMap<String, Object>();
		String username = request.getParameter("phone");
		String password = request.getParameter("password");
//        System.out.println("UserName -->" + username + "  password -->" + password);

		Customer customer;
		try {
			customer = this.customerService.validateLogin(username, MD5Util.encode(password));
		} catch (Exception e) {
			returnMsg.put("code", "0");
			returnMsg.put("msg", "QUERY_FAIL");
			logger.error("数据库查询失败！", e);
			return returnMsg;
		}

		if (null != customer) {
			returnMsg.put("code", "1");
			returnMsg.put("msg", "USER_LOGIN_SUCCESS");
			Map<String, String> data = new HashMap<>();
			data.put("custId", String.valueOf(customer.getCustId()));
			data.put("custPhone", customer.getCustPhone());
			data.put("custPortrait", customer.getCustPortrait());
			data.put("custName", customer.getCustName());
			data.put("carBrand", customer.getCarBrand());
			data.put("carId", customer.getCarId());
			data.put("custEmail", customer.getCustEmail());
//			data.put("token", this.customerService.generalToken(customer, 60 * 60 * 1000));
			returnMsg.put("data", data);

			customer.setLoginIp(request.getRemoteAddr());
			customerService.updateByPrimaryKeySelective(customer);
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

		Customer customer;

		try {
			customer = customerService.getByEmail(email);
			if (customer != null && MD5Util.encode(code).equals(customer.getVerification())) {
				customer.setCustPhone(phone);
				customer.setCustPassword(MD5Util.encode(password));
				customer.setCustPortrait(Consts.DEFAULT_USER_PORTRAIT);
				customerService.updateByPrimaryKeySelective(customer);
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

		Customer customer = customerService.getByEmail(email);
		try {
			if (Consts.EMAIL_REGISTER.equals(type)) {
				if (customer != null) {
					returnMsg.put("code", "0");
					returnMsg.put("msg", "USER_EXIST");
					return returnMsg;
				}
				customer = new Customer();
				customer.setCustEmail(email);
				customerService.addCustomer(customer);

				logger.info("插入新用户后返回的自增id值： " + customer.getCustId());

			} else if (Consts.EMAIL_FORGET_PSW.equals(type)) {
				if (customer == null) {
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
			customer.setVerification(MD5Util.encode(verifCode));
			customerService.updateByPrimaryKeySelective(customer);

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
			Customer customer = customerService.getByEmail(email);
			if (MD5Util.encode(code).equals(customer.getVerification())) {
				customer.setCustPassword(MD5Util.encode(newPsw));
				customerService.updateByPrimaryKeySelective(customer);
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
	public Object modifyInfo(@RequestBody Customer customer) {

		logger.info("======================修改资料接口调用=====================");
		logger.info(JSON.toJSONString(customer));

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			if (customer != null) {
				int i = customerService.updateByPrimaryKeySelective(customer);
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
}
