package com.jundger.work.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.jundger.work.constant.Consts;
import com.jundger.work.dao.*;
import com.jundger.work.pojo.FaultCode;
import com.jundger.work.pojo.JPush.*;
import com.jundger.work.pojo.Order;
import com.jundger.work.pojo.OrderCode;
import com.jundger.work.pojo.OrderNotify;
import com.jundger.work.pojo.json.OrderJson;
import com.jundger.work.service.OrderService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/9/29 15:24
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private SiteMapper siteMapper;

    @Resource
    private OrderCodeMapper orderCodeMapper;

    @Resource
    private OrderNotifyMapper orderNotifyMapper;

    @Resource
    private FaultCodeMapper faultCodeMapper;

    private static Logger logger = Logger.getLogger(OrderServiceImpl.class);

    @Override
    public int addOrder(Order order) {
        return this.orderMapper.insertSelective(order);
    }

    @Override
    public int updateOrder(Order order) {
        return this.orderMapper.updateByOrderNoSelective(order);
    }

    @Override
    public int updateOrderNotify(OrderNotify orderNotify) {
        return this.orderNotifyMapper.updateByOrderNoSelective(orderNotify);
    }

    @Override
    public int addOrderCode(List<OrderCode> list) {
        return this.orderCodeMapper.insertList(list);
    }

    @Override
    public int addOrderNotify(List<OrderNotify> list) {
        return this.orderNotifyMapper.insertList(list);
    }

    @Override
    public Order selectByOrderNo(String orderNo) {
        return this.orderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public Map<String, Object> pushNotifyOrder(List<String> alias, OrderJson extras, String title, String alert) {
        Map<String, Object> returnMsg = new HashMap<>();
        Audience audience = new Audience(null, alias, null);
        Platform<OrderJson> android = new Platform<>(alert, title, null, extras);
        Notification<OrderJson> notification = new Notification<>(android);
        Options options = new Options(60);
        JPushJson<OrderJson> jPushJson = new JPushJson(null, "all", audience, notification, null, options);

        String json = JSON.toJSONString(jPushJson);
        logger.info("推送给目标 " + JSON.toJSONString(alias) + " 的json数据===>\n" + json);

        try {
            URL httpUrl = new URL(Consts.JPUSH_REQUEST_ADDRESS);
            HttpURLConnection httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");

            // Basic Auth 认证方式 --> "Basic AppKey:masterSecret"
            String authString = Consts.JPUSH_APP_KEY + ":" + Consts.JPUSH_MASTER_SECRET;
            String authStringEnc = new BASE64Encoder().encode(authString.getBytes("UTF-8"));
            httpURLConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

            httpURLConnection.setRequestProperty("content-Type", "application/json");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(json.getBytes("UTF-8"));
            if (httpURLConnection.getResponseCode() == 200) {
                returnMsg.put("code", "1");
                returnMsg.put("msg", "SUCCESS");
            } else {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "SEND_MSG_FAIL");
            }
            return returnMsg;
        } catch (IOException e) {
            e.printStackTrace();
            returnMsg.put("code", "0");
            returnMsg.put("msg", "SEND_MSG_FAIL");
            return returnMsg;
        }
    }

    @Override
    public Map<String, Object> pushNotify(List<String> alias, Map<String, Object> extras, String title, String alert) {
        Map<String, Object> returnMsg = new HashMap<>();
        Audience audience = new Audience(null, alias, null);
        Platform<Map<String, Object>> android = new Platform<>(alert, title, null, extras);
        Notification<Map<String, Object>> notification = new Notification<>(android);
        Options options = new Options(60);
        JPushJson<Map<String, Object>> jPushJson = new JPushJson(null, "all", audience, notification, null, options);

        String json = JSON.toJSONString(jPushJson);
        logger.info("推送给目标 " + JSON.toJSONString(alias) + " 的json数据===>\n" + json);

        try {
            URL httpUrl = new URL(Consts.JPUSH_REQUEST_ADDRESS);
            HttpURLConnection httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");

            // Basic Auth 认证方式 --> "Basic AppKey:masterSecret"
            String authString = Consts.JPUSH_APP_KEY + ":" + Consts.JPUSH_MASTER_SECRET;
            String authStringEnc = new BASE64Encoder().encode(authString.getBytes("UTF-8"));
            httpURLConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

            httpURLConnection.setRequestProperty("content-Type", "application/json");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(json.getBytes("UTF-8"));
            if (httpURLConnection.getResponseCode() == 200) {
                returnMsg.put("code", "1");
                returnMsg.put("msg", "SUCCESS");
            } else {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "SEND_MSG_FAIL");
            }
            return returnMsg;
        } catch (IOException e) {
            e.printStackTrace();
            returnMsg.put("code", "0");
            returnMsg.put("msg", "SEND_MSG_FAIL");
            return returnMsg;
        }
    }

    @Override
    public Map<String, Object> pushCustomMsgOrder(List<String> alias, OrderJson extras, String title) {
        Map<String, Object> returnMsg = new HashMap<>();
        Audience audience = new Audience(null, alias, null);
        Message<OrderJson> message = new Message<>(title, title, null, extras);

        Options options = new Options(60);
        JPushJson<OrderJson> jPushJson = new JPushJson(null, "all", audience, null, message, options);

        String json = JSON.toJSONString(jPushJson);
        logger.info("推送给目标 " + JSON.toJSONString(alias) + " 的json数据===>\n" + json);

        try {
            URL httpUrl = new URL(Consts.JPUSH_REQUEST_ADDRESS);
            HttpURLConnection httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");

            // Basic Auth 认证方式 --> "Basic AppKey:masterSecret"
            String authString = Consts.JPUSH_APP_KEY + ":" + Consts.JPUSH_MASTER_SECRET;
            String authStringEnc = new BASE64Encoder().encode(authString.getBytes("UTF-8"));
            httpURLConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

            httpURLConnection.setRequestProperty("content-Type", "application/json");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(json.getBytes("UTF-8"));
            if (httpURLConnection.getResponseCode() == 200) {
                returnMsg.put("code", "1");
                returnMsg.put("msg", "SUCCESS");
            } else {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "SEND_MSG_FAIL");
            }
            return returnMsg;
        } catch (IOException e) {
            e.printStackTrace();
            returnMsg.put("code", "0");
            returnMsg.put("msg", "SEND_MSG_FAIL");
            return returnMsg;
        }
    }

    @Override
    public Map<String, Object> pushCustomMsg(List<String> alias, Map<String, Object> extras, String title) {
        Map<String, Object> returnMsg = new HashMap<>();
        Audience audience = new Audience(null, alias, null);
        Message<Map<String, Object>> message = new Message<>(title, title, null, extras);

        Options options = new Options(60);
        JPushJson<Map<String, String>> jPushJson = new JPushJson(null, "all", audience, null, message, options);

        String json = JSON.toJSONString(jPushJson);
        logger.info("推送给目标 " + JSON.toJSONString(alias) + " 的json数据===>\n" + json);

        try {
            URL httpUrl = new URL(Consts.JPUSH_REQUEST_ADDRESS);
            HttpURLConnection httpURLConnection = (HttpURLConnection)httpUrl.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST");

            // Basic Auth 认证方式 --> "Basic AppKey:masterSecret"
            String authString = Consts.JPUSH_APP_KEY + ":" + Consts.JPUSH_MASTER_SECRET;
            String authStringEnc = new BASE64Encoder().encode(authString.getBytes("UTF-8"));
            httpURLConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);

            httpURLConnection.setRequestProperty("content-Type", "application/json");
            httpURLConnection.connect();
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(json.getBytes("UTF-8"));
            if (httpURLConnection.getResponseCode() == 200) {
                returnMsg.put("code", "1");
                returnMsg.put("msg", "SUCCESS");
            } else {
                returnMsg.put("code", "0");
                returnMsg.put("msg", "SEND_MSG_FAIL");
            }
            return returnMsg;
        } catch (IOException e) {
            e.printStackTrace();
            returnMsg.put("code", "0");
            returnMsg.put("msg", "SEND_MSG_FAIL");
            return returnMsg;
        }
    }

    @Override
    public String FormDescribeStr(List<FaultCode> list) {

        StringBuilder sb = new StringBuilder();
        for (FaultCode fc : list) {
            sb.append(fc.getDescribe()).append("; ");
        }
        return sb.toString();
    }

    @Override
    public String FormFaultCodeStr(List<FaultCode> list) {
        StringBuilder sb = new StringBuilder();
        for (FaultCode fc : list) {
            sb.append(fc.getCode()).append("; ");
        }
        return sb.toString();
    }

    @Override
    public List<Map<String, Object>> queryOrderByOrderNo(String orderNo) {
        return this.orderMapper.selectOrderShowByOrderNo(orderNo);
    }

    @Override
    public List<Map<String, Object>> queryHistoryOrderByReprmId(Integer id) {
        return this.orderMapper.selectFinishOrderByRepairManId(id);
    }

    @Override
    public List<Order> queryAllOrderByCustomerId(Integer id) {
        return this.orderMapper.selectOrderByCustomerId(id);
    }

    @Override
    public List<FaultCode> getCodeOrderByOrderId(Integer id) {
        return this.faultCodeMapper.selectByOrderId(id);
    }

    @Override
    public Map<String, Object> getStatisticsByResolverId(Integer id) {
        return this.orderMapper.getStatisticsByResolverId(id);
    }
}
