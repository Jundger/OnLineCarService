package com.jundger.carservice.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by 14246 on 2018/4/20.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
