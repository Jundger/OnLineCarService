package com.jundger.carservice.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.R;
import com.jundger.carservice.activity.OrderActivity;
import com.jundger.carservice.adapter.FaultInfoAdapter;
import com.jundger.carservice.base.MyApplication;
import com.jundger.carservice.bean.ServicePoint;
import com.jundger.carservice.bean.User;
import com.jundger.carservice.bean.json.Customer;
import com.jundger.carservice.bean.json.OrderJson;
import com.jundger.carservice.constant.APPConsts;
import com.jundger.carservice.constant.Actions;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.bean.json.FaultCode;
import com.jundger.carservice.bean.ResultArray;
import com.jundger.carservice.util.HttpUtil;
import com.jundger.carservice.util.LocationUtil;
import com.jundger.carservice.util.NavigationUtil;
import com.jundger.carservice.util.SharedPreferencesUtil;
import com.shinelw.library.ColorArcProgressBar;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_CANCELED;
import static com.jundger.carservice.service.BlueToothService.MY_UUID;

public class MainPageFragment extends Fragment {

    private String mBrand; // 汽车品牌
    private String mBrandNo; // 型号
    private User user;

    private OnFragmentInteractionListener mListener;

    private ColorArcProgressBar colorArcProgressBar;
    private FloatingActionButton getInfoFab;
    private Button request_service_btn;
    private RecyclerView recyclerView;
    private ImageView connect_state_iv;
    private Toolbar toolbar;
    private TextView fault_info_title_tv;
    private LinearLayout emptyView;
    private CollapsingToolbarLayout collapsingLayout;

    private CircleProgressDialog circleProgressDialog;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice connectDevice;

    IntentFilter filter;
    BroadcastReceiver receiver;
    Boolean isReceiverRegister;

    List<String> deviceList;
    List<BluetoothDevice> devices;
    Set<BluetoothDevice> pairedDevices;
    ConnectedThread connectedThread;
    private Boolean isBluetoothConnect;
    private String receiveData;

    private String scanHint;

    private static final String TAG = "MainPageFragment";

