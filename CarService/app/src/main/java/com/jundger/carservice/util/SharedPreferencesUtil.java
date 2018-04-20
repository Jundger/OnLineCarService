package com.jundger.carservice.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.jundger.carservice.constant.APPConsts;

import java.io.IOException;
import java.util.List;

/**
 * Created by Jundger on 2018/1/3.
 */

public class SharedPreferencesUtil {

    public static void save(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APPConsts.SHARED_SAVE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void save(Context context, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APPConsts.SHARED_SAVE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void save(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APPConsts.SHARED_SAVE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void save(Context context, String key, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APPConsts.SHARED_SAVE_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void save(Context context, String key, float value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APPConsts.SHARED_SAVE_NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static Object query(Context context, String key, String type) {
        try {
            type = type.toUpperCase();
            if (type.equals("STRING")) {
                return context.getSharedPreferences(APPConsts.SHARED_SAVE_NAME, Context.MODE_PRIVATE).getString(key, null);
            } else if (type.equals("INT") | type.equals("INTEGER")) {
                return context.getSharedPreferences(APPConsts.SHARED_SAVE_NAME, Context.MODE_PRIVATE).getInt(key, -1);
            } else if (type.equals("BOOLEAN") | type.equals("BOOL")) {
                return context.getSharedPreferences(APPConsts.SHARED_SAVE_NAME, Context.MODE_PRIVATE).getBoolean(key, false);
            } else if (type.equals("LONG")) {
                return context.getSharedPreferences(APPConsts.SHARED_SAVE_NAME, Context.MODE_PRIVATE).getLong(key, -1);
            } else if (type.equals("FLOAT")) {
                return context.getSharedPreferences(APPConsts.SHARED_SAVE_NAME, Context.MODE_PRIVATE).getFloat(key, -1);
            } else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
