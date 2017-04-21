package com.pisces.basequickadapter.quickadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by SuChangz on 2017/4/20.
 * Version 1.0
 */

public abstract class BaseQuickAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private int mLayoutResID;
    private List<T> data;

    private static final int EMPTY_VIEW_TYPE = -1000;
    private int mEmptyLayoutResID = 0;

    private OnItemClickListener mItemClickListener = null;

    public BaseQuickAdapter(Context context, int layoutResID, List<T> data) {
        mContext = context;
        this.mLayoutResID = layoutResID;
        this.data = data;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseViewHolder holder;
        //load empty view
        if (viewType == EMPTY_VIEW_TYPE && mEmptyLayoutResID != 0) {
            View emptyView = LayoutInflater.from(mContext).inflate(mEmptyLayoutResID, parent, false);
            holder = new BaseViewHolder(emptyView);
        } else {
            final View itemView = LayoutInflater.from(mContext).inflate(mLayoutResID, parent, false);
            holder = new BaseViewHolder(itemView);
            //添加点击事件
            if (mItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(itemView, holder.getLayoutPosition());
                    }
                });
            }
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (data != null && data.size() > 0) {
            convert(holder, data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (data == null || data.size() == 0) {
            return 1;
        }
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data == null || data.size() == 0) {
            return EMPTY_VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }

    //set EmptyView
    public void setEmptyView(int layoutResID) {
        this.mEmptyLayoutResID = layoutResID;
    }

    //setOnItemClickListener
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    protected abstract void convert(BaseViewHolder holder, T item);
}
