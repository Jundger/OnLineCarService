package com.jundger.carservice.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.jundger.carservice.constant.UrlConsts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Title: CarService
 * Date: Create in 2018/4/21 20:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class JsonParser {
    private static final String TAG = "JsonParser";

    public static Map<String, String> parseLogin(String jsonData) {
        Map<String, String> returnMap = new HashMap<>();
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

    public static String parseRegister(String jsonData) {
        String msg = null;
        try {
            JSONObject obj = new JSONObject(jsonData);
            msg = obj.getString(UrlConsts.KEY_RETURN_MSG);
        } catch (JSONException e) {
            Log.e(TAG, "parseRegister: error!");
            e.printStackTrace();
        }
        return msg;
    }
}
