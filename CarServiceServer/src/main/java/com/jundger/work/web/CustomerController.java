package com.jundger.work.web;

import com.jundger.common.util.MD5Util;
import com.jundger.work.pojo.Customer;
import com.jundger.work.pojo.FaultCode;
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
		String username = request.getParameter("username");
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
			data.put("phone_number", customer.getCustPhone());
			data.put("portrait", customer.getCustPortrait());
			data.put("nickname", customer.getCustName());
			data.put("car_brand", customer.getCarBrand());
			data.put("car_id", customer.getCarId());
			data.put("email", customer.getCustEmail());
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
	public Map<String, Object> registerCustomer(HttpServletRequest request) {
		Map<String, Object> returnMsg = new HashMap<String, Object>();
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		Customer customer;

		try {
			customer = this.customerService.getByphoneNumber(username);
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("code", "0");
			returnMsg.put("msg", "QUERY_FAIL");
			System.out.println("数据库查询失败！");
			return returnMsg;
		}
		if (null != customer) {
			returnMsg.put("code", "0");
			returnMsg.put("msg", "USER_EXIST");
			System.out.println("用户已经存在！");
			return returnMsg;
		}

		customer = new Customer();
		customer.setCustPhone(username);
		customer.setCustPassword(MD5Util.encode(password));
		try {
			this.customerService.register(customer);
		} catch (Exception e) {
			returnMsg.put("code", "0");
			returnMsg.put("msg", "QUERY_FAIL");
			System.out.println("数据库查询失败！");
			return returnMsg;
		}
		returnMsg.put("code", "1");
		returnMsg.put("msg", "USER_REGISTER_SUCCESS");
		return returnMsg;
	}

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
			List<FaultCode> faultCode = customerService.queryFaultCode(codes, brand);
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

	/**
	 * 给目标用户发送验证码
	 * @param email 电子邮箱
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value="/sendEmail", method = RequestMethod.POST)
	public Object sendEmail(@RequestParam(value = "email") String email) {

		logger.info("==================发送邮件-->" + email + "==================");

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			//随机生成六位0-9的正整数作为验证码
			StringBuilder builder = new StringBuilder();
			Random random = new Random();
			for (int i = 0; i < 6; i++) {
				builder.append(random.nextInt(10));
			}
			String verifCode = builder.toString();
			Customer customer = customerService.getByEmail(email);
			if (customer == null) {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "EMAIL_IS_NOT_REGISTER");
				return returnMsg;
			}
			logger.info("Before md5-->" + verifCode);
			logger.info("After md5-->" + MD5Util.encode(verifCode));
			customer.setVerification(MD5Util.encode(verifCode));
			customerService.updateByPrimaryKeySelective(customer);

			JavaEmailSender.sendEmail(email, verifCode);

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
}
