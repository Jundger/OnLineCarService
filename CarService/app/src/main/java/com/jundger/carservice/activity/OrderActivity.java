package com.jundger.carservice.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jundger.carservice.R;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.util.InjectUtil;

public class OrderActivity extends AppCompatActivity {
    public static final String PARAM_USER_ID = "user_id";

    private Integer user_id;

    @InjectView(R.id.mybar_back_ib)
    private ImageView mybar_back_ib;

    @InjectView(R.id.mybar_title_tv)
    private TextView mybar_title_tv;

    @InjectView(R.id.order_empty_layout)
    private RelativeLayout order_empty_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        InjectUtil.InjectView(this);
        user_id = getIntent().getIntExtra(PARAM_USER_ID, -1);

        order_empty_layout.setVisibility(View.VISIBLE);
        init();
    }

    private void init() {

        mybar_title_tv.setText("维修订单");

        mybar_back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderActivity.this.finish();
            }
        });
    }

    public static void launchActivity(Context context, Integer custId) {
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra(PARAM_USER_ID, custId);
        context.startActivity(intent);
    }
}
