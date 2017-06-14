package com.pisces.basequickadapter.quickadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pisces.basequickadapter.R;

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
    private static final int FOOTER_VIEW_TYPE = -2000;
    private int mEmptyLayoutResID = 0;

    private OnItemClickListener mItemClickListener = null;
    private int pageCount;
    private int currentState;
    private final int STATE_LOADING = 1;
    private final int STATE_LASTED = 2;
    private OnPageLoadListener mOnPageLoadListener;
    private boolean isHasMore = true;

    public boolean isHasMore() {
        return isHasMore;
    }

    public void setHasMore(boolean hasMore) {
        isHasMore = hasMore;
    }

    public BaseQuickAdapter(Context context, int layoutResID, List<T> data) {
        mContext = context;
        this.mLayoutResID = layoutResID;
        this.data = data;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseViewHolder holder;
        //load empty view
        if (viewType == EMPTY_VIEW_TYPE) {
            View emptyView = LayoutInflater.from(mContext).inflate(mEmptyLayoutResID, parent, false);
            holder = new BaseViewHolder(emptyView);
        } else if (viewType == FOOTER_VIEW_TYPE) {
            View footerView = LayoutInflater.from(mContext).inflate(R.layout.rcv_footer_item, parent, false);
            holder = new BaseViewHolder(footerView);
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
        if (getItemViewType(position) == FOOTER_VIEW_TYPE) {
            switch (currentState) {
                case STATE_LOADING:
                    holder.getView(R.id.pb_footer).setVisibility(View.VISIBLE);
                    holder.getView(R.id.tv_footer).setVisibility(View.GONE);
                    mOnPageLoadListener.onPageLoad();//请求下一页数据
                    break;
                case STATE_LASTED:
                    holder.getView(R.id.tv_footer).setVisibility(View.VISIBLE);
                    holder.getView(R.id.pb_footer).setVisibility(View.GONE);
                    holder.setText(R.id.tv_footer, "无更多数据");
                    break;
            }
        } else {
            if (data != null && data.size() > 0) {
                convert(holder, data.get(position));
            }
        }
    }

    //去加载更多
    public final void isLoadingMore() {
        if (currentState == STATE_LOADING) {
            return;
        }
        currentState = STATE_LOADING;
        notifyItemRangeChanged(data.size(), 1);//刷新最后一项的内容
    }

    //将获取到的数据集合加到之前的集合中来
    public void appendList(List<T> beanList) {
        if (beanList.size() == pageCount) {
            currentState = STATE_LOADING;
            isHasMore = true;
        } else {
            currentState = STATE_LASTED;
            isHasMore = false;
        }
        int positionStart = data.size();
        data.addAll(beanList);
        int itemCount = beanList.size();
        if (positionStart == 0) {
            notifyDataSetChanged();
        } else {
            //notifyDataSetChanged();
            notifyItemRangeChanged(positionStart, itemCount + 1);
        }
    }


    @Override
    public int getItemCount() {
        if (data.size() == 0 && mEmptyLayoutResID != 0) {
            return 1;
        }
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.size() == 0 && mEmptyLayoutResID != 0) {
            return EMPTY_VIEW_TYPE;
        } else if (data.size() == position) {
            return FOOTER_VIEW_TYPE;
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


    public void setOnPageLoadListener(OnPageLoadListener onPageLoadListener, int pageCount) {
        mOnPageLoadListener = onPageLoadListener;
        this.pageCount = pageCount;
    }

    public interface OnPageLoadListener {
        void onPageLoad();
    }
}
