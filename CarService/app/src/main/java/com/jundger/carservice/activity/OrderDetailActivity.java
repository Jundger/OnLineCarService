package com.jundger.carservice.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.GsonBuilder;
import com.jundger.carservice.R;
import com.jundger.carservice.adapter.FaultInfoAdapter;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.bean.json.FaultCode;
import com.jundger.carservice.bean.json.OrderJson;
import com.jundger.carservice.util.InjectUtil;
import com.jundger.carservice.widget.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String PARAM_ORDER = "ORDER";
    private static final String TAG = "OrderDetailActivity";

    @InjectView(R.id.mybar_back_ib)
    private ImageView mybar_back_ib;

    @InjectView(R.id.mybar_title_tv)
    private TextView mybar_title_tv;

    @InjectView(R.id.repairman_portrait_civ)
    private CircleImageView repairman_portrait_civ;

    @InjectView(R.id.order_resolver_site_tv)
    private TextView order_resolver_site_tv;

    @InjectView(R.id.repairman_nickname_tv)
    private TextView repairman_nickname_tv;

    @InjectView(R.id.repairman_phone_tv)
    private TextView repairman_phone_tv;

    @InjectView(R.id.detail_orderno_value_tv)
    private TextView detail_orderno_value_tv;

    @InjectView(R.id.detail_date_value_tv)
    private TextView detail_date_value_tv;

    @InjectView(R.id.detail_status_value_tv)
    private TextView detail_status_value_tv;

    @InjectView(R.id.operate_finish_tv)
    private TextView operate_finish_tv;

    @InjectView(R.id.operate_comment_tv)
    private TextView operate_comment_tv;

    @InjectView(R.id.detail_fault_code_rcv)
    private RecyclerView recyclerView;

    @InjectView(R.id.order_operate_rl)
    private ImageView order_operate_rl;

    private OrderJson orderJson;
    private FaultInfoAdapter faultInfoAdapter;
    private List<FaultCode> infoList = new ArrayList<>();

    private Boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        InjectUtil.InjectView(this);

        orderJson = (OrderJson) getIntent().getSerializableExtra(PARAM_ORDER);
        Log.i(TAG, "接收到的数据====> " + new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(orderJson));

        init();

        event();
    }

    private void event() {
        repairman_phone_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + repairman_phone_tv.getText().toString().trim()));
                startActivity(intent);
            }
        });
    }

    private void init() {

        mybar_title_tv.setText("订单详情");

        mybar_back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetailActivity.this.finish();
            }
        });

        if (null != orderJson) {
            Glide.with(OrderDetailActivity.this)
                    .load(orderJson.getRepairman().getPortrait())
//                .placeholder(R.drawable.portrait_place_holder)
                    .error(R.drawable.load_fail)
                    .into(repairman_portrait_civ);

            order_resolver_site_tv.setText(orderJson.getSiteName());
            repairman_nickname_tv.setText(orderJson.getRepairman().getNickname());
            repairman_phone_tv.setText(orderJson.getRepairman().getPhone());
            detail_orderno_value_tv.setText(orderJson.getOrderNo());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            detail_date_value_tv.setText(spf.format(orderJson.getCreateTime()));

            String status = "NULL";
            String resolvStatus = orderJson.getResolveStatus();
            if ("WAITTING".equals(resolvStatus)) {
                status = "等待接单";
                isFinish = false;
                operate_finish_tv.setVisibility(View.VISIBLE);
                operate_comment_tv.setVisibility(View.GONE);
            } else if ("RUNNING".equals(resolvStatus)){
                status = "正在进行";
                isFinish = false;
                operate_finish_tv.setVisibility(View.VISIBLE);
                operate_comment_tv.setVisibility(View.GONE);
            } else if ("FINISH".equals(resolvStatus)){
                status = "已完成";
                isFinish = true;
                operate_finish_tv.setVisibility(View.GONE);
                operate_comment_tv.setVisibility(View.VISIBLE);
            }
            detail_status_value_tv.setText(status);

            infoList = orderJson.getFaultCodeList();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(OrderDetailActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        faultInfoAdapter = new FaultInfoAdapter(infoList);
        recyclerView.setAdapter(faultInfoAdapter);
    }

    public static void launchActivity(Context context, OrderJson orderJson) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra(PARAM_ORDER, orderJson);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_operate_rl:
                if ("FINISH".equals(orderJson.getResolveStatus())) {
                    Toast.makeText(OrderDetailActivity.this, "点击了评价", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OrderDetailActivity.this, "点击了结束订单", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
