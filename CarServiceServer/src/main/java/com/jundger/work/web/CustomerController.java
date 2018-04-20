package com.jundger.work.web;

import com.jundger.work.pojo.Customer;
import com.jundger.work.service.CustomerService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
@RequestMapping(value = "customer")
public class CustomerController {

	@Resource
	private CustomerService customerService;

	private static Logger logger = Logger.getLogger(CustomerController.class);

	@ResponseBody
	@RequestMapping(value = "login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Map<String, Object> loginVerify(HttpServletRequest request, Model model) {
		Map<String, Object> returnMsg = new HashMap<String, Object>();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
//        System.out.println("UserName -->" + username + "  password -->" + password);

		Customer customer;
		try {
			customer = this.customerService.validateLogin(username, password);
		} catch (Exception e) {
			returnMsg.put("return_code", "0");
			returnMsg.put("msg", "QUERY_FAIL");
			logger.error("数据库查询失败！", e);
			return returnMsg;
		}

		if (null != customer) {
			returnMsg.put("return_code", "1");
			returnMsg.put("msg", "USER_LOGIN_SUCCESS");
			returnMsg.put("phone_number", customer.getCustPhone());
			returnMsg.put("portrait", customer.getCustPortrait());
			returnMsg.put("nickname", customer.getCustName());
			returnMsg.put("car_brand", customer.getCarBrand());
			returnMsg.put("email", customer.getCustEmail());
			returnMsg.put("token", this.customerService.generalToken(customer, 60 * 60 * 1000));
		} else {
			returnMsg.put("return_code", "0");
			returnMsg.put("msg", "USERNAME_OR_PASSWORD_ERROR");
		}

		return returnMsg;
	}

	@ResponseBody
	@RequestMapping(value = "register", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public Map<String, Object> registerCustomer(HttpServletRequest request, Model model) {
		Map<String, Object> returnMsg = new HashMap<String, Object>();
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		Customer customer;

		try {
			customer = this.customerService.getByphoneName(username);
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("return_code", "0");
			returnMsg.put("msg", "QUERY_FAIL");
			System.out.println("数据库查询失败！");
			return returnMsg;
		}
		if (null != customer) {
			returnMsg.put("return_code", "0");
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
			returnMsg.put("return_code", "0");
			returnMsg.put("msg", "QUERY_FAIL");
			System.out.println("数据库查询失败！");
			return returnMsg;
		}
		returnMsg.put("return_code", "1");
		returnMsg.put("msg", "USER_REGISTER_SUCCESS");
		return returnMsg;
	}
}
