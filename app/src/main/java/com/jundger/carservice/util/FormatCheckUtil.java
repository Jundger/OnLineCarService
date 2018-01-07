package com.jundger.carservice.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.jundger.carservice.R;

/**
 * Created by Jundger on 2018/1/8.
 */

public class FormatCheckUtil {

    public static boolean checkPhoneNumber(Context context, String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(context, R.string.phone_not_null, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!phoneNumber.substring(0, 1).equals("1")) {
            Toast.makeText(context, R.string.phone_form_not_specs, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phoneNumber.length() != 11) {
            Toast.makeText(context, R.string.phone_length_not_specs, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean checkPassword(Context context, String password) {
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, R.string.password_not_null, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 6) {
            Toast.makeText(context, R.string.password_not_more_six, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
