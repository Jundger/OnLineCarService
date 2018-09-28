package com.jundger.work.web;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jundger.common.util.MD5Util;
import com.jundger.work.constant.Consts;
import com.jundger.work.pojo.Collect;
import com.jundger.work.pojo.Customer;
import com.jundger.work.service.CustomerService;
import com.jundger.work.util.JavaEmailSender;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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

	@ResponseBody
	@RequestMapping(value = "/getComment")
	public Map<String, Object> getCommentByCust(@RequestParam(value = "user_id") String cust_id) {

		logger.info("getCommentByCust request...");

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			List<Map<String, Object>> comments = customerService.getCommentByCust(cust_id);
			if (null != comments && !comments.isEmpty()) {
				returnMsg.put("code", "1");
				returnMsg.put("msg", "SUCCESS");
				returnMsg.put("data", comments);
			} else {
				returnMsg.put("code", "0");
				returnMsg.put("msg", "EMPTY");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("code", "0");
			returnMsg.put("msg", "FAIL");
		}

		return returnMsg;
	}

	/**
	 * 用户收藏操作
	 * TODO 在没有数据时添加数据，在已有数据时删除数据
	 * @param cust_id 用户ID
	 * @param type 收藏类型（文章/维修点）
	 * @param article_id 文章ID
	 * @param site_id 维修点ID
	 * @return 操作结果
	 */
	@ResponseBody
	@RequestMapping(value = "/collect")
	public Object setCollect(@RequestParam(value = "user_id") Integer cust_id,
							 @RequestParam(value = "type") String type,
							 @RequestParam(value = "article_id", required = false) Integer article_id,
							 @RequestParam(value = "site_id", required = false) Integer site_id) {

		logger.info("==============================addCollect request=======================================");

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			Collect collect = new Collect();
			collect.setUserId(cust_id);
			collect.setType(type);
			switch (type) {
				case "ARTICLE":
					collect.setArticleId(article_id);
					break;
				case "SITE":
					collect.setSiteId(site_id);
					break;
				default:
					returnMsg.put("code", "0");
					returnMsg.put("msg", "TYPE_ERROR");
					return returnMsg;
			}
			customerService.addCollect(collect);
			returnMsg.put("code", "1");
			returnMsg.put("msg", "SUCCESS");

		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("code", "0");
			returnMsg.put("msg", "FAIL");
		}

		return returnMsg;
	}

	@ResponseBody
	@RequestMapping(value = "/getCollect")
	public Object getCollect(@RequestParam(value = "user_id") Integer cust_id) {

		logger.info("==============================getCollect request=======================================");

		Map<String, Object> returnMsg = new HashMap<>();

		try {
			List<Collect> list = customerService.getAllCollect(cust_id);
			returnMsg.put("code", "1");
			returnMsg.put("msg", "SUCCESS");
			returnMsg.put("data", list);

		} catch (Exception e) {
			e.printStackTrace();
			returnMsg.put("code", "0");
			returnMsg.put("msg", "FAIL");
		}

		return returnMsg;
	}

	public static void main(String[] args) {
		Map<String, Object> returnMsg = new HashMap<>();
		Map<String, Object> extras = new HashMap<>();
		Map<String, Object> notify = new HashMap<>();
		Map<String, Object> android = new HashMap<>();
		Map<String, Object> audiences = new HashMap<>();
		extras.put("custPhone", "13900000000");
		extras.put("custName", "Jundger");
		android.put("alert", "恭喜你服务器API测试成功！");
		android.put("extras", extras);
		notify.put("android", android);

		List<String> alias = new ArrayList<>();
		alias.add("13900000000");
		audiences.put("alias", alias);

		returnMsg.put("platform", "all");
		returnMsg.put("audience", audiences);
		returnMsg.put("notification", notify);
		ObjectMapper mapper = new ObjectMapper();
		try {
			String mapJakcson = mapper.writeValueAsString(returnMsg);
			System.out.println("JSON===》" + mapJakcson);

			URL httpUrl = new URL("https://api.jpush.cn/v3/push");
			HttpURLConnection httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setConnectTimeout(8000);
			httpURLConnection.setReadTimeout(8000);

			String authString = "4b806c384e519434c62dde31:f3cb514d970caddc52007411";
//			Base64.encodeBase64(authString.getBytes());
			String authStringEnc = new BASE64Encoder().encode(authString.getBytes("UTF-8"));

			httpURLConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
			httpURLConnection.setRequestProperty("content-Type", "application/json");

			httpURLConnection.connect();
			OutputStream outputStream = httpURLConnection.getOutputStream();
			outputStream.write(mapJakcson.getBytes("UTF-8"));
			if (httpURLConnection.getResponseCode() == 200) {
				System.out.println("请求成功！");
			} else {
				System.out.println("请求失败！");
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

//	public static void main(String[] args) {
//
//		Map<String, Object> params = new HashMap<>();
//		params.put("phone", "13900000000");
//		params.put("password", "123456");
//
//		// 构建请求参数  
//		StringBuffer sbParams = new StringBuffer();
//		if (params != null && params.size() > 0) {
//			for (Map.Entry< String, Object > e:params.entrySet()){
//				sbParams.append(e.getKey());
//				sbParams.append("=");
//				sbParams.append(e.getValue());
//				sbParams.append("&");
//			}
//			sbParams.deleteCharAt(sbParams.length() - 1);
//		}
//
//		try {
//			byte[] entity = sbParams.toString().getBytes("UTF-8");
//			URL httpUrl = new URL("http://120.79.183.78/CarServiceServer/customer/login.do");
//			HttpURLConnection httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
//			httpURLConnection.setDoOutput(true);
//			httpURLConnection.setRequestMethod("POST");
//			httpURLConnection.setConnectTimeout(8000);
//			httpURLConnection.setReadTimeout(8000);
//			httpURLConnection.setRequestProperty("content-Type", "application/x-www-form-urlencoded");
////			httpURLConnection.connect();
//			OutputStream outputStream = httpURLConnection.getOutputStream();
//			outputStream.write(entity);
//			if (httpURLConnection.getResponseCode() == 200) {
//				System.out.println("请求成功！");
//				InputStream is = httpURLConnection.getInputStream();
//				// 封装输入流is，并指定字符集
//				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//				// 存放数据
//				StringBuffer sbf = new StringBuffer();
//				String temp = null;
//				while ((temp = br.readLine()) != null) {
//					sbf.append(temp);
//					sbf.append("\r\n");
//				}
//				String result = sbf.toString();
//
//				System.out.println(result);
//			} else {
//				System.out.println("请求失败！");
//			}
//		} catch (JsonProcessingException e) {
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (ProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
}
