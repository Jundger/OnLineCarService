package com.jundger.carservice.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jundger.carservice.R;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.bean.User;
import com.jundger.carservice.util.InjectUtil;

public class FeedbackActivity extends BaseActivity {

    private static final String PARAM_USER_NAME = "user_name";

    private String phone;

    @InjectView(R.id.feedback_submit_btn)
    private Button feedback_submit_btn;

    @InjectView(R.id.feedback_activity_tb)
    private Toolbar toolbar;

    private static final String TAG = "FeedbackActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        phone = getIntent().getStringExtra(PARAM_USER_NAME);
        InjectUtil.InjectView(this); // 自定义控件绑定注解

        init();
        event();

    }

    private void event() {
        feedback_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = feedback_submit_btn.getText().toString().trim();
                Log.i(TAG, "onClick: get feedback-->" + text);
                Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); // 返回按钮可点击
        }
        toolbar.setSubtitle("反馈意见");
    }

    public static void launchActivity(Context context, String username) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_USER_NAME, username);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            FeedbackActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
