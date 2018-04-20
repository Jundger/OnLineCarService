package com.jundger.carservice.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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
import com.jundger.carservice.util.HttpUtil;
import com.jundger.carservice.util.InjectUtil;
import com.jundger.carservice.util.JsonParser;
import com.jundger.carservice.util.NetCheckUtil;

import java.util.HashMap;

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

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneNumber = username_register_et.getText().toString().trim();
                final String password = password_register_et.getText().toString().trim();
                final String rePassword = re_password_register_et.getText().toString().trim();

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
                        params.put(UrlConsts.KEY_PASSWORD, password);
                        HttpUtil.sendHttpRequset(UrlConsts.getRequestURL(UrlConsts.ACTION_CUSTOMER_REGISTER), params, new HttpUtil.HttpCallbackListener() {
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
                                    LoginActivity.launchActivity(RegisterActivity.this);
                                    RegisterActivity.this.finish();
                                } else if (UrlConsts.REGISTER_USER_EXIT.equals(JsonParser.parseRegister(response))) {
                                    Log.d(TAG, "Register: 账户已经存在！");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "账户已经存在！", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            RegisterActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
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
