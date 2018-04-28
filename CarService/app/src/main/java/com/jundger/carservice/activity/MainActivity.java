package com.jundger.carservice.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jundger.carservice.R;
import com.jundger.carservice.adapter.MyFragmentPagerAdapter;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.base.BaseActivity;
import com.jundger.carservice.fragment.MainPageFragment;
import com.jundger.carservice.fragment.MaintainFragment;
import com.jundger.carservice.fragment.MineFragment;
import com.jundger.carservice.fragment.RepairFragment;
import com.jundger.carservice.pojo.User;
import com.jundger.carservice.util.InjectUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener,
        MainPageFragment.OnFragmentInteractionListener,
        RepairFragment.OnFragmentInteractionListener,
        MaintainFragment.OnFragmentInteractionListener,
        MineFragment.OnFragmentInteractionListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "MainActivity";
    public static final String TRANSMIT_PARAM = "USER";

    private User user;

    @InjectView(R.id.main_page_rl)
    private RelativeLayout main_page_rl;

    @InjectView(R.id.repair_page_rl)
    private RelativeLayout repair_page_rl;

    @InjectView(R.id.maintain_page_rl)
    private RelativeLayout maintain_page_rl;

    @InjectView(R.id.mine_page_rl)
    private RelativeLayout mine_page_rl;

    @InjectView(R.id.viewPager)
    private ViewPager myViewPager;// 要使用的ViewPager

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);

        InjectUtil.InjectView(this); // 自定义控件绑定注解

        main_page_rl.setOnClickListener(this);
        repair_page_rl.setOnClickListener(this);
        maintain_page_rl.setOnClickListener(this);
        mine_page_rl.setOnClickListener(this);

        user = (User) getIntent().getSerializableExtra(TRANSMIT_PARAM);

        initFragment();
    }

    private void initFragment() {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(MainPageFragment.newInstance(user.getBrand(), user.getBrand_no()));
        fragmentList.add(new RepairFragment());
        fragmentList.add(new MaintainFragment());
        fragmentList.add(new MineFragment());
        MyFragmentPagerAdapter myFragmentAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        myViewPager.setAdapter(myFragmentAdapter);
        myViewPager.addOnPageChangeListener(this);

        main_page_rl.setSelected(true);
        repair_page_rl.setSelected(false);
        maintain_page_rl.setSelected(false);
        mine_page_rl.setSelected(false);
    }

    public static void launchActivity(Context context, User user) {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSMIT_PARAM, user);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_page_rl: myViewPager.setCurrentItem(0); break;
            case R.id.repair_page_rl: myViewPager.setCurrentItem(1); break;
            case R.id.maintain_page_rl: myViewPager.setCurrentItem(2); break;
            case R.id.mine_page_rl: myViewPager.setCurrentItem(3); break;
            default: break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                main_page_rl.setSelected(true);
                repair_page_rl.setSelected(false);
                maintain_page_rl.setSelected(false);
                mine_page_rl.setSelected(false);
                break;
            case 1:
                main_page_rl.setSelected(false);
                repair_page_rl.setSelected(true);
                maintain_page_rl.setSelected(false);
                mine_page_rl.setSelected(false);
                break;
            case 2:
                main_page_rl.setSelected(false);
                repair_page_rl.setSelected(false);
                maintain_page_rl.setSelected(true);
                mine_page_rl.setSelected(false);
                break;
            case 3:
                main_page_rl.setSelected(false);
                repair_page_rl.setSelected(false);
                maintain_page_rl.setSelected(false);
                mine_page_rl.setSelected(true);
                break;
            default: break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public ViewPager getMyViewPager() {
        return myViewPager;
    }
}
