package com.jundger.carservice.activity;

import android.os.Handler;
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
        if (token != null && !TextUtils.isEmpty(token) && isLogin) {
            final String phoneNumber = (String) SharedPreferencesUtil.query(this, UrlConsts.SHARED_TOKEN, "String");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MainActivity.launchActivity(SplashActivity.this, phoneNumber);
                    SplashActivity.this.finish();
                    // 淡入淡出动画效果
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    // 类似iphone进入和退出效果
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }
            }, 1000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoginActivity.launchActivity(SplashActivity.this);
                    SplashActivity.this.finish();
                    // 淡入淡出动画效果
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    // 类似iphone进入和退出效果
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }
            }, 1000);
        }
    }
}
