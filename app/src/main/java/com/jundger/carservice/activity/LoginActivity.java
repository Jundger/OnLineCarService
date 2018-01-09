package com.jundger.carservice.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jundger.carservice.R;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.util.FormatCheckUtil;
import com.jundger.carservice.util.InjectUtil;
import com.jundger.carservice.util.JsonParser;
import com.jundger.carservice.util.NetCheckUtil;
import com.jundger.carservice.util.SharedPreferencesUtil;
import com.jundger.carservice.util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private Dialog dialog = null;

    @InjectView(R.id.login_btn)
    private Button login_btn;

    @InjectView(R.id.username_clear_et)
    private EditText username_clear_et;

    @InjectView(R.id.password_clear_et)
    private EditText password_clear_et;

    @InjectView(R.id.forget_psw_tv)
    private TextView forget_psw_tv;

    @InjectView(R.id.register_tv)
    private TextView register_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InjectUtil.InjectView(this); // 自定义控件绑定注解

        login_btn.setOnClickListener(this);
        forget_psw_tv.setOnClickListener(this);
        register_tv.setOnClickListener(this);

        if (!NetCheckUtil.isNetworkConnected(this)) {
            Toast.makeText(this, "请打开网络连接！", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn: loginClickListener(); break;

            case R.id.forget_psw_tv: forgetPswClickListener(); break;

            case R.id.register_tv: registerClickListener(); break;
        }
    }

    private void loginClickListener() {

        final String phoneNumber = username_clear_et.getText().toString().trim();
        final String password = password_clear_et.getText().toString().trim();
        Log.i(TAG, "loginClickListener: phoneNumber-->" + phoneNumber);
        Log.i(TAG, "loginClickListener: password-->" + password);

        // 进行手机号和密码的格式校验
        if (!FormatCheckUtil.checkPhoneNumber(LoginActivity.this, phoneNumber) ||
                !FormatCheckUtil.checkPassword(LoginActivity.this, password)) {
            return;
        }

        if (!NetCheckUtil.isNetworkConnected(this)) {
            Toast.makeText(this, "请打开网络连接！", Toast.LENGTH_SHORT).show();
            return;
        }

        startDialog("正在登录……");
        final long startTime = System.currentTimeMillis();
        HashMap<String, String> params = new HashMap<>();
        params.put(UrlConsts.KEY_USERNAME, phoneNumber);
        params.put(UrlConsts.KEY_PASSWORD, password);
        HttpUtil.sendHttpRequset(UrlConsts.getRequestURL(UrlConsts.ACTION_LOGIN), params, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (null != response && !"".equals(response)) {
                    Log.d(TAG, "Login | recieve from server: " + response);
                    Map<String, String> map = JsonParser.parseLogin(response);
                    if (null != map) {
                        if (UrlConsts.REQUEST_SUCCESS_CODE.equals(map.get(UrlConsts.KEY_RETURN_CODE))) {
                            // 在本地存储服务器返回的Token值及登录状态
                            SharedPreferencesUtil.save(LoginActivity.this, UrlConsts.SHARED_TOKEN, map.get(UrlConsts.KEY_RETURN_TOKEN));
                            SharedPreferencesUtil.save(LoginActivity.this, UrlConsts.SHARED_PHONE, map.get(UrlConsts.KEY_RETURN_NAME));
                            SharedPreferencesUtil.save(LoginActivity.this, UrlConsts.SHARED_IS_LOGIN, true);
                            long endTime = System.currentTimeMillis();
                            if (endTime - startTime < 1500) {
                                try {
                                    Thread.sleep(1500 - (endTime - startTime));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                endDialog();
                            }
                            // 跳转到主页面
                            MainActivity.launchActivity(LoginActivity.this, phoneNumber);
                            LoginActivity.this.finish();
                        } else {
                            Log.d(TAG, "onFinish: 账号或者密码错误！");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "账号或者密码错误！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } else {
                        Log.e(TAG, "onFinish: JSON数据解析错误！");
                    }
                } else {
                    Log.d(TAG, "onFinish: 服务器返回值为空！");
                }
                endDialog();
            }

            @Override
            public void onError(final String error) {
                Log.e(TAG, "onError: " + error);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
                endDialog();
            }
        });
    }

    private void forgetPswClickListener() {
        ForgetPswActivity.launchActivity(LoginActivity.this);
    }

    private void registerClickListener() {
        RegisterActivity.launchActivity(LoginActivity.this);
    }

    private void startDialog(String msg) {
        dialog = new Dialog(LoginActivity.this, R.style.MyDialogStyle);
        dialog.setContentView(R.layout.loading);
        dialog.setCanceledOnTouchOutside(false);
        TextView message = (TextView) dialog.getWindow().findViewById(R.id.load_msg);
        if (dialog != null && !dialog.isShowing()) {
            message.setText(msg);
            dialog.show();
        }
    }

    private void endDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
