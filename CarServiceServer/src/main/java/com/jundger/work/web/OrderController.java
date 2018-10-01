package com.jundger.work.web;

import com.alibaba.fastjson.JSON;
import com.jundger.common.util.CreateRandomCharacter;
import com.jundger.work.constant.Consts;
import com.jundger.work.constant.OrderStatusEnum;
import com.jundger.work.pojo.*;
import com.jundger.work.pojo.JPush.*;
import com.jundger.work.pojo.json.OrderJson;
import com.jundger.work.service.OrderService;
import com.jundger.work.service.SiteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

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

    /**
     * 订单创建接口
     * @param orderJson 订单基本信息
     */
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Map<String, Object> createOrder(@RequestBody OrderJson orderJson) {

        logger.info("======================= 订单创建接口调用 ======================");
        logger.info("接收到数据==> " + orderJson);

        Map<String, Object> returnMsg = new HashMap<>();

        Order order = new Order();
        String orderNo = CreateRandomCharacter.getOrderno();
        Date currentTime = new Date();

        order.setOrderNo(orderNo);
        order.setCreateTime(currentTime);
        order.setPayStatus("NO");
        order.setCommentStatus("NO");
        order.setResolveStatus(OrderStatusEnum.WAITTING.toString());
        order.setCustId(orderJson.getCustomer().getCustId());
        order.setLongitude(Float.valueOf(String.valueOf(orderJson.getLongitude())));
        order.setLatitude(Float.valueOf(String.valueOf(orderJson.getLatitude())));
        logger.info("订单数据插入之前====》" + JSON.toJSONString(order));
        orderService.addOrder(order);

        List<OrderCode> orderCodeList = new ArrayList<>();
        for (FaultCode faultCode : orderJson.getFaultCodeList()) {
            OrderCode orderCode = new OrderCode();
            orderCode.setOrderId(order.getId());
            orderCode.setCodeId(faultCode.getId());
            orderCodeList.add(orderCode);
        }
        logger.info("订单与故障码对应List===》" + JSON.toJSONString(orderCodeList));
        orderService.addOrderCode(orderCodeList);


        orderJson.setCreateTime(currentTime);
        orderJson.setOrderNo(orderNo);

        List<String> alias = siteService.getRepairmanId(Float.valueOf(String.valueOf(orderJson.getLongitude())),
                Float.valueOf(String.valueOf(orderJson.getLatitude())), 1.0);
        logger.info("订单推送目标====》" + JSON.toJSONString(alias));

        List<OrderNotify> orderNotifyList = new ArrayList<>();
        for (String phone : alias) {
            OrderNotify orderNotify = new OrderNotify();
            orderNotify.setOrderId(order.getId());
            orderNotify.setOrderNo(orderNo);
            orderNotify.setRepaiamanPhone(phone);
            orderNotify.setFlag(Consts.FLAG_UNACCEPTED_ORDER);
            orderNotifyList.add(orderNotify);
        }
        logger.info("订单与维修人员对应List===》" + JSON.toJSONString(orderNotifyList));
        orderService.addOrderNotify(orderNotifyList);

        if (orderService.pushNotify(alias, orderJson)) {
            returnMsg.put("code", "1");
            returnMsg.put("msg", "SUCCESS");
        } else {
            returnMsg.put("code", "0");
            returnMsg.put("msg", "FAIL");
        }

        return returnMsg;
    }

    /**
     * 订单接受接口
     * @param orderJson 订单基本信息
     */
    @ResponseBody
    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    public Map<String, Object> acceptOrder(@RequestBody OrderJson orderJson) {
        logger.info("======================= 订单接受接口调用 ======================");
        logger.info("接收到数据==> " + orderJson);

        Map<String, Object> returnMsg = new HashMap<>();

        try {
            // 更新order_code表
            Order order = new Order();
            order.setOrderNo(orderJson.getOrderNo());
            order.setResolverId(orderJson.getRepairman().getId());
            order.setResolveStatus(OrderStatusEnum.RUNNING.toString());
            order.setResolverName(orderJson.getRepairman().getNickname());
            orderService.updateOrder(order);

            // 更新order_notify表
            OrderNotify orderNotify = new OrderNotify();
            orderNotify.setFlag(Consts.FLAG_ACCEPT_ORDER);
            orderNotify.setOrderNo(orderJson.getOrderNo());
            orderService.updateOrderNotify(orderNotify);

            List<String> alias = new ArrayList<>();
            alias.add(orderJson.getCustomer().getCustPhone());
            if (orderService.pushNotify(alias, orderJson)) {
                returnMsg.put("code", "1");
                returnMsg.put("msg", "SUCCESS");
            } else {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.put("code", "0");
            returnMsg.put("msg", "FAIL");
        }

        return returnMsg;
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
