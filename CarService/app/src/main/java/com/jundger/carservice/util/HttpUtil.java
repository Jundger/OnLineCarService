package com.jundger.carservice.util;

import android.util.Log;

import com.jundger.carservice.util.StreamTool;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Title: CarService
 * Date: Create in 2018/4/21 20:52
 * Description:
 *
 * @author Jundger
 * @version 1.0
 */

public class HttpUtil {
    private static final String TAG = "HttpUtil";

    public static void okHttpGet(String url, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void okHttpPost(String url, RequestBody body, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendHttpRequest(final String address, final Map<String, String> params, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    StringBuilder data = new StringBuilder();
                    if (params != null && !params.isEmpty()) {
                        for (Map.Entry<String, String> entry : params.entrySet()) {
                            data.append(entry.getKey()).append("=");
                            data.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                            data.append("&");
                        }
                        data.deleteCharAt(data.length() - 1);
                    }
                    byte[] entity = data.toString().getBytes();

                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length", String.valueOf(entity.length));
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(entity);

                    if (connection.getResponseCode() == 200) { //请求成功
                        InputStream is = connection.getInputStream();
                        String result = new String(StreamTool.read(is));
                        if (listener != null) {
                            listener.onFinish(result);
                        }
                    } else {
                        if (listener != null) {
                            listener.onError("服务器请求失败！");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "HttpUtil exception: " + e.toString());
                    if (listener != null) {
                        listener.onError("未知错误！");
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    // Http请求回调接口
    public interface HttpCallbackListener {

        void onFinish(String response); // 请求成功

        void onError(String error); // 请求失败
    }
}

