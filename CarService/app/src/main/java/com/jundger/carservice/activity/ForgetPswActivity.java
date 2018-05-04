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
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgetPswActivity extends BaseActivity {

    @InjectView(R.id.forget_activity_tb)
    private Toolbar toolbar;

    @InjectView(R.id.email_forget_et)
    private EditText email_forget_et;

    @InjectView(R.id.code_forget_et)
    private EditText code_forget_et;

    @InjectView(R.id.get_code_forget_tv)
    private TextView get_code_forget_tv;

    @InjectView(R.id.forget_psw_btn)
    private Button forget_psw_btn;

    @InjectView(R.id.new_psw_forget_et)
    private EditText new_psw_forget_et;

    @InjectView(R.id.repeat_psw_forget_et)
    private EditText repeat_psw_forget_et;

    private Dialog dialog;

    private static final String TAG = "ForgetPswActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);

        InjectUtil.InjectView(this); // 自定义控件绑定注解

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        if (!NetCheckUtil.isNetworkConnected(ForgetPswActivity.this)) {
            Toast.makeText(ForgetPswActivity.this, "请打开网络连接！", Toast.LENGTH_SHORT).show();
            return;
        }

        forget_psw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDialog("正在注册，请稍等...");
                String email = email_forget_et.getText().toString().trim();
                String code = code_forget_et.getText().toString().trim();
                String new_psw = new_psw_forget_et.getText().toString().trim();
                String repeat_psw = repeat_psw_forget_et.getText().toString().trim();

                if (email.length() > 0 && code.length() > 0 && new_psw.length() > 0 && repeat_psw.length() > 0) {
                    if (new_psw.equals(repeat_psw)) {
                        RequestBody requestBody = new FormBody.Builder()
                                .add("email", email)
                                .add("code", code)
                                .add("newPassword", new_psw)
                                .build();
                        HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_FORGET_PASSWORD), requestBody, new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        endDialog();
                                        Toast.makeText(ForgetPswActivity.this, "忘记密码失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String res = response.body().string();
                                Log.i(TAG, "onResponse: " + res);
                                Map<String, String> result = JsonParser.parseForgetPsw(res);
                                String str = "";
                                if ("1".equals(result.get(UrlConsts.KEY_RETURN_CODE))) {
                                    str = "忘记密码成功！";
                                } else {
                                    switch (result.get(UrlConsts.KEY_RETURN_MSG)) {
                                        case "CODE_ERROR":
                                            str = "验证码错误";
                                            break;
                                        case "FORGET_FAIL":
                                            str = "忘记密码失败！";
                                            break;
                                        default: break;
                                    }
                                }
                                final String hint = str;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        endDialog();
                                        Toast.makeText(ForgetPswActivity.this, hint, Toast.LENGTH_SHORT).show();
                                        ForgetPswActivity.this.finish();
                                    }
                                });
                            }
                        });
                    } else {
                        Toast.makeText(ForgetPswActivity.this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgetPswActivity.this, "请填写全部内容！", Toast.LENGTH_SHORT).show();
                }
                endDialog();
            }
        });

        get_code_forget_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = email_forget_et.getText().toString().trim();
                if (email.length() <= 0) {
                    Toast.makeText(ForgetPswActivity.this, "请输入电子邮箱地址！", Toast.LENGTH_SHORT).show();
                    return ;
                } else if (!FormatCheckUtil.checkEmail(email)) {
                    Toast.makeText(ForgetPswActivity.this, "邮箱格式错误！", Toast.LENGTH_SHORT).show();
                    return ;
                }
                startDialog("正在发送，请稍等...");
                RequestBody requestBody = new FormBody.Builder()
                        .add("email", email)
                        .add("type", UrlConsts.EMAIL_FORGET_PSW)
                        .build();
                HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_SEND_VERFI_CODE), requestBody, new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                endDialog();
                                Toast.makeText(ForgetPswActivity.this, R.string.send_email_fail, Toast.LENGTH_SHORT).show();
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
                            } else {
                                prompt = result.get(UrlConsts.KEY_RETURN_MSG);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    endDialog();
                                    Toast.makeText(ForgetPswActivity.this, prompt, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, ForgetPswActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ForgetPswActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startDialog(String msg) {
        dialog = new Dialog(ForgetPswActivity.this, R.style.MyDialogStyle);
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
