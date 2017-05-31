package com.pisces.basequickadapter.example;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pisces.basequickadapter.R;
import com.pisces.basequickadapter.quickadapter.BaseQuickAdapter;
import com.pisces.basequickadapter.quickadapter.BaseViewHolder;

import java.util.List;

/**
 * Created by PiscesSu on 2017/4/21.
 * Version 1.0
 */

public class MovieQuickAdapter extends BaseQuickAdapter<MovieEntity.SubjectsBean> {
    private Context mContext;

    public MovieQuickAdapter(Context context, List<MovieEntity.SubjectsBean> data) {
        super(context, R.layout.rcv_item, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, MovieEntity.SubjectsBean item) {
        //set TextView
        holder.setText(R.id.title, "片名：" + item.getTitle())
                .setText(R.id.score, "评分：" + item.getRating().getAverage())
                .setText(R.id.director, "导演：" + item.getDirectors().get(0).getName())
                .setText(R.id.actor, "主演：" + item.getCasts().get(0).getName())
                .setText(R.id.type, "类型：" + item.getGenres().toString())
                .setText(R.id.year, "年份：" + item.getYear())
                .setText(R.id.tv_num, holder.getLayoutPosition() + "");
        //setImageView
        Glide.with(mContext).load(item.getImages().getMedium()).into((ImageView) holder.getView(R.id.iv));
    }
}
