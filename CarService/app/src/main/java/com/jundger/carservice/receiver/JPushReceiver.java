package com.jundger.carservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.activity.MainActivity;
import com.jundger.carservice.activity.OrderDetailActivity;
import com.jundger.carservice.base.ActivityCollector;
import com.jundger.carservice.bean.json.OrderJson;
import com.jundger.carservice.constant.APPConsts;
import com.jundger.carservice.util.FormatCheckUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static com.jundger.carservice.base.MyApplication.isShowFinishRequestDialog;
import static com.jundger.carservice.constant.APPConsts.ORDER_NOTIFICATION_FINISH_TITLE;
import static com.jundger.carservice.constant.APPConsts.PUSH_TYPE_MESSAGE;
import static com.jundger.carservice.constant.APPConsts.PUSH_TYPE_NOTIFY;

public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JPushReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            String action = intent.getAction();
            assert bundle != null;
            assert action != null;
//            Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            switch (intent.getAction()) {
                case JPushInterface.ACTION_REGISTRATION_ID:
//                    String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//                    Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
//                    SharedPreferencesUtil.save(context, APPConsts.SHARED_KEY_REGISTRATION_ID, regId);
                    break;
                case JPushInterface.ACTION_MESSAGE_RECEIVED:
                    Log.d(TAG, "===============================  接收到推送下来的自定义消息Message  ==================================");
                    sendToMainActivity(context, bundle, PUSH_TYPE_MESSAGE);

                    break;
                case JPushInterface.ACTION_NOTIFICATION_RECEIVED:
                    Log.d(TAG, "===============================  接收到推送下来的通知Notification  ==================================");
                    sendToMainActivity(context, bundle, PUSH_TYPE_NOTIFY);
                    break;
                case JPushInterface.ACTION_NOTIFICATION_OPENED:
                    Log.d(TAG, "======================================  用户点击打开了通知  =========================================");
                    processOpenNotify(context, bundle);
                    break;
                case JPushInterface.ACTION_RICHPUSH_CALLBACK:
                    Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                    //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

                    break;
                case JPushInterface.ACTION_CONNECTION_CHANGE:
                    boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                    Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
                    break;
                default:
                    Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
//            Log.i(TAG, "printBundle: key-->" + key);
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    private void processOpenNotify(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.i(TAG, "processNotify: title-->" + title);
        Log.i(TAG, "processNotify: extras-->" + extras);
        if (ORDER_NOTIFICATION_FINISH_TITLE.equals(title)) {
            Map<String, Object> request = new Gson().fromJson(extras, new TypeToken<Map<String, Object>>() {
            }.getType());
            if (request != null) {
//                String orderNo = (String) request.get(ORDER_KEY_ORDERNO);
//                String name = (String) request.get(ORDER_KEY_NAME);

                OrderDetailActivity orderDetailActivity = ActivityCollector.getActivity(OrderDetailActivity.class);

                if (null != orderDetailActivity) {
                    orderDetailActivity.showFinishRequestDialog();
                } else {
                    isShowFinishRequestDialog = true;

                    OrderJson orderJson = ActivityCollector.getActivity(MainActivity.class).orderJson;

                    // 打开自定义的Activity
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("ORDER", orderJson);
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        }
    }

    private void sendToMainActivity(Context context, Bundle bundle, String type) {
        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String notify_title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.i(TAG, "processNotify: title-->" + title);
        Log.i(TAG, "processNotify: notify_title-->" + notify_title);
        Log.i(TAG, "processNotify: message-->" + message);
        Log.i(TAG, "processNotify: extras-->" + extras);

        Intent msgIntent = new Intent(APPConsts.MESSAGE_RECEIVED_ACTION);
        msgIntent.putExtra(APPConsts.KEY_TYPE, type);
        if (PUSH_TYPE_MESSAGE.equals(type)) {
            msgIntent.putExtra(APPConsts.KEY_TITLE, title);
        } else if (PUSH_TYPE_NOTIFY.equals(type)) {
            msgIntent.putExtra(APPConsts.KEY_TITLE, notify_title);
        }
        if (!FormatCheckUtil.isEmpty(extras)) {
            try {
                JSONObject extraJson = new JSONObject(extras);
                if (extraJson.length() > 0) {
                    msgIntent.putExtra(APPConsts.KEY_EXTRAS, extras);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
    }
}
