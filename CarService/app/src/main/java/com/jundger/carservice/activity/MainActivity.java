package com.jundger.carservice.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
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
import com.jundger.carservice.constant.Actions;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.fragment.MainPageFragment;
import com.jundger.carservice.fragment.MaintainFragment;
import com.jundger.carservice.fragment.MineFragment;
import com.jundger.carservice.fragment.RepairFragment;
import com.jundger.carservice.bean.User;
import com.jundger.carservice.util.HttpUtil;
import com.jundger.carservice.util.InjectUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.jundger.carservice.constant.APPConsts.CONTENT_ORDER_ACCEPT;
import static com.jundger.carservice.constant.APPConsts.CUSTOMER_TO_REPAIRMAN_RESPONSE;
import static com.jundger.carservice.constant.APPConsts.ORDER_FINISH_REQUEST;
import static com.jundger.carservice.constant.APPConsts.ORDER_FINISH_RESPONSE;
import static com.jundger.carservice.constant.APPConsts.ORDER_KEY_CODE;
import static com.jundger.carservice.constant.APPConsts.ORDER_KEY_NAME;
import static com.jundger.carservice.constant.APPConsts.ORDER_KEY_ORDERNO;

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

    private List<OrderJson> jsonList = new ArrayList<>();

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
                    String type = intent.getStringExtra(APPConsts.KEY_MESSAGE);
                    String extras = intent.getStringExtra(APPConsts.KEY_EXTRAS);
                    Log.i(TAG, "MESSAGE===>" + type + "  ====>\n" + extras);

                    switch (type) {
                        case CONTENT_ORDER_ACCEPT:
//                            Boolean is = MainPageFragment.processAcceptOrder();

                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            OrderJson orderJson = gson.fromJson(extras, new TypeToken<OrderJson>() {}.getType());
//                            Log.i(TAG, "Extras===> " + gson.toJson(orderJson));
                            jsonList.add(orderJson);
                            OrderDetailActivity.launchActivity(MainActivity.this, orderJson);

                            // 判断OrderActivity是否已经存在，如果存在则更新其显示内容
                            OrderActivity orderActivity = ActivityCollector.getActivity(OrderActivity.class);
                            if (null != orderActivity) {
                                Log.i(TAG, "onReceive: 接单活动已经存在！！");
                                orderActivity.orderJsonList.add(orderJson);
                                orderActivity.refreshOrders();
                            } else {
                                Log.i(TAG, "onReceive: 接单活动不存在！！");
                            }
                            break;
                        case ORDER_FINISH_REQUEST:
                            Map<String, Object> request = new Gson().fromJson(extras, new TypeToken<Map<String, Object>>() {}.getType());

                            final String orderNo = (String) request.get(ORDER_KEY_ORDERNO);
                            String name = (String) request.get(ORDER_KEY_NAME);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setIcon(R.mipmap.app_log);
                            builder.setTitle("订单");
                            builder.setMessage(name + "请求结束订单，是否同意？");
                            builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("orderNo", orderNo)
                                            .add("operate", CUSTOMER_TO_REPAIRMAN_RESPONSE)
                                            .add("answer", "1")
                                            .build();
                                    HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_FINISH_ORDER), requestBody, null);
                                }
                            });

                            builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    RequestBody requestBody = new FormBody.Builder()
                                            .add("orderNo", orderNo)
                                            .add("operate", CUSTOMER_TO_REPAIRMAN_RESPONSE)
                                            .add("answer", "0")
                                            .build();
                                    HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_FINISH_ORDER), requestBody, null);
                                }
                            });
                            builder.show();
                            break;
                        case ORDER_FINISH_RESPONSE:
                            Map<String, Object> response = new Gson().fromJson(extras, new TypeToken<Map<String, Object>>() {}.getType());
                            Integer code = (Integer) response.get(ORDER_KEY_CODE);
                            if (code == 1) {
                                Toast.makeText(MainActivity.this, "订单结束成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "订单结束请求被拒绝！", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default: break;
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
