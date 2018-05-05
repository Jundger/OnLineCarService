package com.jundger.carservice.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jundger.carservice.R;
import com.jundger.carservice.activity.CommentActivity;
import com.jundger.carservice.activity.OrderActivity;
import com.jundger.carservice.activity.ProfileActivity;
import com.jundger.carservice.activity.FeedbackActivity;
import com.jundger.carservice.activity.LoginActivity;
import com.jundger.carservice.activity.SettingsActivity;
import com.jundger.carservice.constant.APPConsts;
import com.jundger.carservice.bean.User;
import com.jundger.carservice.util.SharedPreferencesUtil;
import com.jundger.carservice.widget.CircleImageView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineFragment extends Fragment {
    private static final String ARG_PARAM1 = "user_info";

    private User user;

    private OnFragmentInteractionListener mListener;

    private GridView gridView;
    private List<Map<String, Object>> list;
    private SimpleAdapter simpleAdapter;
    private TextView nickname_show_tv;
    private CircleImageView my_portrait_civ;

    private static final String TAG = "MineFragment";

    private int[] icon = {R.drawable.order_icon, R.drawable.evaluate_icon, R.drawable.collect_icon,
            R.drawable.feedback_icon, R.drawable.notice_icon, R.drawable.setting_icon};
    private String[] iconName = {"维修订单", "评价", "收藏", "反馈", "通知", "设置"};

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
        gridView = getActivity().findViewById(R.id.mine_page_gview);
        nickname_show_tv = getActivity().findViewById(R.id.edit_person_data_tv);
        my_portrait_civ = getActivity().findViewById(R.id.my_portrait_civ);
    }

    private void init() {
        list = new ArrayList<>();
        getData();
        String[] from = {"image", "text"};
        int[] to = {R.id.item_grid_img_tv, R.id.item_grid_name_tv};
        simpleAdapter = new SimpleAdapter(getActivity(), list, R.layout.item_mine_grid, from, to);
        gridView.setAdapter(simpleAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        OrderActivity.launchActivity(getActivity(), user.getCustId());
                        break;
                    case 1:
                        CommentActivity.launchActivity(getActivity(), user.getCustId());
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "抱歉，收藏模块尚未完善！", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        FeedbackActivity.launchActivity(getActivity(), user.getNickname());
                        break;
                    case 4:
                        Toast.makeText(getActivity(), "抱歉，通知模块尚未完善！", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Intent intent = new Intent(getActivity(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    default: break;
                }
            }
        });

        if (user.getNickname() != null && !"".equals(user.getNickname())) {
            nickname_show_tv.setText(user.getNickname());
        } else {
            nickname_show_tv.setText("暂无昵称");
        }

        Glide.with(getActivity())
                .load(user.getPortrait())
//                .placeholder(R.drawable.portrait_place_holder)
                .error(R.drawable.load_fail)
                .into(my_portrait_civ);
        my_portrait_civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_info", user);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "MineFragment | onActivityResult: requestCode-->" + requestCode + " | resultCode-->" + resultCode);
        if (requestCode == 0 && resultCode == 1) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                user = (User) bundle.getSerializable("user_info");
                if (user != null) {
                    nickname_show_tv.setText(user.getNickname() == null ? "暂无昵称" : user.getNickname());
                    Glide.with(getActivity())
                            .load(user.getPortrait())
//                            .placeholder(R.drawable.portrait_place_holder)
                            .error(R.drawable.load_fail)
                            .into(my_portrait_civ);
                }
            }
        }
    }

    public static MineFragment newInstance(User user) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_PARAM1);
        }
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
