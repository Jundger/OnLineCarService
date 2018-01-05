package com.jundger.carservice.fragment;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.jundger.carservice.R;
import com.jundger.carservice.activity.LoginActivity;
import com.jundger.carservice.adapter.FaultInfoAdapter;
import com.jundger.carservice.domain.FaultInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainPageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private FloatingActionButton getInfoFab;
    private RecyclerView recyclerView;
    private ImageView connect_state_iv;
    private Toolbar toolbar;
    private Dialog dialog;

    private Menu menu;

    private Boolean isConnect = false;

    private FaultInfo[] info = {new FaultInfo("P107801", "动力总成系统", "尼桑（日产）、英菲尼迪", "排气阀门正时控制位置传感器-电路故障"),
            new FaultInfo("B009A", "车身系统", "所有汽车制造商", "一般由安全带传感器，其电路或接头故障所致"),
            new FaultInfo("U0112", "网络通讯系统", "所有汽车制造商", "与电池能量控制模块B通讯丢失")};
    private List<FaultInfo> infoList = new ArrayList<>();
    private FaultInfoAdapter faultInfoAdapter;

    public MainPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Toolbar", "onActivityCreated: MainPageFragment");
//        startDialog("正在加载数据……");
        bindView();
        init();

        initFaultInfo();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        faultInfoAdapter = new FaultInfoAdapter(infoList);
        recyclerView.setAdapter(faultInfoAdapter);
//        endDialog();
    }

    private void bindView() {
        getInfoFab = getActivity().findViewById(R.id.get_info_fab);
        toolbar = getActivity().findViewById(R.id.main_activity_tb);
        recyclerView = getActivity().findViewById(R.id.fault_info_recycler_view);
        connect_state_iv = getActivity().findViewById(R.id.connect_state_iv);
    }

    private void init() {
        toolbar.setTitle("故障检测");
//        toolbar.setLogo(R.mipmap.app_log1);
//        setHasOptionsMenu(true);
//        toolbar.inflateMenu(R.menu.main_toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setIcon(R.mipmap.app_log);
//            actionBar.setDisplayShowTitleEnabled(false);
        }

        getInfoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnect = !isConnect;
                int pic = isConnect ? R.drawable.has_connect_black : R.drawable.no_connect_red;
                connect_state_iv.setImageResource(pic);
            }
        });
    }

    private void initFaultInfo() {
        infoList.clear();
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int index = random.nextInt(info.length);
            infoList.add(info[index]);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
}
