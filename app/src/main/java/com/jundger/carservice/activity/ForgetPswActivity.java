package com.jundger.carservice.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jundger.carservice.R;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.util.FormatCheckUtil;
import com.jundger.carservice.util.InjectUtil;
import com.jundger.carservice.util.NetCheckUtil;

public class ForgetPswActivity extends BaseActivity {

    @InjectView(R.id.forget_activity_tb)
    private Toolbar toolbar;

    @InjectView(R.id.username_forget_et)
    private EditText username_forget_et;

    @InjectView(R.id.code_forget_et)
    private EditText code_forget_et;

    @InjectView(R.id.get_code_forget_tv)
    private TextView get_code_forget_tv;

    @InjectView(R.id.forget_psw_btn)
    private Button forget_psw_btn;

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

        forget_psw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneNumber = username_forget_et.getText().toString().trim();
                final String code = code_forget_et.getText().toString().trim();

                if (!NetCheckUtil.isNetworkConnected(ForgetPswActivity.this)) {
                    Toast.makeText(ForgetPswActivity.this, "请打开网络连接！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (FormatCheckUtil.checkPhoneNumber(ForgetPswActivity.this, phoneNumber)) {
                    Toast.makeText(ForgetPswActivity.this, "抱歉，忘记密码功能尚未开通！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        get_code_forget_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ForgetPswActivity.this, "暂时无法获取验证码！", Toast.LENGTH_SHORT).show();
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
}
