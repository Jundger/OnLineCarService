package com.jundger.work.constant;

/**
 * Title: CarServiceServer
 * Date: Create in 2018/5/3 23:50
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */
public class Consts {

	/**
	 * 维修点列表返回数量
	 */
	public static final String LIMIT_NUM = "10";

	/**
	 * 手机客户端默认经度
	 */
	public static final String DEFAULT_LONGITUDE = "106.524505";

	/**
	 * 手机客户端默认纬度
	 */
	public static final String DEFAULT_LATITUDE = "29.457349";

	/**
	 * 手机客户端请求附近维修店默认半径
	 */
	public static final String DEFAULT_RADIUS = "20";

	/**
	 * 邮件发送的两种类型
	 * 0-注册邮件
	 * 1-忘记密码邮件
	 */
	public static final String EMAIL_REGISTER = "register";
	public static final String EMAIL_FORGET_PSW = "forget";

	/**
	 * APP默认用户头像请求路径
	 */
	public static final String DEFAULT_USER_PORTRAIT = "http://120.79.183.78/images/head_portrait_test01.png";

	/**
	 * JPush消息推送
	 */
	public static final String JPUSH_REQUEST_ADDRESS = "https://api.jpush.cn/v3/push";
	public static final String JPUSH_APP_KEY = "4b806c384e519434c62dde31";
	public static final String JPUSH_MASTER_SECRET = "f3cb514d970caddc52007411";
	public static final String ORDER_NOTIFICATION_ACCEPT_ALERT = "您有一个订单待接收";
	public static final String ORDER_NOTIFICATION_FINISH_ALERT = "有一个订单被对方请求结束";
	public static final String ORDER_NOTIFICATION_ACCEPT_TITLE = "订单接收请求";
	public static final String ORDER_NOTIFICATION_FINISH_TITLE = "订单结束请求";
	public static final String CONTENT_ORDER_ACCEPT = "ORDER_ACCEPT";
	public static final String ORDER_FINISH_REQUEST = "FINISH_REQUEST";
	public static final String ORDER_FINISH_RESPONSE = "FINISH_RESPONSE";

	/**
	 * 订单接收与否标志
	 */
	public static final String FLAG_ACCEPT_ORDER = "ACCEPT";
	public static final String FLAG_UNACCEPTED_ORDER = "UNACCEPTED";

	/**
	 * 订单结束相关操作标志位
	 */
	public static final String REPAIRMAN_TO_CUSTOMER_REQUEST = "RPRM_TO_CUST_REQ";
	public static final String CUSTOMER_TO_REPAIRMAN_REQUEST = "CUST_TO_RPRM_REQ";
	public static final String REPAIRMAN_TO_CUSTOMER_RESPONSE = "RPRM_TO_CUST_RES";
	public static final String CUSTOMER_TO_REPAIRMAN_RESPONSE = "CUST_TO_RPRM_RES";
}
