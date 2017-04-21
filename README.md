# BaseQuickAdapter
---
简单封装了下RecyclerViewAdapter，避免每次都要写一堆重复代码

---
1. 设置空布局 setEmptyView();
2. 设置点击监听 setOnItemClickListener();

---

#### 用法
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
                .setText(R.id.year, "年份：" + item.getYear());
        //setImageView
        Glide.with(mContext).load(item.getImages().getMedium()).into((ImageView) holder.getView(R.id.iv));
    }
    }
---

![https://github.com/piscessu/BaseQuickAdapter/blob/master/screenshots/1.png](http://)
![https://github.com/piscessu/BaseQuickAdapter/blob/master/screenshots/2.png](http://)
![https://github.com/piscessu/BaseQuickAdapter/blob/master/screenshots/3.png](http://)


---
**持续改进中。。。**