package com.jundger.carservice.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.R;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.bean.ResultArray;
import com.jundger.carservice.bean.ResultObject;
import com.jundger.carservice.bean.ServicePoint;
import com.jundger.carservice.bean.SiteLocation;
import com.jundger.carservice.constant.APPConsts;
import com.jundger.carservice.constant.Actions;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MapActivity extends BaseActivity implements  LocationSource, AMapLocationListener {


    private AMap aMap; // 地图对象
    private MapView mapView; // 地图控件

    // 定位需要的声明
    private AMapLocationClient mLocationClient = null;// 定位发起端
    private AMapLocationClientOption mLocationOption = null;// 定位参数
    private LocationSource.OnLocationChangedListener mListener = null;// 定位监听器

    // 标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    private Toolbar mToolbar;
    private TextView location_city_info_tv;

    private List<SiteLocation> siteLocationList;

    private static final String TAG = "MapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mToolbar = findViewById(R.id.map_activity_tb);
        location_city_info_tv = findViewById(R.id.location_city_info_tv);
//        mToolbar.setNavigationIcon(R.mipmap.location_icon);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true); // 显示返回按钮
            actionBar.setHomeButtonEnabled(true); // 返回按钮可点击
        }

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();

        // 设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        // 设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);

        // 定位的小图标 默认是蓝点
//        MyLocationStyle myLocationStyle = new MyLocationStyle();
//        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_map_icon));
//        myLocationStyle.radiusFillColor(android.R.color.transparent);
//        myLocationStyle.strokeColor(android.R.color.transparent);
//        aMap.setMyLocationStyle(myLocationStyle);

        // 开始定位
        initLocation();

        // 将地图移动到默认定位点（重庆市城区）
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(APPConsts.DEFAULT_LATITUDE, APPConsts.DEFAULT_LONGITUDE)));
    }

    // 定位
    private void initLocation() {
        // 初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        // 设置定位回调监听
        mLocationClient.setLocationListener(this);
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式，Hight_Accuracy为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiScan(true); // setWifiScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(true);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        if (requestLocationPermission()) {
            // 启动定位
            mLocationClient.startLocation();
        }

        AMap.OnInfoWindowClickListener infoWindowClickListener = new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i(TAG, "onInfoWindowClick: " + marker.getTitle());
                for (SiteLocation site : siteLocationList) {
                    if (site.getName().equals(marker.getTitle())) {
                        RequestBody requestBody = new FormBody.Builder()
                                .add("site_name", marker.getTitle())
                                .build();
                        HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_QUERY_SITE), requestBody, new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Log.i(TAG, "onFailure: 查询维修点信息失败");
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                String res = response.body().string();
                                ResultObject<ServicePoint> result = new Gson().fromJson(res, new TypeToken<ResultObject<ServicePoint>>(){}.getType());
                                if (UrlConsts.CODE_SUCCESS.equals(result.getCode())) {
                                    final ServicePoint servicePoint = result.getData();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ShopDetailActivity.launchActivity(MapActivity.this, servicePoint);
                                        }
                                    });
                                }
                            }
                        });
                        return ;
                    }
                }
            }
        };
        aMap.setOnInfoWindowClickListener(infoWindowClickListener);
    }

    /**
     * 在运行时一次性申请所有的权限
     * 回调：onRequestPermissionsResult()
     */
    private boolean requestLocationPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapActivity.this, permissions, 1);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 权限申请回调结果处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "请开启要求的所有权限", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    //启动定位
                    mLocationClient.startLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                }
            default: break;
        }
    }

    public static void launchActivity(Context context) {
        Intent intent = new Intent(context, MapActivity.class);
        context.startActivity(intent);
    }

    /**
     * 定位监听
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                // 定位成功回调信息，设置相关消息
                aMapLocation.getLocationType(); // 获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();     // 获取纬度
                aMapLocation.getLongitude();    // 获取经度
                aMapLocation.getAccuracy();     // 获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(aMapLocation.getTime());
//                df.format(date);//定位时间
                aMapLocation.getAddress();      // 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();      // 国家信息
                aMapLocation.getProvince();     // 省信息
                aMapLocation.getCity();         // 城市信息
                aMapLocation.getDistrict();     // 城区信息
                aMapLocation.getStreet();       // 街道信息
                aMapLocation.getStreetNum();    // 街道门牌号信息
                aMapLocation.getCityCode();     // 城市编码
                aMapLocation.getAdCode();       // 地区编码

                aMapLocation.getLatitude();
                aMapLocation.getLongitude();

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    // 设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    // 将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    // 点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    // 添加当前位置图钉
                    aMap.addMarker(getMarkerOptions(aMapLocation));

                    // 显示所有维修点的坐标图钉
                    showSiteLocation();

                    // Toolbar右上角显示定位点城市
                    location_city_info_tv.setText(aMapLocation.getCity());
                    // 获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + "" + aMapLocation.getProvince() + "" + aMapLocation.getCity() + "" + aMapLocation.getProvince() + "" + aMapLocation.getDistrict() + "" + aMapLocation.getStreet() + "" + aMapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    Log.d("AmapDebug", "location Info -->:"
                            + buffer.toString());
                    Log.d("AmapDebug", "经度 -->:"
                            + aMapLocation.getLatitude() + "  纬度 -->" + aMapLocation.getLongitude());
                    isFirstLoc = false;
                }

            } else {
                // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapDebug", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());

//                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showSiteLocation() {

        HttpUtil.okHttpGet(UrlConsts.getRequestURL(Actions.ACTION_GET_SITE_LOCATION), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MapActivity.this, "维修点坐标获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();

                ResultArray<SiteLocation> result = new Gson().fromJson(res, new TypeToken<ResultArray<SiteLocation>>(){}.getType());

                if (UrlConsts.CODE_SUCCESS.equals(result.getCode()) && !result.getData().isEmpty()) {
                    siteLocationList = result.getData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (SiteLocation site : siteLocationList) {
                                aMap.addMarker(getOptions(site));
                            }
                        }
                    });
                }

            }
        });
    }

    // 自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
        // 设置图钉选项
        MarkerOptions options = new MarkerOptions();
        // 图标
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_map_icon));
        // 位置
        options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
        // 标题
        options.title("Location:");
        // 内容
        options.snippet((amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum()));
        // 设置多少帧刷新一次图片资源
        options.period(60);

        return options;

    }

    private MarkerOptions getOptions(SiteLocation site) {
        // 设置图钉选项
        MarkerOptions options = new MarkerOptions();
        // 图标
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_site_location));
        // 位置
        options.position(new LatLng(site.getLatitude(), site.getLongitude()));
        // 标题
        options.title(site.getName());
        // 内容
        options.snippet(site.getAddress());
        // 设置多少帧刷新一次图片资源
        options.period(60);

        return options;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            MapActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

}
