package cn.tron.beijingnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tron.baselibrary.utils.Constants;
import cn.tron.beijingnews.R;
import cn.tron.beijingnews.activity.PicassoSampleActivity;
import cn.tron.beijingnews.bean.PhotosMenuBean;

/**
 * Created by ZZB27 on 2017.2.9.0009.
 */

public class PhotosMenuDetailPagerAdapter extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapter.ViewHolder> {

    private List<PhotosMenuBean.DataBean.NewsBean> news;
    private Context mContext;

    public PhotosMenuDetailPagerAdapter(Context mContext, List<PhotosMenuBean.DataBean.NewsBean> news) {
        this.mContext = mContext;
        this.news = news;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.item_photos_menu_detail_pager, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // 根据位置得到数据
        PhotosMenuBean.DataBean.NewsBean newsBean = news.get(position);
        String imageRUrl = Constants.BASE_URL + newsBean.getLargeimage(); // 高清图

        // Glide加载图片
        Glide.with(mContext).load(imageRUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.news_pic_default) // 设置默认图片
                .error(R.drawable.news_pic_default) // 请求失败的图片
                .into(holder.ivIcon);

        // 设置标题
        holder.tvTitle.setText(newsBean.getTitle());
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // 设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PicassoSampleActivity.class);
                    /**
                     * 传入图片地址
                     * PhotosMenuBean.DataBean.NewsBean newsBean = news.get(getLayoutPosition());
                     * String largeimageUrl = newsBean.getLargeimage();
                     */
                    intent.putExtra("url", Constants.BASE_URL + news.get(getLayoutPosition()).getLargeimage());

                    mContext.startActivity(intent);
                }
            });

        }

    }

}
