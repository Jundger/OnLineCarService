package com.jundger.carservice.fragment;

import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.R;
import com.jundger.carservice.activity.MapActivity;
import com.jundger.carservice.adapter.SiteAdapter;
import com.jundger.carservice.constant.APPConsts;
import com.jundger.carservice.constant.Actions;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.bean.ResultArray;
import com.jundger.carservice.bean.ServicePoint;
import com.jundger.carservice.util.HttpUtil;
import com.jundger.carservice.util.LocationUtil;
import com.jundger.carservice.util.RecyclerViewDivider;
import com.jundger.carservice.util.SharedPreferencesUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RepairFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private RecyclerView recyclerView;

    private Dialog dialog = null;

    private Location location = null;

    private SiteAdapter siteAdapter;
    private List<ServicePoint> siteList = new ArrayList<>();

    private static final String TAG = "RepairFragment";

    // 下拉刷新
    private SwipeRefreshLayout swipeRefreshLayout;

    public RepairFragment() {
        // Required empty public constructor
    }

    public static RepairFragment newInstance(String param1, String param2) {
        RepairFragment fragment = new RepairFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true); // 加上这句话，Toolbar的menu才会显示出来
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repair, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mToolbar.getMenu().clear();
        inflater.inflate(R.menu.repair_toolbar, menu);
        MenuItem searchItem = menu.findItem(R.id.repair_menu_search);
        mSearchView = (SearchView) searchItem.getActionView();
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框中) 右侧有叉叉 可以关闭搜索框
        mSearchView.setIconified(false);
        //设置搜索框直接展开显示。左侧有放大镜(在搜索框外) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
//        mSearchView.setIconifiedByDefault(false);
        // 设置搜索框直接展开显示。左侧有无放大镜(在搜索框中) 右侧无叉叉 有输入内容后有叉叉 不能关闭搜索框
//        mSearchView.onActionViewExpanded();
        // 设置最大宽度
//        mSearchView.setMaxWidth(800);
        // 设置输入框提示语
        mSearchView.setQueryHint("搜索附近维修店");
        mSearchView.clearFocus();
//        mSearchView.setBackground(getResources().getDrawable(R.drawable.search_et_bk));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        event();

        location = LocationUtil.requestLocation(getActivity());
        if (location != null) {
            Log.d(TAG, "获取到当前经纬度: " + "longitude-->" + location.getLongitude() + " | latitude-->" + location.getLatitude());
            // 在本地存储位置信息
            SharedPreferencesUtil.save(getActivity(), APPConsts.SHARED_KEY_LONGITUDE, String.valueOf(location.getLongitude()));
            SharedPreferencesUtil.save(getActivity(), APPConsts.SHARED_KEY_LATITUDE, String.valueOf(location.getLatitude()));
        } else {
            Log.d(TAG, "onActivityCreated: 获取经纬度失败！");
        }

        requestServicePoint();

    }

    private void requestServicePoint() {
//        startDialog("Loading...");

        String url = UrlConsts.getRequestURL(Actions.ACTION_GET_SITE);

        RequestBody requestBody = null;
        if (location != null) {
            requestBody = new FormBody.Builder()
                    .add("longitude", String.valueOf(location.getLongitude()))
                    .add("latitude", String.valueOf(location.getLatitude()))
                    .build();
        }

        HttpUtil.okHttpPost(url, requestBody, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(TAG, "RepairFragment | okHttp success!" + response.code());
                if (response.code() == 200) {
                    String responseData = response.body().string();

                    // Gson解析json数据
                    ResultArray<ServicePoint> result = new Gson().fromJson(responseData, new TypeToken<ResultArray<ServicePoint>>() {
                    }.getType());

                    if (UrlConsts.SITE_EMPTY.equals(result.getMsg())) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "run: 当前坐标附近20公里没有维修店存在");
                                Toast.makeText(getActivity(), "当前坐标附近20公里没有维修店存在", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (UrlConsts.CODE_SUCCESS.equals(result.getCode())) {
                        /**
                         * 易错点！！！！！！！！！！
                         * 错误示范：siteList = result.getData();
                         *         siteAdapter.notifyDataSetChanged();
                         * 原因：数据源虽更新了，但是却指向了新的引用，而不是已和Adapter绑定的那个list
                         *      所以解决办法应该是将原有list清空并重装数据
                         */
                        siteList.clear();
                        siteList.addAll(result.getData());

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                        endDialog();
                                siteAdapter.notifyDataSetChanged();
                            }
                        });
                    } else if (UrlConsts.CODE_FAIL.equals(result.getCode())) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "run: 请求失败");
                                Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "run: 网络请求失败");
                            Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "RepairFragment | okHttp Error!");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        endDialog();
                        Toast.makeText(getContext(), "获取信息失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void init() {
        recyclerView = getActivity().findViewById(R.id.service_point_show_rv);
        mToolbar = getActivity().findViewById(R.id.repair_activity_tb);
        swipeRefreshLayout = getActivity().findViewById(R.id.swipe_refresh_repair);
        swipeRefreshLayout.setColorSchemeResources(R.color.appThemeColor);
//        toolbar.setTitle("维修");
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        mToolbar.inflateMenu(R.menu.repair_toolbar);

        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(),
                LinearLayoutManager.VERTICAL, R.drawable.recycler_view_divider));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        siteAdapter = new SiteAdapter(siteList);
        recyclerView.setAdapter(siteAdapter);
    }

    private void event() {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.location_item:
                        MapActivity.launchActivity(getActivity());
                        break;
                    default: break;
                }
                return false;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshShopInfo();
            }
        });
    }

    private void refreshShopInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        requestServicePoint();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void startDialog(String msg) {
        dialog = new Dialog(getActivity(), R.style.MyDialogStyle);
        dialog.setContentView(R.layout.loading);
        dialog.setCanceledOnTouchOutside(false);
        TextView message = (TextView) dialog.getWindow().findViewById(R.id.load_msg);
        if (dialog != null && !dialog.isShowing()) {
            message.setText(msg);
            dialog.show();
        }
    }

    private void endDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
