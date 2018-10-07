package com.jundger.carservice.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.TokenWatcher;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jundger.carservice.R;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.util.FormatCheckUtil;
import com.jundger.carservice.util.InjectUtil;

public class NotifyActivity extends BaseActivity {

    private static final String TAG = "NotifyActivity";

    @InjectView(R.id.mybar_back_ib)
    private ImageView mybar_back_ib;

    @InjectView(R.id.mybar_title_tv)
    private TextView mybar_title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        InjectUtil.InjectView(this);

        init();
    }

    private void init() {
        mybar_title_tv.setText("通知中心");

        mybar_back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifyActivity.this.finish();
            }
        });
    }

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, NotifyActivity.class);
        context.startActivity(intent);
    }
}
