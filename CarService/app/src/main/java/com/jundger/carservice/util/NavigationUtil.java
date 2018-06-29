package com.jundger.carservice.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;

import java.net.URLEncoder;

/**
 * Title: CarService
 * Date: Create in 2018/5/3 18:45
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class NavigationUtil {

    public static void toNavigation(Context context, LatLng latLng) {
        //1.判断用户手机是否安装高德地图APP
        boolean isInstalled = isPkgInstalled("com.autonavi.minimap", context);
        //2.首选使用高德地图APP完成导航
        if (isInstalled) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("androidamap://navi?");
            try {
                //填写应用名称
                stringBuilder.append("sourceApplication=").append(URLEncoder.encode("CarService", "utf-8"));
                //导航目的地
                stringBuilder.append("&poiname=").append(URLEncoder.encode("POI_NAME", "utf-8"));
                //目的地经纬度
                stringBuilder.append("&lat=").append(latLng.latitude);
                stringBuilder.append("&lon=").append(latLng.longitude);
                stringBuilder.append("&dev=1&style=2");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "高德地图启调失败", Toast.LENGTH_SHORT).show();
            }
            //调用高德地图APP
            Intent intent = new Intent();
            intent.setPackage("com.autonavi.minimap");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setAction(Intent.ACTION_VIEW);
            //传递组装的数据
            intent.setData(Uri.parse(stringBuilder.toString()));
            context.startActivity(intent);
        } else if (isPkgInstalled("com.baidu.BaiduMap", context)) {
            try {
//                Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:"+"29.457349,106.524505"+"|name:"+"重庆理工大学"+"&destination=latlng:"+"29.458122,106.531815"+"|name:"+"维修点"+"&mode=driving&src=重庆快易科技|CC房车-车主#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                Intent intent = new Intent();
                intent.setData(Uri.parse("baidumap://map/navi?location=" + latLng.latitude + "," + latLng.longitude));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "百度地图启调失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "本机没有可使用的地图软件", Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isPkgInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
