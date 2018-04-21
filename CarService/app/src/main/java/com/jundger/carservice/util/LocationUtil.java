package com.jundger.carservice.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Title: CarService
 * Date: Create in 2018/4/21 20:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class LocationUtil {

    public static Location requestLocation(Context context) {

        LocationManager locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);//低精度，如果设置为高精度，依然获取不了location。
        criteria.setAltitudeRequired(false);//不要求海拔
        criteria.setBearingRequired(false);//不要求方位
        criteria.setCostAllowed(true);//允许有花费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//低功耗
        //从可用的位置提供器中，匹配以上标准的最佳提供器
        String locationProvider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "requestLocation: 请开启相应权限!");
            return null;
        }

        //监视地理位置变化
//        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);

        return locationManager.getLastKnownLocation(locationProvider);
    }

    static LocationListener locationListener = new LocationListener() {
        // 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: " + "longitude-->" + location.getLongitude() + " | latitude-->" + location.getLatitude());
        }

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String s) {
            Log.d(TAG, "onProviderEnabled: " + s + ".." + Thread.currentThread().getName());
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String s) {
            Log.d(TAG, "onProviderDisabled: " + s + ".." + Thread.currentThread().getName());
        }
    };
}
