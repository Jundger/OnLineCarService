package com.jundger.carservice.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.jundger.carservice.annotation.InjectView;

import java.lang.reflect.Field;

/**
 * Created by 14246 on 2017/12/22.
 */

public class InjectUtil {
    private static final String TAG = "InjectUtil";

    public static void InjectView(Activity activity) {
        // 获取类中的所有field（域/变量）
        Field[] fields = activity.getClass().getDeclaredFields();
        // 对获取到的field做遍历
        for (Field field : fields) {
            // 对遍历到的field做判断，是否带特定注解标识
            if (field.isAnnotationPresent(InjectView.class)) {
                // 获取到该field的注解
                InjectView injectView = field.getAnnotation(InjectView.class);
                // 获取到该field的注解的value
                int viewId = injectView.value();
                // 绑定控件
                View view = activity.findViewById(viewId);
                if (null != view) {
                    // 如果field是private的，则必须调用，否则会抛出IllegalAccessException异常
                    field.setAccessible(true);
                    try {
                        // 对该field做控件绑定操作
                        field.set(activity, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i(TAG, "Error: The viewId is not exist!");
                }                
            }
        }
    }
}
