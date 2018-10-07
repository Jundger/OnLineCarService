package com.jundger.work.web;

import com.alibaba.fastjson.JSON;
import com.jundger.common.util.CreateRandomCharacter;
import com.jundger.work.constant.Consts;
import com.jundger.work.constant.OrderStatusEnum;
import com.jundger.work.pojo.*;
import com.jundger.work.pojo.json.OrderJson;
import com.jundger.work.service.CustomerService;
import com.jundger.work.service.OrderService;
import com.jundger.work.service.RepairmanService;
import com.jundger.work.service.SiteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private RepairmanService repairmanService;

    @Autowired
    private CustomerService customerService;


    private static Logger logger = Logger.getLogger(CustomerController.class);

    /**
     * 订单创建接口
     * @param orderJson 订单基本信息
     */
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Map<String, Object> createOrder(@RequestBody OrderJson orderJson) {

        logger.info("======================= 订单创建接口调用 ======================");
        logger.info("接收到数据==> " + JSON.toJSONString(orderJson));

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
        order.setDescribe(orderService.FormDescribeStr(orderJson.getFaultCodeList())); // orderService.FormDescribeStr(orderJson.getFaultCodeList())
        order.setOther(orderService.FormFaultCodeStr(orderJson.getFaultCodeList())); // orderService.FormFaultCodeStr(orderJson.getFaultCodeList())
//        logger.info("订单数据插入之前====》" + JSON.toJSONString(order));
        orderService.addOrder(order);

        List<OrderCode> orderCodeList = new ArrayList<>();
        for (FaultCode faultCode : orderJson.getFaultCodeList()) {
            OrderCode orderCode = new OrderCode();
            orderCode.setOrderId(order.getId());
            orderCode.setCodeId(faultCode.getId());
            orderCodeList.add(orderCode);
        }
//        logger.info("订单与故障码对应List===》" + JSON.toJSONString(orderCodeList));
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
//        logger.info("订单与维修人员对应List===》" + JSON.toJSONString(orderNotifyList));
        orderService.addOrderNotify(orderNotifyList);

        logger.info("发送给维修人员的json数据===>\n" + JSON.toJSONString(orderJson));
        if (orderService.pushNotifyOrder(alias, orderJson)) {
            returnMsg.put("code", "1");
            returnMsg.put("msg", "SUCCESS");
            returnMsg.put("data", orderNo);
        } else {
            returnMsg.put("code", "0");
            returnMsg.put("msg", "FAIL");
        }

        return returnMsg;
    }

    /**
     * 订单接受接口
     * @param orderNo 订单号
     * @param phone 普通用户电话
     * @param id 维修人员身份id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    public Map<String, Object> acceptOrder(@RequestParam(value = "orderNo") String orderNo,
                                           @RequestParam(value = "customerPhone") String phone,
                                           @RequestParam(value = "repairmanId") Integer id) {
        logger.info("======================= 订单接受接口调用 ======================");
        logger.info("orderNo-->" + orderNo);
        logger.info("customerPhone-->" + phone);
        logger.info("repairmanId-->" + id);

        Map<String, Object> returnMsg = new HashMap<>();

        try {
            Order order = orderService.selectByOrderNo(orderNo);
            Repairman repairman = repairmanService.getById(id);
            Site site = siteService.getSiteByRepairmanId(repairman.getId());
            repairman.setPassword(null);
            repairman.setVerification(null);
            repairman.setLoginIp(null);
            repairman.setUpdateTime(null);
            repairman.setRegistTime(null);
            if (null == order) {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "ORDER_NOT_EXIST");
                return returnMsg;
            } else if (!OrderStatusEnum.WAITTING.toString().equals(order.getResolveStatus())) {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "ORDER_STATUS_ERROR");
                return  returnMsg;
            }

            // 更新order表
            order.setResolverId(id);
            order.setResolveStatus(OrderStatusEnum.RUNNING.toString());
            order.setResolverName(repairman.getNickname());
            orderService.updateOrder(order);

            // 更新order_notify表
            OrderNotify orderNotify = new OrderNotify();
            orderNotify.setFlag(Consts.FLAG_ACCEPT_ORDER);
            orderNotify.setOrderNo(orderNo);
            orderService.updateOrderNotify(orderNotify);

            // 开始组装要发送给用户的OrderJson
            OrderJson orderJson = new OrderJson();
            orderJson.setOrderNo(orderNo);
            orderJson.setFaultCodeList(orderService.getCodeOrderByOrderId(order.getId()));
            orderJson.setRepairman(repairman);
            orderJson.setSiteName(site.getName());
            orderJson.setResolveStatus(order.getResolveStatus());
            orderJson.setCreateTime(order.getCreateTime());

            List<String> alias = new ArrayList<>();
            alias.add(phone);

            logger.info("发送给用户 " + JSON.toJSONString(alias) + " 的json数据===>\n" + JSON.toJSONString(orderJson));
            if (orderService.pushCustomMsgOrder(alias, orderJson)) {
                returnMsg.put("code", "1");
                returnMsg.put("msg", "SUCCESS");
            } else {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "SEND_MSG_ERROR");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.put("code", "0");
            returnMsg.put("msg", "FAIL");
        }

        return returnMsg;
    }

    /**
     * 订单结束接口
     * @param orderNo 订单号
     */
    @ResponseBody
    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    public Map<String, Object> finishOrder(@RequestParam(value = "orderNo") String orderNo,
                                           @RequestParam(value = "operate") String operate,
                                           @RequestParam(value = "answer", required = false, defaultValue = "0") Integer answer) {
        logger.info("======================= 订单结束接口调用 ======================");
        logger.info("接收到数据（订单号）==> " + orderNo);
        logger.info("接收到数据（操作代码）==> " + operate);

        Map<String, Object> returnMsg = new HashMap<>();
        Map<String, Object> sendMsg = new HashMap<>();

        List<String> alias = new ArrayList<>();

        try {
            Order order = orderService.selectByOrderNo(orderNo);
            if (null == order) {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "ORDER_NOT_EXIST");
                return returnMsg;
            } else if (!OrderStatusEnum.RUNNING.toString().equals(order.getResolveStatus())) {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "ORDER_STATUS_ERROR");
                return  returnMsg;
            }
            Customer customer = customerService.getById(order.getCustId());
            Repairman repairman = repairmanService.getById(order.getResolverId());

            String type;

            switch (operate) {
                case Consts.REPAIRMAN_TO_CUSTOMER_REQUEST:
                    sendMsg.put("orderNo", orderNo);
                    sendMsg.put("name", order.getResolverName());
                    alias.add(customer.getCustPhone());

                    type = Consts.ORDER_FINISH_REQUEST;
                    break;
                case Consts.CUSTOMER_TO_REPAIRMAN_RESPONSE:
                    if (answer == 1) {
                        // 更新order表中订单状态
                        order.setResolveStatus(OrderStatusEnum.FINISH.toString());
                        orderService.updateOrder(order);

                        sendMsg.put("code", 1);
                        sendMsg.put("msg", "SUCCESS");
                    } else {
                        sendMsg.put("code", 0);
                        sendMsg.put("msg", "REFUSE");
                    }
                    alias.add(repairman.getPhone());
                    type = Consts.ORDER_FINISH_RESPONSE;
                    break;
                case Consts.CUSTOMER_TO_REPAIRMAN_REQUEST:
                    sendMsg.put("orderNo", orderNo);
                    sendMsg.put("name", customer.getCustName());

                    alias.add(repairman.getPhone());
                    type = Consts.ORDER_FINISH_REQUEST;
                    break;
                case Consts.REPAIRMAN_TO_CUSTOMER_RESPONSE:
                    if (answer == 1) {
                        // 更新order表中订单状态
                        order.setResolveStatus(OrderStatusEnum.FINISH.toString());
                        orderService.updateOrder(order);

                        sendMsg.put("code", 1);
                        sendMsg.put("msg", "SUCCESS");
                    } else {
                        sendMsg.put("code", 0);
                        sendMsg.put("msg", "REFUSE");
                    }
                    alias.add(customer.getCustPhone());
                    type = Consts.ORDER_FINISH_RESPONSE;
                    break;
                default:
                    returnMsg.put("code", "0");
                    returnMsg.put("msg", "OPERATE_CODE_ERROR");
                    return returnMsg;
            }

            logger.info("发送给用户的json数据===>\n" + JSON.toJSONString(sendMsg));

            if (orderService.pushCustomMsg(alias, sendMsg, type)) {
                returnMsg.put("code", "1");
                returnMsg.put("msg", "SUCCESS");
            } else {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "SEND_MSG_FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.put("code", "0");
            returnMsg.put("msg", "FAIL");
        }

        return returnMsg;
    }

    /**
     * 查询已结束的订单接口（一般为维修人员客户端调用）
     * @param orderNo 订单号
     */
    @ResponseBody
    @RequestMapping(value = "/queryFinish", method = RequestMethod.POST)
    public Map<String, Object> queryFinishOrder(@RequestParam(value = "orderNo", required = false) String orderNo,
                                           @RequestParam(value = "repairmanId", required = false) Integer raprmId) {
        logger.info("======================= 订单查询接口调用 ======================");
        logger.info("接收到数据==> " + orderNo);

        Map<String, Object> returnMsg = new HashMap<>();
        List<Map<String, Object>> queryResult = null;

        try {

            if (null != orderNo) {
                queryResult = orderService.queryOrderByOrderNo(orderNo);
            } else if (null != raprmId) {
                queryResult = orderService.queryHistoryOrderByReprmId(raprmId);
            }

            if (null == queryResult || queryResult.isEmpty()) {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "ORDER_NOT_EXIST");
            } else {
                returnMsg.put("code", "1");
                returnMsg.put("msg", "SUCCESS");
                returnMsg.put("data", queryResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.put("code", "0");
            returnMsg.put("msg", "FAIL");
        }

        return returnMsg;
    }

    /**
     * 查询已结束的订单接口（一般为维修人员客户端调用）
     * @param customerId 用户id
     */
    @ResponseBody
    @RequestMapping(value = "/queryAll", method = RequestMethod.POST)
    public Map<String, Object> queryAllOrder(@RequestParam(value = "customerId") Integer customerId) {
        logger.info("======================= 订单查询接口调用 ======================");
        logger.info("接收到用户id==> " + customerId);

        Map<String, Object> returnMsg = new HashMap<>();
        List<OrderJson> orderJsonList = new ArrayList<>();

        try {
            List<Order> orderList = orderService.queryAllOrderByCustomerId(customerId);

            if (null == orderList || orderList.isEmpty()) {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "ORDER_NOT_EXIST");
                return returnMsg;
            }

            Site site;
            Repairman repairman;
            for (Order order : orderList) {
                OrderJson orderJson = new OrderJson();
                repairman = repairmanService.getById(order.getResolverId());
                repairman.setPassword(null);
                repairman.setVerification(null);
                repairman.setLoginIp(null);
                repairman.setUpdateTime(null);
                repairman.setRegistTime(null);
                orderJson.setOrderNo(order.getOrderNo());
                orderJson.setRepairman(repairman);
                orderJson.setResolveStatus(order.getResolveStatus());
                orderJson.setCreateTime(order.getCreateTime());
                orderJson.setFaultCodeList(orderService.getCodeOrderByOrderId(order.getId()));
                if (null == repairman) {
                    orderJson.setSiteName(null);
                } else {
                    logger.info(repairman.getId());
                    site = siteService.getSiteByRepairmanId(repairman.getId());
                    logger.info("site--> " + site);
                    orderJson.setLongitude(Double.valueOf(String.valueOf(site.getLongitude())));
                    orderJson.setLatitude(Double.valueOf(String.valueOf(site.getLatitude())));
                    orderJson.setSiteName(site.getName());
                }
                orderJsonList.add(orderJson);
            }
            logger.info("返回的数据============> " + JSON.toJSONString(orderJsonList));

            returnMsg.put("code", "1");
            returnMsg.put("msg", "SUCCESS");
            returnMsg.put("data", orderJsonList);

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
