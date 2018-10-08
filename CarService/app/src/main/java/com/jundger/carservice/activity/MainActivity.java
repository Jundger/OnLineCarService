package com.jundger.carservice.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.R;
import com.jundger.carservice.adapter.MyFragmentPagerAdapter;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.base.ActivityCollector;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.bean.json.OrderJson;
import com.jundger.carservice.constant.APPConsts;
import com.jundger.carservice.fragment.MainPageFragment;
import com.jundger.carservice.fragment.MaintainFragment;
import com.jundger.carservice.fragment.MineFragment;
import com.jundger.carservice.fragment.RepairFragment;
import com.jundger.carservice.bean.User;
import com.jundger.carservice.util.InjectUtil;


import java.util.ArrayList;
import java.util.Map;

import static com.jundger.carservice.constant.APPConsts.MESSAGE_ORDER_ACCEPT;
import static com.jundger.carservice.constant.APPConsts.ORDER_FINISH_RESPONSE;
import static com.jundger.carservice.constant.APPConsts.ORDER_KEY_CODE;
import static com.jundger.carservice.constant.APPConsts.ORDER_KEY_MESSAGE;
import static com.jundger.carservice.constant.APPConsts.ORDER_NOTIFICATION_FINISH_TITLE;
import static com.jundger.carservice.constant.APPConsts.PUSH_TYPE_MESSAGE;
import static com.jundger.carservice.constant.APPConsts.PUSH_TYPE_NOTIFY;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        MainPageFragment.OnFragmentInteractionListener,
        RepairFragment.OnFragmentInteractionListener,
        MaintainFragment.OnFragmentInteractionListener,
        MineFragment.OnFragmentInteractionListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";
    public static final String TRANSMIT_PARAM = "USER";

    private static final int LOCATION_CODE = 1;
    private static String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    private User user;

    public OrderJson orderJson;

    @InjectView(R.id.main_page_rl)
    private RelativeLayout main_page_rl;

    @InjectView(R.id.repair_page_rl)
    private RelativeLayout repair_page_rl;

    @InjectView(R.id.maintain_page_rl)
    private RelativeLayout maintain_page_rl;

    @InjectView(R.id.mine_page_rl)
    private RelativeLayout mine_page_rl;

    @InjectView(R.id.viewPager)
    private ViewPager myViewPager;// 要使用的ViewPager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);

        InjectUtil.InjectView(this); // 自定义控件绑定注解

        main_page_rl.setOnClickListener(this);
        repair_page_rl.setOnClickListener(this);
        maintain_page_rl.setOnClickListener(this);
        mine_page_rl.setOnClickListener(this);

        user = (User) getIntent().getSerializableExtra(TRANSMIT_PARAM);

        Log.i(TAG, "onCreate: user----->" + new Gson().toJson(user));

        registerMessageReceiver();
        initFragment();
        requestLocationPermiss();
    }

    private void requestLocationPermiss() {
        int i = ContextCompat.checkSelfPermission(MainActivity.this, permissions[0]);
        if (i != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions, LOCATION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意
                } else {
                    // 权限被用户拒绝
                    Toast.makeText(MainActivity.this, "请开启定位权限", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initFragment() {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(MainPageFragment.newInstance());
        fragmentList.add(new RepairFragment());
        fragmentList.add(new MaintainFragment());
        fragmentList.add(MineFragment.newInstance(user));
        MyFragmentPagerAdapter myFragmentAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);
        myViewPager.addOnPageChangeListener(this);

        main_page_rl.setSelected(true);
        repair_page_rl.setSelected(false);
        maintain_page_rl.setSelected(false);
        mine_page_rl.setSelected(false);
    }

    public static void launchActivity(Context context, User user) {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSMIT_PARAM, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_page_rl:
                myViewPager.setCurrentItem(0);
                break;
            case R.id.repair_page_rl:
                myViewPager.setCurrentItem(1);
                break;
            case R.id.maintain_page_rl:
                myViewPager.setCurrentItem(2);
                break;
            case R.id.mine_page_rl:
                myViewPager.setCurrentItem(3);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                main_page_rl.setSelected(true);
                repair_page_rl.setSelected(false);
                maintain_page_rl.setSelected(false);
                mine_page_rl.setSelected(false);
                break;
            case 1:
                main_page_rl.setSelected(false);
                repair_page_rl.setSelected(true);
                maintain_page_rl.setSelected(false);
                mine_page_rl.setSelected(false);
                break;
            case 2:
                main_page_rl.setSelected(false);
                repair_page_rl.setSelected(false);
                maintain_page_rl.setSelected(true);
                mine_page_rl.setSelected(false);
                break;
            case 3:
                main_page_rl.setSelected(false);
                repair_page_rl.setSelected(false);
                maintain_page_rl.setSelected(false);
                mine_page_rl.setSelected(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public ViewPager getMyViewPager() {
        return myViewPager;
    }


    /**
     * for receive customer msg from jpush server
     */
    private MessageReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(APPConsts.MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (APPConsts.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String title = intent.getStringExtra(APPConsts.KEY_TITLE);
                    String type = intent.getStringExtra(APPConsts.KEY_TYPE);
                    String extras = intent.getStringExtra(APPConsts.KEY_EXTRAS);
                    Log.i(TAG, "TYPE===>" + type + " | TITLE===>" + title + "  ====>\n" + extras);

                    switch (type) {
                        case PUSH_TYPE_NOTIFY:
                            processNotification(title, extras);
                            break;
                        case PUSH_TYPE_MESSAGE:
                            processMessage(title, extras);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void processNotification(String title, String extras) {
        if (null == title) {
            Log.i(TAG, "processNotification: 消息title为空");
            return;
        }
        switch (title) {
            // 从维修人员发起的订单结束请求
            case ORDER_NOTIFICATION_FINISH_TITLE:
                Log.i(TAG, "processNotification: 从维修人员发起的订单结束请求" + extras);
                break;
        }
    }

    private void processMessage(String title, String extras) {
        if (null == title) {
            Log.i(TAG, "processMessage: 消息title为空");
            return;
        }
        switch (title) {
            // 订单结束请求的响应结果通知
            case ORDER_FINISH_RESPONSE:
                Map<String, Object> response = new Gson().fromJson(extras, new TypeToken<Map<String, Object>>() {
                }.getType());
                Log.i(TAG, "processMessage: 订单请求响应结果-->" + new Gson().toJson(response));
                if (response != null) {
                    String msg = (String) response.get(ORDER_KEY_MESSAGE);
                    ActivityCollector.getActivity(OrderDetailActivity.class).processOrderFinishResponse(msg);
                }
                break;
            // 创建的订单被维修人员接收后的反馈消息
            case MESSAGE_ORDER_ACCEPT:
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                orderJson = gson.fromJson(extras, new TypeToken<OrderJson>() {}.getType());
                OrderDetailActivity.launchActivity(MainActivity.this, orderJson);

                // 判断OrderActivity是否已经存在，如果存在则更新其显示内容
//                OrderActivity orderActivity = ActivityCollector.getActivity(OrderActivity.class);
//                if (null != orderActivity) {
//                    Log.i(TAG, "processMessage: 接单活动已经存在！！");
//                    orderActivity.orderJsonList.add(orderJson);
//                    orderActivity.refreshOrders();
//                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
