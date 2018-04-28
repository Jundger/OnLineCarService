package com.jundger.work.web;

import com.jundger.work.pojo.Customer;
import com.jundger.work.pojo.FaultCode;
import com.jundger.work.service.CustomerService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			customer = this.customerService.validateLogin(username, password);
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
			data.put("token", this.customerService.generalToken(customer, 60 * 60 * 1000));
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
		customer.setCustPassword(password);
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
	@RequestMapping(value = "/querycode", method = RequestMethod.POST)
	public Map<String, Object> queryFaultMsg(@RequestParam(value = "brand") String brand, @RequestParam(value = "code") String code) {

		Map<String, Object> returnMsg = new HashMap<String, Object>();

		try {
			FaultCode faultCode = customerService.queryFaultCode(code, brand);
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
}
