package com.jundger.carservice.constant;

/**
 * Title: CarService
 * Date: Create in 2018/4/21 20:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class APPConsts {

    /**
     * 本地SharedPreferences存储文件名
     */
    public static final String SHARED_SAVE_NAME = "OnLineCarService";

    /**
     * 蓝牙故障码读取指令
     */
    public static final String BLUETOOTH_READ_COMMAND = "AT+DTC?";

    /**
     * 下位机蓝牙模块名称
     */
    public static final String BLUETOOTH_NAME = "OBD_BT";

    /**
     * 本地SharedPreferences存储key值
     */
    public static final String SHARED_KEY_ISLOGIN = "login_status";
    public static final String SHARED_KEY_LONGITUDE = "longitude";
    public static final String SHARED_KEY_LATITUDE = "latitude";

    /**
     * 极光推送设置
     */
    public static final String SHARED_KEY_REGISTRATION_ID = "Registration_ID";
    public static final String CUSTOMER_TAG = "CUSTOMER";
    public static final String REPAIRMAN_TAG = "REPAIRMAN";

    /**
     * 本地SQLite数据库名称
     */
    public static final String DATABASE_NAME = "OnLineCarService.db";

    /**
     * 本地SQLite数据库表名
     */
    public static final String DB_TABLE_OBD = "OBD_Info";

    /**
     * 本地SQLite数据库存储版本号
     */
    public static final int DATABASE_VERSION = 1;
    /**
     * 初始定位地点纬度
     * 重庆市城区
     */
    public static final double DEFAULT_LATITUDE = 29.35;

    /**
     * 初始定位地点经度
     * 重庆市城区
     */
    public static final double DEFAULT_LONGITUDE = 106.33;

    /**
     * Handler Message
     */
    public static final int CONNECT_SUCCESS = 1;
    public static final int CONNECT_FAIL = 2;
    public static final int MESSAGE_READ = 3;
    public static final int SHOW_RESULT = 4;

    /**
     * CircleProgressDialog环形进度条大小
     */
    public static final int CIRCLE_PROGRESS_SIZE = 200;

    public static final int TURN_ON_BT_REQUEST_CODE = 1;

    /**
     * 接单等待时间
     */
    public static final int WAIT_ORDER_TIME = 5 * 60 * 1000;

    /**
     * 极光推送相关
     */
    public static final String MESSAGE_RECEIVED_ACTION = "com.jpush.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static final String KEY_TYPE = "type";

    public static final String PUSH_TYPE_MESSAGE = "MESSAGE";
    public static final String PUSH_TYPE_NOTIFY = "NOTIFICATION";

    public static final String ORDER_NOTIFICATION_ACCEPT_TITLE = "订单接收请求";
    public static final String ORDER_NOTIFICATION_FINISH_TITLE = "订单结束请求";

    /**
     * 订单相关
     */
    public static final String MESSAGE_ORDER_ACCEPT = "ORDER_ACCEPT";
    public static final String ORDER_FINISH_REQUEST = "FINISH_REQUEST";
    public static final String ORDER_FINISH_RESPONSE = "FINISH_RESPONSE";

    public static final String ORDER_KEY_CODE = "code";
    public static final String ORDER_KEY_MESSAGE = "msg";
    public static final String ORDER_KEY_ORDERNO = "orderNo";
    public static final String ORDER_KEY_NAME = "name";

    /**
     * 订单结束相关操作标志位
     */
    public static final String REPAIRMAN_TO_CUSTOMER_REQUEST = "RPRM_TO_CUST_REQ";
    public static final String CUSTOMER_TO_REPAIRMAN_REQUEST = "CUST_TO_RPRM_REQ";
    public static final String REPAIRMAN_TO_CUSTOMER_RESPONSE = "RPRM_TO_CUST_RES";
    public static final String CUSTOMER_TO_REPAIRMAN_RESPONSE = "CUST_TO_RPRM_RES";

}
