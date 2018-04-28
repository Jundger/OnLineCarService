package com.jundger.carservice.constant;

/**
 * Title: CarService
 * Date: Create in 2018/4/21 20:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class Actions {

    /**
     * 用户登录请求的action
     */
    public static final String ACTION_CUSTOMER_LOGIN = "customer/login.do";

    /**
     * 用户注册请求的action
     */
    public static final String ACTION_CUSTOMER_REGISTER = "customer/register.do";

    /**
     * 获取所有服务点信息的action
     */
    public static final String ACTION_GET_SITE = "site/getlist.do";

    /**
     * 获取所有文章的action
     */
    public static final String ACTION_GET_ARTICLE = "article/getlist.do";

    /**
     * 查询故障码的action
     */
    public static final String ACTION_QUERY_CODE = "customer/querycode.do";
}
