package com.jundger.carservice.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jundger.carservice.R;
import com.jundger.carservice.adapter.ArticleAdapter;
import com.jundger.carservice.domain.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MaintainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MaintainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MaintainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
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
        initArticle();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        articleAdapter = new ArticleAdapter(shopList);
        recyclerView.setAdapter(articleAdapter);
    }

    private void init() {
        recyclerView = getActivity().findViewById(R.id.maintain_knowledge_rv);
        swipeRefresh = getActivity().findViewById(R.id.swipe_refresh_maintain);
        swipeRefresh.setColorSchemeResources(R.color.appThemeColor);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshArticle();
            }
        });
//        Toolbar toolbar = getActivity().findViewById(R.id.maintain_fragment_tb);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);
    }

    private void initArticle() {
        shopList.clear();
        for (int i = 0; i < 3; i++) {
            Article sp1 = new Article(R.drawable.maintain_article05, "微整形手术 宝马7系中期改款或今年底亮相", "网易汽车", "01-04 22:28");
            shopList.add(sp1);
            Article sp2 = new Article(R.drawable.maintain_article04, "细节方面有所提升 奔驰全新一代G级官图", "汽车最前第一线");
            shopList.add(sp2);
            Article sp3 = new Article(R.drawable.maintain_article03, "把凯美瑞爆改成皮卡车 当作毕业作品", "改装车", "12-03 18:21");
            shopList.add(sp3);
            Article sp4 = new Article(R.drawable.maintain_article01, "微整形手术 宝马7系中期改款或今年底亮相", "超跑密探", "11-14 11:42");
            shopList.add(sp4);
            Article sp5 = new Article(R.drawable.maintain_article02, "细节方面有所提升 奔驰全新一代G级官图", "网易汽车综合", "08-23 14:59");
            shopList.add(sp5);
        }
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
                        initArticle();
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MaintainFragment.
     */
    // TODO: Rename and change types and number of parameters
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

    // TODO: Rename method, update argument and hook method into UI event
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
