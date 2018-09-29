package com.jundger.work.web;

import com.alibaba.fastjson.JSON;
import com.jundger.common.util.CreateRandomCharacter;
import com.jundger.work.constant.Consts;
import com.jundger.work.pojo.Customer;
import com.jundger.work.pojo.JPush.*;
import com.jundger.work.pojo.json.OrderJson;
import com.jundger.work.service.OrderService;
import com.jundger.work.service.SiteService;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/9/29 15:24
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

@Controller
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SiteService siteService;


    private static Logger logger = Logger.getLogger(CustomerController.class);

    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public OrderJson createOrder(@RequestBody OrderJson orderJson) {

        logger.info("Info from client=============================================》\n" + orderJson);

        String orderNo = CreateRandomCharacter.getOrderno();
        Date currentTime = new Date();

        orderJson.setCreateTime(currentTime);
        orderJson.setOrderNo(orderNo);

//        List<String> alias = siteService.getRepairmanId(orderJson.getLongitude(), orderJson.getLatitude(), 1.0);
//        logger.info("订单推送目标====》" + JSON.toJSONString(alias));
//        Audience audience = new Audience(null, alias, null);
//        Platform<OrderJson> android = new Platform<>(Consts.ORDER_NOTIFICATION_ALERT, null, null, orderJson);
//        Notification<OrderJson> notification = new Notification<>(android);
//        Options options = new Options(60);
//        JPushJson<OrderJson> jPushJson = new JPushJson(null, "all", audience, notification, null, options);
//
//        String json = JSON.toJSONString(jPushJson);
//        System.out.println("JSON===》" + json);
//
//        try {
//            URL httpUrl = new URL(Consts.JPUSH_REQUEST_ADDRESS);
//            HttpURLConnection httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setRequestMethod("POST");
//
//            String authString = Consts.JPUSH_APP_KEY + ":" + Consts.JPUSH_MASTER_SECRET;
//            String authStringEnc = new BASE64Encoder().encode(authString.getBytes("UTF-8"));
//
//            httpURLConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
//            httpURLConnection.setRequestProperty("content-Type", "application/json");
//
//            httpURLConnection.connect();
//            OutputStream outputStream = httpURLConnection.getOutputStream();
//            outputStream.write(json.getBytes("UTF-8"));
//            if (httpURLConnection.getResponseCode() == 200) {
//                System.out.println("请求成功！");
//            } else {
//                System.out.println("请求失败！");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return orderJson;
    }

    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Map<String, Object> test(@RequestParam(value = "temp") String temp) {

        logger.info("Info from client========================================》");

        Map<String, Object> returnMsg = new HashMap<>();
        returnMsg.put("phone", "31425");
        returnMsg.put("name", "Jundger");

        return returnMsg;
    }

//    public static void main(String[] args) {
//
//        Customer customer = new Customer();
//        customer.setCustPhone("13900000000");
//        customer.setCustName("Jundger");
//        List<String> alias = new ArrayList<>();
//        alias.add("18875198367");
//        alias.add("13900000000");
//        alias.add("13800000000");
//        Audience audience = new Audience(null, alias, null);
//        Platform<Customer> android = new Platform<>("服务器JPush推送服务测试...", null, null, customer);
//        Notification<Customer> notification = new Notification<>(android);
//        Options options = new Options(60);
//        JPushJson<Customer> jPushJson = new JPushJson(null, "all", audience, notification, null, options);
//
//        String json = JSON.toJSONString(jPushJson);
//        System.out.println("JSON===》" + json);
//
//        try {
//            URL httpUrl = new URL(Consts.JPUSH_REQUEST_ADDRESS);
//            HttpURLConnection httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
//            httpURLConnection.setDoOutput(true);
//            httpURLConnection.setRequestMethod("POST");
//
//            String authString = Consts.JPUSH_APP_KEY + ":" + Consts.JPUSH_MASTER_SECRET;
////			Base64.encodeBase64(authString.getBytes());
//            String authStringEnc = new BASE64Encoder().encode(authString.getBytes("UTF-8"));
//
//            httpURLConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
//            httpURLConnection.setRequestProperty("content-Type", "application/json");
//
//            httpURLConnection.connect();
//            OutputStream outputStream = httpURLConnection.getOutputStream();
//            outputStream.write(json.getBytes("UTF-8"));
//            if (httpURLConnection.getResponseCode() == 200) {
//                System.out.println("请求成功！");
//            } else {
//                System.out.println("请求失败！");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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
