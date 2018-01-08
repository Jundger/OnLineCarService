package com.jundger.carservice.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.jundger.carservice.R;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.util.SharedPreferencesUtil;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String token = (String) SharedPreferencesUtil.query(this, UrlConsts.SHARED_TOKEN, "String");
        boolean isLogin = (boolean) SharedPreferencesUtil.query(this, UrlConsts.SHARED_IS_LOGIN, "boolean");
        Log.i(TAG, "onCreate: token from sharedpreference-->" + token);
        if (token != null && !TextUtils.isEmpty(token) && isLogin) {
            String phoneNumber = (String) SharedPreferencesUtil.query(this, UrlConsts.SHARED_TOKEN, "String");
            MainActivity.launchActivity(SplashActivity.this, phoneNumber);
        } else {
            LoginActivity.launchActivity(SplashActivity.this);
        }
        SplashActivity.this.finish();
    }
}