    private List<FaultCode> infoList = new ArrayList<>();
    private FaultInfoAdapter faultInfoAdapter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case APPConsts.CONNECT_SUCCESS:
                    connectedThread = new ConnectedThread((BluetoothSocket) msg.obj);
                    connectedThread.write(APPConsts.BLUETOOTH_READ_COMMAND.getBytes());
                    connectedThread.start();
                    stopProgressDialog();
                    Toast.makeText(getActivity(), "蓝牙连接成功，请再次点击按钮获取数据", Toast.LENGTH_SHORT).show();
                    break;
                case APPConsts.MESSAGE_READ:
                    receiveData = (String) msg.obj;
                    Log.d(TAG, "handleMessage: recieve from bluetooth-->" + receiveData);
                    break;
                case APPConsts.SHOW_RESULT:
                    Log.d(TAG, "handleMessage: SHOW_RESULT");
                    queryCodeAndShowResult(receiveData);
                    break;
                default:
                    Log.d(TAG, "handleMessage: no such msg.what!");
                    break;
            }
        }
    };

    public MainPageFragment() {
    }

    public static MainPageFragment newInstance() {
        return new MainPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated: ");
        bindView();
        init();

        // 从Android 6.0开始，必须先申请定位权限才能发现蓝牙设备
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "startDiscovery: enter checkPermission");
            checkBTPermissions();
        }
    }

    private void queryCodeAndShowResult(String receive_data) {
        if (receive_data == null) {
            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
            return ;
        }

        Integer count = (int) receive_data.charAt(0) - 48;
        Log.i(TAG, "queryCodeAndShowResult: TROUBLE CODE NUMBER-->" + count);

        List<String> list = new ArrayList<>();
        if (count == 0) {
            Log.i(TAG, "queryCodeAndShowResult: NO TROUBLE CODE!");
            Toast.makeText(getActivity(), "当前没有故障码", Toast.LENGTH_SHORT).show();
            return ;
        } else {
            String[] code = receive_data.substring(2).split(",");
            list.addAll(Arrays.asList(code));
        }

        Map<String, Object> sendMsg = new HashMap<>();
        sendMsg.put("brand", mBrand);
        sendMsg.put("code", list);
        String json = new Gson().toJson(sendMsg);
        Log.i(TAG, "json-->" +  json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);

        HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_QUERY_CODE), requestBody, new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG, "onFailure: | okHttp Error!");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "解析失败，请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                if (response.code() != 200) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), response.code() + "-请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    return ;
                }
                String res = response.body().string();
                Log.i(TAG, "onResponse: receive from server: " + res);
                // Gson解析json数据
                final ResultArray<FaultCode> result = new Gson().fromJson(res, new TypeToken<ResultArray<FaultCode>>(){}.getType());

                if (UrlConsts.CODE_SUCCESS.equals(result.getCode())) {
                    infoList.clear();
                    infoList.addAll(result.getData());

                    refreshRecyclerView();
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void refreshRecyclerView() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        faultInfoAdapter.notifyDataSetChanged();
                        if (!infoList.isEmpty()) {
                            emptyView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            fault_info_title_tv.setVisibility(View.VISIBLE);
                            request_service_btn.setVisibility(View.VISIBLE);
                        } else {
                            emptyView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            fault_info_title_tv.setVisibility(View.GONE);
                            request_service_btn.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }

    private void bindView() {
        getInfoFab = getActivity().findViewById(R.id.get_info_fab);
        toolbar = getActivity().findViewById(R.id.main_page_fragment_tb);
        recyclerView = getActivity().findViewById(R.id.fault_info_recycler_view);
        connect_state_iv = getActivity().findViewById(R.id.connect_state_iv);
        emptyView = getActivity().findViewById(R.id.empty_layout_ll);
        fault_info_title_tv = getActivity().findViewById(R.id.fault_info_title_tv);
        collapsingLayout = getActivity().findViewById(R.id.collapsing_toolbar);
        colorArcProgressBar = getActivity().findViewById(R.id.bar1);
        request_service_btn = getActivity().findViewById(R.id.request_service_btn);
    }

    private void init() {
        isReceiverRegister = false;

        colorArcProgressBar.setBackgroundResource(R.drawable.car_logo);

        if (connectDevice != null && connectDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
            isBluetoothConnect = true;
            connect_state_iv.setImageResource(R.drawable.has_connect_white);
            colorArcProgressBar.setCurrentValues(100);
        } else {
            isBluetoothConnect = false;
            connect_state_iv.setImageResource(R.drawable.no_connect_red);
        }
        if (!infoList.isEmpty()) {
            emptyView.setVisibility(View.GONE);
            fault_info_title_tv.setVisibility(View.VISIBLE);
            request_service_btn.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        faultInfoAdapter = new FaultInfoAdapter(infoList);
        recyclerView.setAdapter(faultInfoAdapter);

        getInfoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBluetoothConnect) {
                    Log.i(TAG, "onClick: 打开蓝牙");
                    openBlueTooth();
                } else {
                    Log.i(TAG, "onClick: 蓝牙已经处于打开状态");
                    if (mBrand == null || mBrandNo == null || "".equals(mBrand) || "".equals(mBrandNo)) {
                        Toast.makeText(getActivity(), "请先到个人中心设置车辆信息", Toast.LENGTH_SHORT).show();
                    } else {
                        connectedThread.write(APPConsts.BLUETOOTH_READ_COMMAND.getBytes());

                        colorArcProgressBar.setCurrentValues(0);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                colorArcProgressBar.setCurrentValues(100);
                                mHandler.obtainMessage(APPConsts.SHOW_RESULT).sendToTarget();
                            }
                        }, 1000);
                    }
                }
            }
        });

        setBrandTitle();

        request_service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] items = new String[]{"A.等候接单", "B.分配最近"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("请选择：")
                        .setIcon(R.mipmap.app_log)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                switch (i) {
                                    case 0:
                                        startProgressDialog("请稍候...");
                                        createOrder();
                                        break;
                                    case 1:
                                        String longitude = (String) SharedPreferencesUtil.query(getActivity(), APPConsts.SHARED_KEY_LONGITUDE, "string");
                                        String latitude = (String) SharedPreferencesUtil.query(getActivity(), APPConsts.SHARED_KEY_LATITUDE, "string");
                                        if (longitude != null && latitude != null) {
                                            RequestBody requestBody = new FormBody.Builder()
                                                    .add("longitude", longitude)
                                                    .add("latitude", latitude)
                                                    .add("radius", "50")
                                                    .add("limit", "1")
                                                    .build();
                                            HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_GET_SITE), requestBody, new Callback() {
                                                @Override
                                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getActivity(), "网络请求失败！", Toast.LENGTH_SHORT).show();
                                                        }
                                                });
                                                }

                                                @Override
                                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                                    if (response.code() == 200) {
                                                        String res = response.body().string();
                                                        ResultArray<ServicePoint> result = new Gson().fromJson(res, new TypeToken<ResultArray<ServicePoint>>(){}.getType());
                                                        if (UrlConsts.CODE_SUCCESS.equals(result.getCode())) {
                                                            ServicePoint servicePoint = result.getData().get(0);
                                                            LatLng latLng = new LatLng(servicePoint.getLatitude(), servicePoint.getLongitude());
                                                            NavigationUtil.toNavigation(getActivity(), latLng);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                        break;
                                    default: break;
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void createOrder() {
        String url = UrlConsts.getRequestURL(Actions.ACTION_CREATE_ORDER);
        final long startTime = System.currentTimeMillis();

//        List<FaultCode> codeList = new ArrayList<>();
//        FaultCode faultCode1 = new FaultCode(1, "P107801", "动力总成系统", "尼桑（日产）、英菲尼迪", "排气阀门正时控制位置传感器-电路故障", null);
//        FaultCode faultCode2 = new FaultCode(1, "B009A", "车身系统", "所有汽车制造商", "一般由安全带传感器，其电路或接头故障所致", null);
//        codeList.add(faultCode1);
//        codeList.add(faultCode2);

        Customer customer = new Customer(user);
        OrderJson orderJson = null;
        Location location = LocationUtil.requestLocation(getActivity());
        if (null != location) {
            Log.i(TAG, "当前经纬度: " + "longitude-->" + location.getLongitude() + " | latitude-->" + location.getLatitude());
            orderJson = new OrderJson(null, infoList, null, customer, null, location.getLongitude(), location.getLatitude(), new Date());
        } else {
            Log.i(TAG, "createOrder: 获取地理位置失败");
            Toast.makeText(getActivity(), "获取地理位置失败", Toast.LENGTH_SHORT).show();
            orderJson = new OrderJson(null, infoList, null, customer, null, APPConsts.DEFAULT_LONGITUDE, APPConsts.DEFAULT_LATITUDE, new Date());
        }

        String json = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(orderJson);
        Log.i(TAG, "订单创建发送数据==========》\n" +  json);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.okHttpPost(url, requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "run: 网络请求失败");
                        Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        stopProgressDialog();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String prompt;
                if (response.code() == 200) {
                    String res = response.body().string();
                    Log.i(TAG, "onResponse: 创建订单请求返回数据===>" + res);
                    Map<String, String> result = new Gson().fromJson(res, new TypeToken<Map<String, String>>() {}.getType());
                    if (UrlConsts.CODE_SUCCESS.equals(result.get(UrlConsts.KEY_RETURN_CODE))) {
                        Log.i(TAG, "onResponse: 订单创建成功，正在等待维修师傅接单...");
                        prompt = "订单创建成功，正在等待维修师傅接单...";
                        Log.i("MainActivity", "onResponse: 订单号>>>>>>>>>>>>>>>>>>>>>>>>>" + result.get(UrlConsts.KEY_RETURN_DATA));

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                circleProgressDialog.changeText(prompt);
                            }
                        });

                        long endTime = System.currentTimeMillis();
                        if (endTime - startTime < APPConsts.WAIT_ORDER_TIME) {
                            try {
                                Thread.sleep(APPConsts.WAIT_ORDER_TIME - (endTime - startTime));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        stopProgressDialog();

                    } else {
                        Log.i(TAG, "onResponse: 订单创建失败，请稍后再试");
                        prompt = "订单创建失败，请稍后再试";
                    }
                } else {
                    Log.i(TAG, "未知错误");
                    prompt = "未知错误";
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), prompt, Toast.LENGTH_SHORT).show();
                    }
                });
                stopProgressDialog();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void startProgressDialog(String msg) {
        circleProgressDialog = new CircleProgressDialog(getActivity());
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

    public static Boolean processAcceptOrder() {

//        Log.i(TAG, "processAcceptOrder: 接受订单后进来了");
//
//        OrderActivity.launchActivity(MyApplication.getContext(), 1);

        return null;
    }

    private void showListDialog(String[] items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        for (int i = 0; i < items.length; i++) {
            Log.i(TAG, "showListDialog: item" + i + ": " + items[i]);
        }
        builder.setTitle(R.string.hint_connect_bluetooth)
                .setCancelable(false)
                .setIcon(R.mipmap.app_log);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startProgressDialog("Connecting...");

                BluetoothDevice selectedDevice = devices.get(i);
                ConnectThread connect = new ConnectThread(selectedDevice);
                connect.start();
            }
        });
        builder.show();
    }

    private void openBlueTooth() {
        devices = new ArrayList<>();
        deviceList = new ArrayList<>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (null == bluetoothAdapter) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.hint_not_support_bluetooth), Toast.LENGTH_SHORT).show();
            return ;
        } else if (!bluetoothAdapter.isEnabled()) {
            turnOnBluetooth();
        } else {
            startDiscovery();
        }

        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (null == action) {
                    return;
                }
                switch (action) {
                    case BluetoothDevice.ACTION_FOUND:
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        Log.i(TAG, "onReceive: FOUND DEVICE NAME: " + device.getName());

                        String str = formShowString(device);
                        if (!isRepetition(device.getAddress())) {
                            devices.add(device);
                            deviceList.add(str);

                            scanHint += device.getName() + "\n";
                            circleProgressDialog.changeText("已扫描到 " + deviceList.size() + "个蓝牙设备:\n" + scanHint);

                            if (APPConsts.BLUETOOTH_NAME.equals(device.getName())) {
                                bluetoothAdapter.cancelDiscovery();
                                connectDevice = device;
                            }
                        }
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        Log.i(TAG, "onReceive: BluetoothAdapter.ACTION_DISCOVERY_STARTED......");
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        Log.i(TAG, "onReceive: BluetoothAdapter.ACTION_DISCOVERY_FINISHE......");
                        stopProgressDialog();
                        String[] items = (String[]) deviceList.toArray(new String[deviceList.size()]);
                        showListDialog(items);
                        break;
                    case BluetoothAdapter.ACTION_STATE_CHANGED:
                        if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
                            turnOnBluetooth();
                        }
                        break;
                    case BluetoothDevice.ACTION_ACL_CONNECTED:
                        Log.i(TAG, "onReceive: ACTION_ACL_CONNECTED");
                        isBluetoothConnect = true;
                        connect_state_iv.setImageResource(R.drawable.has_connect_white);
                        break;
                    case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                        isBluetoothConnect = false;
                        Log.i(TAG, "onReceive: ACTION_ACL_DISCONNECTED");
                        connect_state_iv.setImageResource(R.drawable.no_connect_red);
                        break;
                    default: break;
                }
            }
        };

        getActivity().registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        getActivity().registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        getActivity().registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        getActivity().registerReceiver(receiver, filter);
        isReceiverRegister = true;
    }

    private boolean isRepetition(String address) {
        for (BluetoothDevice device : devices) {
            if (device.getAddress().equals(address)) {
                return true;
            }
        }
        return false;
    }

    private void turnOnBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, APPConsts.TURN_ON_BT_REQUEST_CODE);
    }

    private void startDiscovery() {
        Log.i(TAG, "startDiscovery: begin discovery...");
        scanHint = "";

        startProgressDialog(getActivity().getString(R.string.hint_scan_bluetooth));

        pairedDevices = bluetoothAdapter.getBondedDevices();


//        // 从Android 6.0开始，必须先申请定位权限才能发现蓝牙设备
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Log.i(TAG, "startDiscovery: enter checkPermission");
//            checkBTPermissions();
//        }

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = getActivity().checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += getActivity().checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {
                Log.i(TAG, "checkBTPermissions: request permission");
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }

    private String formShowString(BluetoothDevice device) {

        String suffix = "";
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice d : pairedDevices) {
                if (d.getName().equals(device.getName())) {
                    suffix = " (paired) ";
                    break;
                }
            }
        }
        return device.getName() + suffix + "\n" + device.getAddress();
    }

    private void setBrandTitle() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<User> userList = DataSupport.findAll(User.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        user = userList.get(0);
                        if (user != null && user.getBrand() != null && user.getBrand_no() != null) {
                            mBrand = user.getBrand();
                            mBrandNo = user.getBrand_no();
                            collapsingLayout.setTitle(mBrand + " " + mBrandNo);
                        } else {
                            collapsingLayout.setTitle("----");
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.hint_request_open_bluetooth), Toast.LENGTH_SHORT).show();
        } else {
            startDiscovery();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onPause() {
        super.onPause();

        stopProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (isReceiverRegister) {
            getActivity().unregisterReceiver(receiver);
            isReceiverRegister = false;
        }
    }

    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            Log.i(TAG, "construct");
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.i(TAG, "get socket failed");

            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            bluetoothAdapter.cancelDiscovery();
            Log.i(TAG, "connect - run");
            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
                Log.i(TAG, "connect - succeeded");
            } catch (IOException connectException) {
                Log.i(TAG, "connect failed");
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.i(TAG, "close socket failed");
                }
                return;
            }

            // Do work to manage the connection (in a separate thread)

            mHandler.obtainMessage(APPConsts.CONNECT_SUCCESS, mmSocket).sendToTarget();
        }


        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    /**
                     * 此处存在接收字符串被分段的现象
                     * 因此等待一定时间后再接收
                     */
                    if (mmInStream.available() <= 0) {
                        continue;
                    } else {
                        try {
                            Thread.sleep(80);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    bytes = mmInStream.read(buffer);

                    String str = new String(buffer, 0, bytes, "utf-8");
                    mHandler.obtainMessage(APPConsts.MESSAGE_READ, str).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}
