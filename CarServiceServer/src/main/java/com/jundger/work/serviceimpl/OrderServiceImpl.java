package com.jundger.work.serviceimpl;

import com.alibaba.fastjson.JSON;
import com.jundger.work.constant.Consts;
import com.jundger.work.dao.OrderCodeMapper;
import com.jundger.work.dao.OrderMapper;
import com.jundger.work.dao.OrderNotifyMapper;
import com.jundger.work.pojo.JPush.*;
import com.jundger.work.pojo.Order;
import com.jundger.work.pojo.OrderCode;
import com.jundger.work.pojo.OrderNotify;
import com.jundger.work.pojo.json.OrderJson;
import com.jundger.work.service.OrderService;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

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
    private OrderCodeMapper orderCodeMapper;

    @Resource
    private OrderNotifyMapper orderNotifyMapper;

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
    public Boolean pushNotify(List<String> alias, OrderJson extras) {

        Audience audience = new Audience(null, alias, null);
        Platform<OrderJson> android = new Platform<>(Consts.ORDER_NOTIFICATION_ALERT, null, null, extras);
        Notification<OrderJson> notification = new Notification<>(android);
        Options options = new Options(60);
        JPushJson<OrderJson> jPushJson = new JPushJson(null, "all", audience, notification, null, options);

        String json = JSON.toJSONString(jPushJson);

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
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
