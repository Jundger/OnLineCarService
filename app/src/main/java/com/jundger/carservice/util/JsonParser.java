package com.jundger.carservice.util;

import android.text.TextUtils;
import android.util.Log;

import com.jundger.carservice.constant.UrlConsts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jundger on 2018/1/2.
 */

public class JsonParser {
    private static final String TAG = "JsonParser";

    public static Map<String, String> parseLogin(String jsonData) {
        Map<String, String> returnMap = new HashMap<>();;
        try {
            JSONObject obj = new JSONObject(jsonData);
            String code = obj.getString(UrlConsts.KEY_RETURN_CODE);
            String msg = obj.getString(UrlConsts.KEY_RETURN_MSG);
            if (UrlConsts.REQUEST_SUCCESS_CODE.equals(code)) {
                String token = obj.getString(UrlConsts.KEY_RETURN_TOKEN);
                String user = obj.getString(UrlConsts.KEY_RETURN_NAME);
                returnMap.put(UrlConsts.KEY_RETURN_CODE, code);
                returnMap.put(UrlConsts.KEY_TOKEN, token);
                returnMap.put(UrlConsts.KEY_USERNAME, user);
                returnMap.put(UrlConsts.KEY_RETURN_MSG, msg);
            } else {
                returnMap.put(UrlConsts.KEY_RETURN_CODE, code);
                returnMap.put(UrlConsts.KEY_RETURN_MSG, msg);
            }
            return returnMap;
        } catch (JSONException e) {
            Log.e(TAG, "parseLogin: error!");
            e.printStackTrace();
        }
        return null;
    }
}