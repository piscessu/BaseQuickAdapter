package com.pisces.basequickadapter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pisces.basequickadapter.example.MovieEntity;
import com.pisces.basequickadapter.example.MovieQuickAdapter;
import com.pisces.basequickadapter.quickadapter.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<MovieEntity> {

    RecyclerView rcv;
    List<MovieEntity.SubjectsBean> mBeanList;

    MovieQuickAdapter mQuickAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //init RecyclerView
        rcv = (RecyclerView) findViewById(R.id.rcv);
        rcv.setLayoutManager(new LinearLayoutManager(this));

        //init Adapter
        mBeanList = new ArrayList<>();
        mQuickAdapter = new MovieQuickAdapter(this, mBeanList);

        //set EmptyView
        mQuickAdapter.setEmptyView(R.layout.rcv_empty);

        //set OnItemClickListener
        mQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toasty.info(MainActivity.this, mBeanList.get(position).getTitle()).show();
            }
        });

        rcv.setAdapter(mQuickAdapter);

        //get Data
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
        progressDialog.show();
        RetrofitManager.getService().getTopMovie(0, 10).enqueue(this);
    }

    @Override
    public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
        progressDialog.dismiss();
        mBeanList.addAll(response.body().getSubjects());
        mQuickAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<MovieEntity> call, Throwable t) {
        progressDialog.dismiss();
    }
}
