package com.jundger.carservice.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

import cn.jpush.android.api.JPushInterface;

/**
 * Title: CarService
 * Date: Create in 2018/4/21 21:56
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        context = getApplicationContext();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

//        String Registration_ID = (String) SharedPreferencesUtil.query(this, APPConsts.SHARED_KEY_REGISTRATION_ID, "STRING");
//        if (Registration_ID != null && !"".equals(Registration_ID)) {
//            Log.i("JPushReceiver", "====== Registration_ID =======》" + Registration_ID);
//        }
    }

    public static Context getContext() {
        return context;
    }
}
