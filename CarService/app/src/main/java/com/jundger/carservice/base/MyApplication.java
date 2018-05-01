package com.jundger.carservice.base;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

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
    }

    public static Context getContext() {
        return context;
    }
}
