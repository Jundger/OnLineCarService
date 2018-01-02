package com.jundger.carservice.constant;

/**
 * Created by 14246 on 2017/12/25.
 */

public class UrlConsts {

    // 服务器路径
    private static final String SERVER_URL = "http://39.108.160.249/webssm";

    //请求参数键
    public static final String KEY_TOKEN = "sessionid";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    /**
     * 登录请求的action
     */
    public static final String ACTION_LOGIN = "ssm/login";

    /**
     * 注册请求的action
     */
    public static final String ACTION_REGIST = "ssm/register";

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
