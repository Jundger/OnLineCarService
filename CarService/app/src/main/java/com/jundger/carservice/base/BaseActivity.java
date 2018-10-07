package com.jundger.carservice.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BaseActivity extends AppCompatActivity{

    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "Add a activity ======> " +getClass().getSimpleName());
        ActivityCollector.addActivity(this, getClass());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Remove a activity ==> " +getClass().getSimpleName());
        ActivityCollector.removeActivity(this);
    }
}
