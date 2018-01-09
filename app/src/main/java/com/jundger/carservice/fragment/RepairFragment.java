package com.jundger.carservice.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Adapter;
import android.widget.Toast;

import com.jundger.carservice.R;
import com.jundger.carservice.activity.MainActivity;
import com.jundger.carservice.activity.MapActivity;
import com.jundger.carservice.adapter.ServicePointAdapter;
import com.jundger.carservice.domain.ServicePoint;
import com.jundger.carservice.util.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

public class RepairFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Toolbar mToolbar;
    private SearchView mSearchView;
    private RecyclerView recyclerView;

    private ServicePointAdapter shopAdapter;
    private List<ServicePoint> shopList = new ArrayList<>();

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
        initServicePoint();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        shopAdapter = new ServicePointAdapter(shopList);
        recyclerView.setAdapter(shopAdapter);
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
                        initServicePoint();
                        shopAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initServicePoint() {
        shopList.clear();
        for (int i = 0; i < 3; i++) {
            ServicePoint sp1 = new ServicePoint(R.drawable.service_point01, "重庆车行舰汽车维修店", 3.6F, 268, "重庆市南岸区南坪大石路56号坪大石路5号", 3.5F);
            shopList.add(sp1);
            ServicePoint sp2 = new ServicePoint(R.drawable.service_point02, "重庆长安铃木汽车销售服务有限公司", 2.1F, 1568, "重庆市南岸区江南大道35号山河大厦", 1.3F);
            shopList.add(sp2);
            ServicePoint sp3 = new ServicePoint(R.drawable.service_point03, "重庆东风风神同捷专营店", 4.9F, 23, "重庆市南岸区福相路8号8栋", 2.6F);
            shopList.add(sp3);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
