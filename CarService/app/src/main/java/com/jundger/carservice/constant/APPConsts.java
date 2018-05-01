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
     * 本地SharedPreferences存储key值
     */
    public static final String SHARED_KEY_TOKEN = "token";
    public static final String SHARED_KEY_PHONE = "phone_number";
    public static final String SHARED_KEY_ISLOGIN = "login_status";
    public static final String SHARED_KEY_NICKNAME = "nickname";
    public static final String SHARED_KEY_BRAND = "car_brand";
    public static final String SHARED_KEY_BRAND_NO = "brand_no";
    public static final String SHARED_KEY_PORTRAIT = "portrait";

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
}
