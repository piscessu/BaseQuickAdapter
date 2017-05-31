package com.pisces.basequickadapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by PiscesSu on 2017/5/17.
 * Version 1.0
 */

public abstract class EndlessOnScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "EndlessOnScrollListener";
    private LinearLayoutManager mLayoutManager;
    private int totalCount;//当前总条目
    private int lastVisiblePosition;//最后一条可见条目位置
    private int lastTotalCount;//上一次总条目
    private boolean loading = true;
    private boolean isMore = true;//是否还有更多数据
    int currentPage = 1;

    public EndlessOnScrollListener(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        //当前页面有几个item
        //Log.d(TAG, "onScrolled: getChildCount = " + recyclerView.getChildCount());
        //Log.d(TAG, "onScrolled: getAdapter().getItemCount() = " + recyclerView.getAdapter().getItemCount());
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        //Log.d(TAG, "onScrollStateChanged: getChildCount = " + mLayoutManager.getChildCount());
        //Log.d(TAG, "onScrollStateChanged: getItemCount = " + mLayoutManager.getItemCount());
        Log.d(TAG, "onScrollStateChanged: findLastVisibleItemPosition = " + mLayoutManager.findLastVisibleItemPosition());
        Log.d(TAG, "onScrollStateChanged: findFirstVisibleItemPosition = " + mLayoutManager.findFirstVisibleItemPosition());
        totalCount = mLayoutManager.getItemCount();
        if (loading) {
            if (totalCount > lastTotalCount) {
                //已经加载结束
                loading = false;
                lastTotalCount = totalCount;
            }
        }
        lastVisiblePosition = mLayoutManager.findLastVisibleItemPosition();
        //newState状态，0：停止滑动 1：正在滑动 2：滑翔中
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (lastVisiblePosition + 1 == totalCount) {
                currentPage++;
                loadMore(currentPage);
                loading = true;
            }
        }
    }

    /**
     * 加载更多
     *
     * @param currentPage 页码
     */
    protected abstract void loadMore(int currentPage);
}
