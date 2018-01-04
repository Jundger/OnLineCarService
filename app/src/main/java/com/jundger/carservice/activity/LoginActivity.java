package com.jundger.carservice.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.jundger.carservice.util.InjectUtil;
import com.jundger.carservice.util.JsonParser;
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

        // TODO 测试阶段直接跳转到主界面
//        MainActivity.launchActivity(LoginActivity.this, "13983348685", "123456");
//        LoginActivity.this.finish();
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
        final String PHONE_NOT_NULL = "手机号码不能为空";
        final String PHONE_FORM_NOT_SPECS = "手机号码不符合大陆规范";
        final String PHONE_LENGTH_NOT_SPECS = "手机号码长度不符合大陆规范";
        final String PASSWORD_NOT_NULL = "密码不能为空";
        final String PASSWORD_NOT_MORE_SIX = "密码长度必须大于6位";

        final String phoneNumber = username_clear_et.getText().toString().trim();
        final String password = password_clear_et.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(LoginActivity.this, PHONE_NOT_NULL, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!phoneNumber.substring(0, 1).equals("1")) {
            Toast.makeText(LoginActivity.this, PHONE_FORM_NOT_SPECS, Toast.LENGTH_SHORT).show();
            return;
        }
        if (phoneNumber.length() != 11) {
            Toast.makeText(LoginActivity.this, PHONE_LENGTH_NOT_SPECS, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, PASSWORD_NOT_NULL, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(LoginActivity.this, PASSWORD_NOT_MORE_SIX, Toast.LENGTH_SHORT).show();
            return;
        }

        startDialog("正在登录");
        HashMap<String, String> params = new HashMap<>();
        params.put(UrlConsts.KEY_USERNAME, phoneNumber);
        params.put(UrlConsts.KEY_PASSWORD, password);
        HttpUtil.sendHttpRequset(UrlConsts.getRequestURL(UrlConsts.ACTION_LOGIN), params, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                endDialog();
                if (null != response && !"".equals(response)) {
                    Log.d(TAG, "onFinish | recieve from server: " + response);
                    Map<String, String> map = JsonParser.parseLogin(response);
                    if (null != map) {
                        if (UrlConsts.REQUEST_SUCCESS_CODE.equals(map.get(UrlConsts.KEY_RETURN_CODE))) {
                            // 在本地存储服务器返回的Token值
                            SharedPreferencesUtil.save(LoginActivity.this, UrlConsts.KEY_TOKEN, map.get(UrlConsts.KEY_RETURN_TOKEN));
                            // 跳转到主页面
                            MainActivity.launchActivity(LoginActivity.this, phoneNumber, password);
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
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "onError: " + error);
                endDialog();
            }
        });
    }

    private void forgetPswClickListener() {
        Toast.makeText(LoginActivity.this, "Forget Password Click!", Toast.LENGTH_SHORT).show();
    }

    private void registerClickListener() {
        Toast.makeText(LoginActivity.this, "Register User Click!", Toast.LENGTH_SHORT).show();
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

}
