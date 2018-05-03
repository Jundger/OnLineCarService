package com.jundger.carservice.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jundger.carservice.R;
import com.jundger.carservice.adapter.ArticleAdapter;
import com.jundger.carservice.constant.Actions;
import com.jundger.carservice.constant.UrlConsts;
import com.jundger.carservice.bean.Article;
import com.jundger.carservice.bean.ResultArray;
import com.jundger.carservice.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MaintainFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;

    private ArticleAdapter articleAdapter;
    private List<Article> shopList = new ArrayList<>();

    public MaintainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("Toolbar", "onActivityCreated: MaintainFragment");
        init();
        event();
        requestArticle();
    }

    private void requestArticle() {

        String url = UrlConsts.getRequestURL(Actions.ACTION_GET_ARTICLE);
        HttpUtil.okHttpGet(url, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();

                // Gson解析json数据
                final ResultArray<Article> result = new Gson().fromJson(res, new TypeToken<ResultArray<Article>>(){}.getType());

                shopList.clear();
                shopList.addAll(result.getData());

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("1".equals(result.getCode())) {

                            articleAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "加载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void init() {
        recyclerView = getActivity().findViewById(R.id.maintain_knowledge_rv);
        swipeRefresh = getActivity().findViewById(R.id.swipe_refresh_maintain);
        swipeRefresh.setColorSchemeResources(R.color.appThemeColor);

//        Toolbar toolbar = getActivity().findViewById(R.id.maintain_fragment_tb);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        articleAdapter = new ArticleAdapter(shopList);
        recyclerView.setAdapter(articleAdapter);
    }

    private void event() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshArticle();
            }
        });
    }

    private void refreshArticle() {
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
//                        initArticle();
                        articleAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
//        inflater.inflate(R.menu.main_toolbar, menu);
        Log.i("Toolbar", "onCreateOptionsMenu: MaintainFragment");
    }

    public static MaintainFragment newInstance(String param1, String param2) {
        MaintainFragment fragment = new MaintainFragment();
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
        return inflater.inflate(R.layout.fragment_maintain, container, false);
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
        void onFragmentInteraction(Uri uri);
    }
}
