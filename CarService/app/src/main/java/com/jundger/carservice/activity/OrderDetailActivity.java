package com.jundger.carservice.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.R;
import com.jundger.carservice.adapter.FaultInfoAdapter;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.bean.json.FaultCode;
import com.jundger.carservice.bean.json.OrderJson;
import com.jundger.carservice.constant.APPConsts;
import com.jundger.carservice.constant.Actions;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.util.HttpUtil;
import com.jundger.carservice.util.InjectUtil;
import com.jundger.carservice.widget.CircleImageView;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.jundger.carservice.base.MyApplication.isShowFinishRequestDialog;
import static com.jundger.carservice.constant.APPConsts.CUSTOMER_TO_REPAIRMAN_REQUEST;
import static com.jundger.carservice.constant.APPConsts.CUSTOMER_TO_REPAIRMAN_RESPONSE;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String PARAM_ORDER = "ORDER";
    private static final String TAG = "OrderDetailActivity";

    private ImageView mybar_back_ib;
    private TextView mybar_title_tv;
    private CircleImageView repairman_portrait_civ;
    private TextView order_resolver_site_tv;
    private TextView repairman_nickname_tv;
    private TextView repairman_phone_tv;
    private TextView detail_orderno_value_tv;
    private TextView detail_date_value_tv;
    private TextView detail_status_value_tv;
    private TextView operate_finish_tv;
    private TextView operate_comment_tv;
    private RecyclerView recyclerView;
    private RelativeLayout order_operate_rl;

    private OrderJson orderJson;
    private FaultInfoAdapter faultInfoAdapter;
    private List<FaultCode> infoList = new ArrayList<>();

    private CircleProgressDialog circleProgressDialog;

    private Boolean isFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderJson = (OrderJson) getIntent().getSerializableExtra(PARAM_ORDER);
        Log.i(TAG, "接收到的数据====> " + new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(orderJson));

        bindView();

        init();

        event();
    }

    private void bindView() {
        mybar_back_ib = findViewById(R.id.mybar_back_ib);
        mybar_title_tv = findViewById(R.id.mybar_title_tv);
        repairman_portrait_civ = findViewById(R.id.repairman_portrait_civ);
        order_resolver_site_tv = findViewById(R.id.order_resolver_site_tv);
        repairman_nickname_tv = findViewById(R.id.repairman_nickname_tv);
        repairman_phone_tv = findViewById(R.id.repairman_phone_tv);
        detail_orderno_value_tv = findViewById(R.id.detail_orderno_value_tv);
        detail_date_value_tv = findViewById(R.id.detail_date_value_tv);
        detail_status_value_tv = findViewById(R.id.detail_status_value_tv);
        operate_finish_tv = findViewById(R.id.operate_finish_tv);
        operate_comment_tv = findViewById(R.id.operate_comment_tv);
        recyclerView = findViewById(R.id.detail_fault_code_rcv);
        order_operate_rl = findViewById(R.id.order_operate_rl);

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

        if (isShowFinishRequestDialog) {
            isShowFinishRequestDialog = false;
            showFinishRequestDialog();
        }
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
                    processFinishOrderOperate();
                }
                break;
        }
    }

    public void showFinishRequestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
        builder.setIcon(R.mipmap.app_log);
        builder.setTitle(orderJson.getOrderNo());
        builder.setCancelable(false);
        builder.setMessage(orderJson.getRepairman().getNickname() + "请求结束订单，是否同意？");
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                detail_status_value_tv.setText("已完成");
                operate_finish_tv.setVisibility(View.GONE);
                operate_comment_tv.setVisibility(View.VISIBLE);
                Map<String, String> params = new HashMap<>();
                params.put("orderNo", orderJson.getOrderNo());
                params.put("operate", CUSTOMER_TO_REPAIRMAN_RESPONSE);
                params.put("answer", "1");
                HttpUtil.sendHttpRequest(UrlConsts.getRequestURL(Actions.ACTION_FINISH_ORDER), params, null);
            }
        });

        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Map<String, String> params = new HashMap<>();
                params.put("orderNo", orderJson.getOrderNo());
                params.put("operate", CUSTOMER_TO_REPAIRMAN_RESPONSE);
                params.put("answer", "0");
                HttpUtil.sendHttpRequest(UrlConsts.getRequestURL(Actions.ACTION_FINISH_ORDER), params, null);
            }
        });
        builder.show();
    }

    /**
     * 处理结束订单的操作
     */
    private void processFinishOrderOperate() {
        startProgressDialog("请稍等...");
        final long startTime = System.currentTimeMillis();
        RequestBody requestBody = new FormBody.Builder()
                .add("orderNo", orderJson.getOrderNo())
                .add("operate", CUSTOMER_TO_REPAIRMAN_REQUEST)
                .build();
        HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_FINISH_ORDER), requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG, "请求失败");
                stopProgressDialog();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String prompt;
                if (response.code() == 200) {
                    String res = response.body().string();
                    Log.i(TAG, "onResponse: 得到服务器返回数据===>" + res);

                    Map<String, String> result = new Gson().fromJson(res, new TypeToken<Map<String, String>>() {
                    }.getType());
                    if (UrlConsts.CODE_SUCCESS.equals(result.get(UrlConsts.KEY_RETURN_CODE))) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                circleProgressDialog.changeText("订单结束请求已发送，请等待对方接受请求...");
                            }
                        });

                        Log.i(TAG, "订单结束请求已发送，请等待对方接受请求...");

                        return;
                    } else {
                        prompt = "结束接单失败";
                    }
                } else {
                    prompt = "未知错误";
                }
                long endTime = System.currentTimeMillis();
                if (endTime - startTime < 1000) {
                    try {
                        Thread.sleep(1000 - (endTime - startTime));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                final String finalPrompt = prompt;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopProgressDialog();
                        Toast.makeText(OrderDetailActivity.this, finalPrompt, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 处理订单结束请求被响应后的操作
     * @param result 响应结果
     */
    public void processOrderFinishResponse(String result) {
        Log.i(TAG, "processOrderFinishResponse: 订单请求被响应后被调用");
        stopProgressDialog();

        if ("SUCCESS".equals(result)) {
            Toast.makeText(OrderDetailActivity.this, "订单结束成功！", Toast.LENGTH_SHORT).show();
            detail_status_value_tv.setText("已完成");
            operate_finish_tv.setVisibility(View.GONE);
            operate_comment_tv.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(OrderDetailActivity.this, "订单结束请求被拒绝！", Toast.LENGTH_SHORT).show();
        }
    }

    private void startProgressDialog(String msg) {
        circleProgressDialog = new CircleProgressDialog(OrderDetailActivity.this);
        // 可对对话框的大小、进度条的颜色、宽度、文字的颜色、内容等属性进行设置
        circleProgressDialog.setDialogSize(APPConsts.CIRCLE_PROGRESS_SIZE);
        circleProgressDialog.setCancelable(false);
        circleProgressDialog.setText(msg);
        circleProgressDialog.setProgressColor(R.color.appThemeColor);
        circleProgressDialog.setTextColor(R.color.appThemeColor);

        if (null != circleProgressDialog && !circleProgressDialog.isShowing()) {
            circleProgressDialog.showDialog();
        }

        // 显示过程中可根据状态改变文字内容及颜色
//        circleProgressDialog.changeText("erro:...");
//        circleProgressDialog.changeTextColor(Color.parseColor("##EB0000"));
    }

    private void stopProgressDialog() {
        if (null != circleProgressDialog && circleProgressDialog.isShowing()) {
            circleProgressDialog.dismiss();
        }
    }
}
