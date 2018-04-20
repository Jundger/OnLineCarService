package com.jundger.carservice.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jundger.carservice.R;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.pojo.ServicePoint;
import com.jundger.carservice.util.InjectUtil;

public class ShopDetailActivity extends AppCompatActivity {

    private ServicePoint mServicePoint;

    @InjectView(R.id.detail_collapsing_tllt)
    private CollapsingToolbarLayout detail_collapsing_tllt;

    @InjectView(R.id.shop_detail_tb)
    private Toolbar shop_detail_tb;

    @InjectView(R.id.shop_detail_tv)
    private TextView shop_detail_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        InjectUtil.InjectView(this);

        mServicePoint = (ServicePoint) getIntent().getSerializableExtra("ServicePoint");

        setSupportActionBar(shop_detail_tb);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); // 返回按钮可点击
        }

        shop_detail_tv.setText(mServicePoint.getName());
    }

    public static void launchActivity(Context context, ServicePoint servicePoint) {
        Intent intent = new Intent(context, ShopDetailActivity.class);
        intent.putExtra("ServicePoint", servicePoint);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ShopDetailActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu_toolbar, menu);
        return true;
    }
}
