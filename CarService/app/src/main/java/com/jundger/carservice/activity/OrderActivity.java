package com.jundger.carservice.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.R;
import com.jundger.carservice.adapter.CommentAdapter;
import com.jundger.carservice.adapter.OrderAdapter;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.bean.ResultArray;
import com.jundger.carservice.bean.User;
import com.jundger.carservice.bean.json.OrderJson;
import com.jundger.carservice.constant.Actions;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.util.HttpUtil;
import com.jundger.carservice.util.InjectUtil;
import com.jundger.carservice.util.LocationUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderActivity extends BaseActivity {
    public static final String PARAM_USER = "USER";
    private static final String TAG = "OrderActivity";

    private User user;
    private OrderAdapter orderAdapter;
    public List<OrderJson> orderJsonList = new ArrayList<>();

    public static Double longitude = 106.528041;
    public static Double latitude = 29.455653;

    @InjectView(R.id.mybar_back_ib)
    private ImageView mybar_back_ib;

    @InjectView(R.id.mybar_title_tv)
    private TextView mybar_title_tv;

    @InjectView(R.id.order_list_rcv)
    private RecyclerView recyclerView;

    @InjectView(R.id.order_list_srl)
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        InjectUtil.InjectView(this);
        user = (User) getIntent().getSerializableExtra(PARAM_USER);

        requestOrderList();
        requestLocation();
        init();
    }

    private void requestLocation() {
        Location location = LocationUtil.requestLocation(OrderActivity.this);
        if (null != location) {
            Log.i(TAG, "经纬度获取不为空: " + longitude + " : " + latitude);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        } else {
            Log.i(TAG, "经纬度获取为空");
        }
    }

    private void requestOrderList() {

        swipeRefresh.setRefreshing(true);
        String url = UrlConsts.getRequestURL(Actions.ACTION_QUERY_ORDER);
        RequestBody requestBody = new FormBody.Builder()
                .add("customerId", String.valueOf(user.getCustId()))
                .build();
        HttpUtil.okHttpPost(url, requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(OrderActivity.this, "网络请求无响应！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String prompt;
                if (response.code() == 200) {
                    String res = response.body().string();
                    Log.i(TAG, "onResponse: 得到服务器返回数据===>" + res);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    ResultArray<OrderJson> result = gson.fromJson(res, new TypeToken<ResultArray<OrderJson>>(){}.getType());
                    if (UrlConsts.CODE_SUCCESS.equals(result.getCode())) {

                        orderJsonList.clear();

                        // 将正在进行的订单置顶
//                        for (OrderJson orderJson : result.getData()) {
//                            if ("RUNNING".equals(orderJson.getResolveStatus())) {
//                                orderJsonList.add(orderJson);
//                                result.getData().remove(orderJson);
//                            }
//                        }
                        Iterator<OrderJson> iterator = result.getData().iterator();
                        while (iterator.hasNext()) {
                            OrderJson item = iterator.next();
                            if ("RUNNING".equals(item.getResolveStatus())) {
                                //list.remove(temp);// 出现java.util.ConcurrentModificationException
                                orderJsonList.add(item);
                                iterator.remove();// 推荐使用
                            }

                        }
                        orderJsonList.addAll(result.getData());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                orderAdapter.notifyDataSetChanged();
                                swipeRefresh.setRefreshing(false);
                            }
                        });
                        return;
                    } else {
                        prompt = "接单失败";
                    }
                } else {
                    prompt = "网络请求失败！";
                }
                final String finalPrompt = prompt;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(OrderActivity.this, finalPrompt, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void init() {

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshOrders();
            }
        });

        mybar_title_tv.setText("维修订单");

        mybar_back_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderActivity.this.finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        orderAdapter = new OrderAdapter(orderJsonList);
        recyclerView.setAdapter(orderAdapter);
    }

    public static void launchActivity(Context context, User user) {
        Intent intent = new Intent(context, OrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAM_USER, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void refreshOrders() {
        swipeRefresh.setRefreshing(true);
        requestOrderList();
        Log.i(TAG, "onCreate: 列表中的数据=====>" + new Gson().toJson(orderJsonList));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orderAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
