package com.jundger.carservice.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.R;
import com.jundger.carservice.constant.APPConsts;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.pojo.ResultObject;
import com.jundger.carservice.pojo.User;
import com.jundger.carservice.util.SharedPreferencesUtil;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        String token = (String) SharedPreferencesUtil.query(this, APPConsts.SHARED_KEY_TOKEN, "String");


        Boolean isLogin = (Boolean) SharedPreferencesUtil.query(this, APPConsts.SHARED_KEY_ISLOGIN, "boolean");
        if (isLogin != null && isLogin) {
            String user_json = (String) SharedPreferencesUtil.query(this, "USER_JSON", "String");
            if (user_json != null) {
                ResultObject<User> result = new Gson().fromJson(user_json, new TypeToken<ResultObject<User>>(){}.getType());
                final User user = result.getData();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.launchActivity(SplashActivity.this, user);
                        SplashActivity.this.finish();
                        // 淡入淡出动画效果
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        // 类似iphone进入和退出效果
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    }
                }, 1200);
            }
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
            }, 1200);
        }
    }
}
