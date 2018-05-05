package com.jundger.carservice.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.R;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.constant.Actions;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.util.FormatCheckUtil;
import com.jundger.carservice.util.HttpUtil;
import com.jundger.carservice.util.InjectUtil;
import com.jundger.carservice.util.JsonParser;
import com.jundger.carservice.util.NetCheckUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";

    @InjectView(R.id.register_activity_tb)
    private Toolbar register_activity_tb;

    @InjectView(R.id.username_register_et)
    private EditText username_register_et;

    @InjectView(R.id.password_register_et)
    private EditText password_register_et;

    @InjectView(R.id.re_password_register_et)
    private EditText re_password_register_et;

    @InjectView(R.id.email_register_et)
    private EditText email_register_et;

    @InjectView(R.id.code_register_et)
    private EditText code_register_et;

    @InjectView(R.id.get_code_register_tv)
    private TextView get_code_register_tv;

    @InjectView(R.id.register_btn)
    private Button register_btn;

    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        InjectUtil.InjectView(this); // 自定义控件绑定注解

        setSupportActionBar(register_activity_tb);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); // 返回按钮可点击
        }

        event();
    }

    private void event() {
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneNumber = username_register_et.getText().toString().trim();
                final String email = email_register_et.getText().toString().trim();
                final String password = password_register_et.getText().toString().trim();
                final String rePassword = re_password_register_et.getText().toString().trim();
                final String code = code_register_et.getText().toString().trim();

                if (phoneNumber.length() <= 0 || email.length() <= 0 || password.length() <= 0 || rePassword.length() <= 0 || code.length() <= 0) {
                    Toast.makeText(RegisterActivity.this, "请填写所有数据！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!NetCheckUtil.isNetworkConnected(RegisterActivity.this)) {
                    Toast.makeText(RegisterActivity.this, "请打开网络连接！", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 进行手机号和密码的格式校验
                if (FormatCheckUtil.checkPhoneNumber(RegisterActivity.this, phoneNumber) &&
                        FormatCheckUtil.checkPassword(RegisterActivity.this, password)) {
                    if (!password.equals(rePassword)) {
                        Toast.makeText(RegisterActivity.this, "两次输入密码不一样，请重新输入！", Toast.LENGTH_SHORT).show();
                    } else {
                        startDialog("正在注册……");
                        final long startTime = System.currentTimeMillis();
                        HashMap<String, String> params = new HashMap<>();
                        params.put(UrlConsts.KEY_USERNAME, phoneNumber);
                        params.put(UrlConsts.KEY_EMAIL, email);
                        params.put(UrlConsts.KEY_PASSWORD, password);
                        params.put(UrlConsts.KEY_CODE, code);
                        HttpUtil.sendHttpRequest(UrlConsts.getRequestURL(Actions.ACTION_CUSTOMER_REGISTER), params, new HttpUtil.HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                Log.d(TAG, "Register | recieve from server: " + response);
                                if (UrlConsts.REGISTER_SUCCESS.equals(JsonParser.parseRegister(response))) {
                                    long endTime = System.currentTimeMillis();
                                    if (endTime - startTime < 1500) {
                                        try {
                                            Thread.sleep(1500 - (endTime - startTime));
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        endDialog();
                                    }
                                    Log.d(TAG, "Register: 注册成功！");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "注册成功,请登录！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Intent intent = new Intent();
                                    intent.putExtra("phone", phoneNumber);
                                    setResult(2, intent);
                                    RegisterActivity.this.finish();
                                } else if (UrlConsts.VERI_CODE_ERROR.equals(JsonParser.parseRegister(response))) {
                                    Log.d(TAG, "Register: 验证码错误！");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "验证码错误，请重发！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "Register: 注册失败！");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "注册失败！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                endDialog();
                            }

                            @Override
                            public void onError (String error){
                                Log.e(TAG, "onError: " + error);
                                endDialog();
                            }
                        });
                    }
                }
            }
        });

        get_code_register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_register_et.getText().toString().trim();
                if (email.length() <= 0) {
                    Toast.makeText(RegisterActivity.this, "请输入电子邮箱地址！", Toast.LENGTH_SHORT).show();
                    return ;
                } else if (!FormatCheckUtil.checkEmail(email)) {
                    Toast.makeText(RegisterActivity.this, "邮箱格式错误！", Toast.LENGTH_SHORT).show();
                    return ;
                }
                startDialog("正在发送，请稍等...");
                RequestBody requestBody = new FormBody.Builder()
                        .add("email", email)
                        .add("type", UrlConsts.EMAIL_REGISTER)
                        .build();
                HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_SEND_VERFI_CODE), requestBody, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                endDialog();
                                Toast.makeText(RegisterActivity.this, R.string.send_email_fail, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (response.code() == 200) {
                            String res = response.body().string();
                            Map<String, String> result = new Gson().fromJson(res, new TypeToken<Map<String, String>>(){}.getType());
                            final String prompt;
                            if (UrlConsts.CODE_SUCCESS.equals(result.get(UrlConsts.KEY_RETURN_CODE))) {
                                prompt = getResources().getString(R.string.send_email_success);
                            } else if (UrlConsts.REGISTER_USER_EXIT.equals(result.get(UrlConsts.KEY_RETURN_MSG))) {
                                prompt = getResources().getString(R.string.register_user_exist);
                            } else {
                                Log.i(TAG, "onResponse: 发送验证码失败：" + result.get(UrlConsts.KEY_RETURN_MSG));
                                prompt = getResources().getString(R.string.send_email_fail);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    endDialog();
                                    Toast.makeText(RegisterActivity.this, prompt, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            RegisterActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startDialog(String msg) {
        dialog = new Dialog(RegisterActivity.this, R.style.MyDialogStyle);
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
