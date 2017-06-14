package com.pisces.basequickadapter;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pisces.basequickadapter.example.MovieEntity;
import com.pisces.basequickadapter.example.MovieQuickAdapter;
import com.pisces.basequickadapter.quickadapter.BaseQuickAdapter;
import com.pisces.basequickadapter.quickadapter.EndlessScrollListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<MovieEntity> {
    SwipeRefreshLayout srl;
    RecyclerView rcv;
    List<MovieEntity.SubjectsBean> mBeanList;
    static int CURRENT_PAGE = 0;//起始页第一页
    static final int PAGE_COUNT = 50;//每页有50条数据

    MovieQuickAdapter mQuickAdapter;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //init RecyclerView
        rcv = (RecyclerView) findViewById(R.id.rcv);
        mLinearLayoutManager = new LinearLayoutManager(this);
        rcv.setLayoutManager(mLinearLayoutManager);
        rcv.addOnScrollListener(new EndlessScrollListener());

        srl = (SwipeRefreshLayout) findViewById(R.id.srl);
        srl.setColorSchemeColors(Color.RED, Color.GREEN, Color.YELLOW);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBeanList.clear();
                        getMovies(CURRENT_PAGE);
                    }
                }, 2000);
            }
        });
        //init Adapter
        mBeanList = new ArrayList<>();
        mQuickAdapter = new MovieQuickAdapter(this, mBeanList);

        //set EmptyView
        mQuickAdapter.setEmptyView(R.layout.rcv_empty);
        mQuickAdapter.setOnPageLoadListener(new BaseQuickAdapter.OnPageLoadListener() {
            @Override
            public void onPageLoad() {
                CURRENT_PAGE++;
                getMovies(CURRENT_PAGE);
            }
        }, PAGE_COUNT);
        //set OnItemClickListener
        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toasty.info(MainActivity.this, mBeanList.get(position).getTitle()).show();
            }
        });
        getMovies(CURRENT_PAGE);
    }

    private void getMovies(int page) {
        //1,11,21 (page-1)*PAGE_COUNT+1
        srl.setRefreshing(true);
        RetrofitManager.getService().getTopMovie(page * PAGE_COUNT , PAGE_COUNT).enqueue(this);
    }

    @Override
    public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
        if (rcv.getAdapter() == null) {
            mBeanList.addAll(response.body().getSubjects());
            rcv.setAdapter(mQuickAdapter);
        } else {
            //mQuickAdapter.notifyDataSetChanged();
            mQuickAdapter.appendList(response.body().getSubjects());
        }
        srl.setRefreshing(false);
    }

    @Override
    public void onFailure(Call<MovieEntity> call, Throwable t) {
        srl.setRefreshing(false);
    }
}
