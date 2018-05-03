package com.jundger.carservice.constant;

/**
 * Title: CarService
 * Date: Create in 2018/4/21 20:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class UrlConsts {

    // 服务器路径
//    private static final String SERVER_URL = "http://39.108.160.249/CarServiceServer";
    private static final String SERVER_URL = "http://192.168.1.102:8080";

    // 请求参数键
    public static final String KEY_TOKEN = "token";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    // 返回参数键
    public static final String KEY_RETURN_MSG = "msg";
    public static final String KEY_RETURN_CODE = "code";
    public static final String KEY_RETURN_PHONE = "phone_number";
    public static final String KEY_RETURN_TOKEN = "token";

    // 请求结果
    public static final String REQUEST_SUCCESS_MSG = "USER_LOGIN_SUCCESS";
    public static final String REQUEST_FAIL_MSG = "USER_LOGIN_FAIL";
    public static final String REGISTER_SUCCESS = "USER_REGISTER_SUCCESS";
    public static final String REGISTER_FAIL = "UNKNOWN_ERROR";
    public static final String REGISTER_USER_EXIT = "USER_EXIST";
    public static final String CODE_SUCCESS = "1";
    public static final String CODE_FAIL = "0";


    /**
     * 得到相应action请求的URL路径
     *
     * @param action 需要请求的action
     * @return URL路径
     */
    public static String getRequestURL(String action) {
        return SERVER_URL + "/" + action;//得到服务器路径加上请求的动作
    }
}
