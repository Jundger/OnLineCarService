package com.jundger.carservice.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps2d.model.LatLng;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.R;
import com.jundger.carservice.adapter.CommentAdapter;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.constant.Actions;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.bean.Comment;
import com.jundger.carservice.bean.ResultArray;
import com.jundger.carservice.bean.ServicePoint;
import com.jundger.carservice.util.HttpUtil;
import com.jundger.carservice.util.InjectUtil;
import com.jundger.carservice.util.NavigationUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShopDetailActivity extends BaseActivity {

    ServicePoint mServicePoint;

    @InjectView(R.id.detail_collapsing_tllt)
    private CollapsingToolbarLayout detail_collapsing_tllt;

    @InjectView(R.id.shop_detail_tb)
    private Toolbar shop_detail_tb;

    @InjectView(R.id.shop_detail_tv)
    private TextView shop_detail_tv;

    @InjectView(R.id.main_picture_show_iv)
    private ImageView main_picture_show_iv;

    @InjectView(R.id.shop_property_tv)
    private TextView shop_property_tv;

    @InjectView(R.id.navigation_fab)
    private FloatingActionButton navigation_fab;

    @InjectView(R.id.comment_show_rv)
    private RecyclerView recyclerView;

    private CommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();

    private static final String TAG = "ShopDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        InjectUtil.InjectView(this);

        mServicePoint = (ServicePoint) getIntent().getSerializableExtra("ServicePoint");

        init();

        event();
    }

    private void event() {

        navigation_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetailActivity.this);
                builder.setTitle("提示")
                        .setIcon(R.mipmap.app_log)
                        .setCancelable(false)
                        .setMessage("请在手机设置里打开GPS")
                        .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                LatLng desLatLng = new LatLng(mServicePoint.getLatitude(), mServicePoint.getLongitude());
                                NavigationUtil.toNavigation(ShopDetailActivity.this, desLatLng);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ShopDetailActivity.this, "取消导航", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentAdapter = new CommentAdapter(commentList);
        recyclerView.setAdapter(commentAdapter);
        sendCommentRequest(mServicePoint.getId());

        setSupportActionBar(shop_detail_tb);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); // 返回按钮可点击
        }

        shop_detail_tv.setText(mServicePoint.getName());
        Glide.with(ShopDetailActivity.this)
                .load(mServicePoint.getImage())
                .thumbnail(0.2f)
                .into(main_picture_show_iv);
    }

    private void sendCommentRequest(String site_id) {

        RequestBody requestBody = new FormBody.Builder()
                .add("site_id", site_id)
                .build();
        HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_GET_COMMENT), requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ShopDetailActivity.this, "评论获取失败，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                final ResultArray<Comment> result = gson.fromJson(res, new TypeToken<ResultArray<Comment>>(){}.getType());
                if (UrlConsts.CODE_SUCCESS.equals(result.getCode())) {
                    commentList.clear();
                    commentList.addAll(result.getData());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            commentAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ShopDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public static void launchActivity(Context context, ServicePoint servicePoint) {
        Intent intent = new Intent(context, ShopDetailActivity.class);
        intent.putExtra("ServicePoint", servicePoint);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ShopDetailActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu_toolbar, menu);
        return true;
    }
}
