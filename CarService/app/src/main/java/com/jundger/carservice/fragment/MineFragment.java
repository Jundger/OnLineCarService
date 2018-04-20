package com.jundger.carservice.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jundger.carservice.R;
import com.jundger.carservice.activity.LoginActivity;
import com.jundger.carservice.activity.SettingsActivity;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Toolbar toolbar;

    private GridView gridView;
    private List<Map<String, Object>> list;
    private SimpleAdapter simpleAdapter;
    private TextView edit_person_data_tv;

    private int[] icon = {R.drawable.order_icon, R.drawable.claim_icon, R.drawable.feedback_icon, R.drawable.evaluate_icon, R.drawable.collect_icon};
    private String[] iconName = {"维修订单", "保险理赔", "反馈", "评价", "收藏"};

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindView();
        init();
    }

    private void bindView() {
        toolbar = getActivity().findViewById(R.id.mine_activity_tb);
        gridView = getActivity().findViewById(R.id.mine_page_gview);
        edit_person_data_tv = getActivity().findViewById(R.id.edit_person_data_tv);
    }

    private void init() {
        setHasOptionsMenu(true);
        toolbar.inflateMenu(R.menu.mine_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setting_item:
                        Intent intent = new Intent(getActivity(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.message_item:
                        Toast.makeText(getActivity(), "Message button click!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.exit_login_item:
                        showNormalDialog();
                    default: break;
                }
                return true;
            }
        });

        list = new ArrayList<>();
        getData();
        String[] from = {"image", "text"};
        int[] to = {R.id.item_grid_img_tv, R.id.item_grid_name_tv};
        simpleAdapter = new SimpleAdapter(getView().getContext(), list, R.layout.item_mine_grid, from, to);
        gridView.setAdapter(simpleAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Toast.makeText(getActivity(), "抱歉，维修订单模块尚未完善！", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "抱歉，保险理赔模块尚未完善！", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "抱歉，收藏模块尚未完善！", Toast.LENGTH_SHORT).show();
                        break;
                    default: break;
                }
            }
        });

        edit_person_data_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "抱歉，尚不能修改个人资料！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNormalDialog(){

        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
        normalDialog.setIcon(R.mipmap.app_log);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("是否要退出账号?");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferencesUtil.save(getActivity(), UrlConsts.SHARED_IS_LOGIN, false);

                LoginActivity.launchActivity(getActivity());
                getActivity().finish();
            }
        });

        normalDialog.setNegativeButton("取消", null);
        normalDialog.show();
    }

    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
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
        return inflater.inflate(R.layout.fragment_mine, container, false);
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

    public void getData() {
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            list.add(map);
        }
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
}
