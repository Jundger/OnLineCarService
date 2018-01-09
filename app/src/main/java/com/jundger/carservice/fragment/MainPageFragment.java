package com.jundger.carservice.fragment;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jundger.carservice.R;
import com.jundger.carservice.activity.LoginActivity;
import com.jundger.carservice.adapter.FaultInfoAdapter;
import com.jundger.carservice.annotation.InjectView;
import com.jundger.carservice.database.OBDDatabaseUtil;
import com.jundger.carservice.domain.FaultInfo;
import com.jundger.carservice.util.InjectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainPageFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @InjectView(R.id.get_info_fab)
    private FloatingActionButton getInfoFab;

    @InjectView(R.id.fault_info_recycler_view)
    private RecyclerView recyclerView;

    @InjectView(R.id.connect_state_iv)
    private ImageView connect_state_iv;

    @InjectView(R.id.main_page_fragment_tb)
    private Toolbar toolbar;

    @InjectView(R.id.fault_info_title_tv)
    private TextView fault_info_title_tv;

    @InjectView(R.id.empty_layout_ll)
    private LinearLayout emptyView;

    @InjectView(R.id.collapsing_toolbar)
    private CollapsingToolbarLayout collapsingLayout;

    private Dialog dialog;
    private Menu menu;
    private Boolean isConnect = false;

    private OBDDatabaseUtil databaseUtil;
    private FaultInfo[] info = {new FaultInfo("P107801", "动力总成系统", "尼桑（日产）、英菲尼迪", "排气阀门正时控制位置传感器-电路故障"),
            new FaultInfo("B009A", "车身系统", "所有汽车制造商", "一般由安全带传感器，其电路或接头故障所致"),
            new FaultInfo("U0112", "网络通讯系统", "所有汽车制造商", "与电池能量控制模块B通讯丢失"),
            new FaultInfo("U0556", "所有系统", "所有汽车制造商", "与电池能量控制模块B通讯丢失")};
    private List<FaultInfo> infoList = new ArrayList<>();
    private FaultInfoAdapter faultInfoAdapter;

    public MainPageFragment() {
        // Required empty public constructor
    }

    public static MainPageFragment newInstance(String param1, String param2) {
        MainPageFragment fragment = new MainPageFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Jundger", "onActivityCreated: ");
//        startDialog("正在加载数据……");
//        InjectUtil.InjectView(getActivity());
        bindView();
        init();

        addToRecyclerView();
//        endDialog();
    }

    private void addToRecyclerView() {
//        startDialog("正在加载数据……");
        new Thread(new Runnable() {
            @Override
            public void run() {
                infoList.clear();
                infoList.add(databaseUtil.query("P107801"));
                infoList.add(databaseUtil.query("B009A"));
                infoList.add(databaseUtil.query("U0112"));
                infoList.add(databaseUtil.query("U0556"));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isListEmpty(infoList)) {
                            emptyView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            fault_info_title_tv.setVisibility(View.VISIBLE);
                            faultInfoAdapter = new FaultInfoAdapter(infoList);
                            recyclerView.setAdapter(faultInfoAdapter);
                        } else {
                            emptyView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            fault_info_title_tv.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }).start();
    }

    private void bindView() {
        getInfoFab = getActivity().findViewById(R.id.get_info_fab);
        toolbar = getActivity().findViewById(R.id.main_page_fragment_tb);
        recyclerView = getActivity().findViewById(R.id.fault_info_recycler_view);
        connect_state_iv = getActivity().findViewById(R.id.connect_state_iv);
        emptyView = getActivity().findViewById(R.id.empty_layout_ll);
        fault_info_title_tv = getActivity().findViewById(R.id.fault_info_title_tv);
        collapsingLayout = getActivity().findViewById(R.id.collapsing_toolbar);
    }

    private void init() {
        databaseUtil = new OBDDatabaseUtil(getActivity());

//        toolbar.setTitle("故障检测");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        getInfoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnect = !isConnect;
                int pic = isConnect ? R.drawable.has_connect_white : R.drawable.no_connect_red;
                connect_state_iv.setImageResource(pic);
                insertDatebase();
                addToRecyclerView();
            }
        });
        collapsingLayout.setTitle("奔驰 E200 Coupe");
    }

    private void insertDatebase() {
        for (int i = 0; i < info.length; i++) {
            databaseUtil.insert(info[i]);
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
        databaseUtil.deleteAll();
    }

    private void startDialog(String msg) {
        dialog = new Dialog(getView().getContext(), R.style.MyDialogStyle);
        dialog.setContentView(R.layout.loading);
        dialog.setCanceledOnTouchOutside(false);
        TextView message = dialog.getWindow().findViewById(R.id.load_msg);
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

    public boolean isListEmpty(List<FaultInfo> list) {
        if (null != list && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (null == list.get(i)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
