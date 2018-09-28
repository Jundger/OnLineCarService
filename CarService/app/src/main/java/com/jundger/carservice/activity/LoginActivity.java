package com.jundger.carservice.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.jundger.carservice.constant.APPConsts;
import com.jundger.carservice.constant.Actions;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.bean.ResultObject;
import com.jundger.carservice.bean.User;
import com.jundger.carservice.util.FormatCheckUtil;
import com.jundger.carservice.util.InjectUtil;
import com.jundger.carservice.util.NetCheckUtil;
import com.jundger.carservice.util.HttpUtil;
import com.jundger.carservice.util.SharedPreferencesUtil;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

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

        // LitePal根据assets中的配置文件建立数据库和对应的表结构
        LitePal.getDatabase();

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

        HttpUtil.sendHttpRequest(UrlConsts.getRequestURL(Actions.ACTION_CUSTOMER_LOGIN), params, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (null != response && !"".equals(response)) {
                    Log.d(TAG, "Login | recieve from server: " + response);

                    ResultObject<User> result = new Gson().fromJson(response, new TypeToken<ResultObject<User>>(){}.getType());
//                    Map<String, String> map = JsonParser.parseLogin(response);
                    if (null != result) {
                        if (UrlConsts.CODE_SUCCESS.equals(result.getCode())) {
                            User user = result.getData();

                            // 在本地存储登录状态
                            SharedPreferencesUtil.save(LoginActivity.this, APPConsts.SHARED_KEY_ISLOGIN, true);

                            // 通过LitePal存储用户信息
                            user.save();

                            // 极光推送直接将用户电话设置为别名
                            JPushInterface.setAlias(LoginActivity.this,  1, user.getPhone());
                            Set<String> tags = new LinkedHashSet<>();
                            tags.add(APPConsts.CUSTOMER_TAG);
                            JPushInterface.setTags(LoginActivity.this, 1, tags);

                            Log.i(TAG, "onFinish: user----->" + new Gson().toJson(user));

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
                            MainActivity.launchActivity(LoginActivity.this, user);
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
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "MineFragment | onActivityResult: requestCode-->" + requestCode + " | resultCode-->" + resultCode);
        if (requestCode == 1 && resultCode == 2 && data != null) {
            String phone = data.getStringExtra("phone");
            username_clear_et.setText(phone);
        }
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
