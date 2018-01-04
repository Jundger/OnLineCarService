package com.jundger.carservice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jundger.carservice.R;
import com.jundger.carservice.base.BaseActivity;

public class ForgetPswActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);
    }

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, ForgetPswActivity.class);
        context.startActivity(intent);
    }
}
