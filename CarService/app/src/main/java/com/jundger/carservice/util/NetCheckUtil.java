package com.jundger.carservice.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Title: CarService
 * Date: Create in 2018/4/21 20:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class NetCheckUtil {


    /**
     * 检查是否有网络连接
     * @param context 上下文
     * @return 检查结果
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (null != connectivityManager) {
                NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null) {
                    return mNetworkInfo.isAvailable();
                }
            }
        }
        return false;
    }
}
