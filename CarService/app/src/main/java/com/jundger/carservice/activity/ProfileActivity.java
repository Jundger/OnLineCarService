package com.jundger.carservice.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.R;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.bean.User;
import com.jundger.carservice.constant.APPConsts;
import com.jundger.carservice.constant.Actions;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.util.HttpUtil;
import com.jundger.carservice.util.InjectUtil;
import com.jundger.carservice.util.SharedPreferencesUtil;
import com.jundger.carservice.widget.CircleImageView;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String PARAM_USER = "user_info";
    private static final String TAG = "ProfileActivity";

    public static final String MODIFY_NICKNAME = "nickname";
    public static final String MODIFY_EMAIL = "email";
    public static final String MODIFY_BRAND = "brand";
    public static final String MODIFY_BRAND_NO = "brand_no";

    private User user;

    @InjectView(R.id.edit_info_activity_tb)
    private Toolbar toolbar;

    @InjectView(R.id.set_portrait_rl)
    private RelativeLayout set_portrait_rl;

    @InjectView(R.id.set_nickname_rl)
    private RelativeLayout set_nickname_rl;

    @InjectView(R.id.set_email_rl)
    private RelativeLayout set_email_rl;

    @InjectView(R.id.set_brand_rl)
    private RelativeLayout set_brand_rl;

    @InjectView(R.id.set_brand_no_rl)
    private RelativeLayout set_brand_no_rl;

    @InjectView(R.id.show_portrait_civ)
    private CircleImageView show_portrait_civ;

    @InjectView(R.id.show_nickname_tv)
    private TextView show_nickname_tv;

    @InjectView(R.id.show_email_tv)
    private TextView show_email_tv;

    @InjectView(R.id.show_brand_tv)
    private TextView show_brand_tv;

    @InjectView(R.id.show_brand_no_tv)
    private TextView show_brand_no_tv;

    @InjectView(R.id.exit_login_btn)
    private Button exit_login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = (User) getIntent().getSerializableExtra(PARAM_USER);
        InjectUtil.InjectView(this); // 自定义控件绑定注解

        init();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_portrait_rl:
                modifyPortrait();
                break;
            case R.id.set_nickname_rl:
                modifyInfo(MODIFY_NICKNAME);
                break;
            case R.id.set_email_rl:
                modifyInfo(MODIFY_EMAIL);
                break;
            case R.id.set_brand_rl:
                modifyInfo(MODIFY_BRAND);
                break;
            case R.id.set_brand_no_rl:
                modifyInfo(MODIFY_BRAND_NO);
                break;
            case R.id.exit_login_btn:
                showExitDialog();
                break;
            default:
                Log.i(TAG, "onClick: Error!");
                break;
        }
    }

    private void modifyPortrait() {
        final String[] items = {"相册", "拍照", "随机"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("请选择头像来源")
                .setIcon(R.mipmap.app_log)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Toast.makeText(ProfileActivity.this, items[i], Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void modifyInfo(final String type) {
        final EditText editText = new EditText(ProfileActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("请输入修改后的数据")
                .setIcon(R.mipmap.app_log)
                .setView(editText)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        final String input = editText.getText().toString().trim();
                        Log.i(TAG, "Modify user information: " + input);
                        if (!"".equals(input)) {
                            switch (type) {
                                case MODIFY_NICKNAME:
                                    user.setNickname(input);
                                    break;
                                case MODIFY_EMAIL:
                                    user.setEmail(input);
                                    break;
                                case MODIFY_BRAND:
                                    user.setBrand(input);
                                    break;
                                case MODIFY_BRAND_NO:
                                    user.setBrand_no(input);
                                    break;
                            }
                            sendModifyRequest(type, input, user);
                        }
                    }
                })
                .show();
    }

    private void sendModifyRequest(final String type, final String input, final User user) {

        String json = new Gson().toJson(user);
        Log.i(TAG, "sendModifyRequest: " + json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.okHttpPost(UrlConsts.getRequestURL(Actions.ACTION_MODIFY_PROFILE), requestBody, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG, "onFailure: 修改个人资料网络请求失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String prompt;
                if (response.code() == 200) {
                    String res = response.body().string();
                    Map<String, String> result = new Gson().fromJson(res, new TypeToken<Map<String, String>>() {
                    }.getType());
                    if (UrlConsts.CODE_SUCCESS.equals(result.get(UrlConsts.KEY_RETURN_CODE))) {
                        // 通过LitePal更新用户信息
                        user.updateAll("phone = ?", user.getPhone());

                        prompt = "更改成功";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switch (type) {
                                    case MODIFY_NICKNAME:
                                        show_nickname_tv.setText(input);
                                        break;
                                    case MODIFY_EMAIL:
                                        show_email_tv.setText(input);
                                        break;
                                    case MODIFY_BRAND:
                                        show_brand_tv.setText(input);
                                        break;
                                    case MODIFY_BRAND_NO:
                                        show_brand_no_tv.setText(input);
                                        break;
                                }
                            }
                        });
                    } else {
                        prompt = "更改失败";
                    }
                } else {
                    prompt = "未知错误";
                }
                final String finalPrompt = prompt;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ProfileActivity.this, finalPrompt, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); // 返回按钮可点击
        }
        toolbar.setSubtitle("个人资料");

        show_nickname_tv.setText(user.getNickname() == null ? "无" : user.getNickname());
        show_email_tv.setText(user.getEmail());
        show_brand_tv.setText(user.getBrand() == null ? "无" : user.getBrand());
        show_brand_no_tv.setText(user.getBrand_no() == null ? "无" : user.getBrand_no());
        Glide.with(ProfileActivity.this)
                .load(user.getPortrait())
                .error(R.drawable.load_fail)
                .into(show_portrait_civ);
    }

    private void showExitDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setIcon(R.mipmap.app_log);
        builder.setTitle("提示");
        builder.setMessage("是否要退出账号?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataSupport.deleteAll(User.class, "phone = ?", user.getPhone());

                SharedPreferencesUtil.save(ProfileActivity.this, APPConsts.SHARED_KEY_ISLOGIN, false);
                LoginActivity.launchActivity(ProfileActivity.this);
                ProfileActivity.this.finish();
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("user_info", user);
            intent.putExtras(bundle);
            setResult(1, intent);
            ProfileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        /**
         * 这里注意一定要在调用上级函数前操作
         * 否则调用后activity已经销毁导致无法返回数据
         */
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user_info", user);
        intent.putExtras(bundle);
        setResult(1, intent);

        super.onBackPressed();
    }

}
