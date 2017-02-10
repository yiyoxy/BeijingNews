package cn.tron.beijingnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tron.baselibrary.utils.BitmapCacheUtils;
import cn.tron.baselibrary.utils.Constants;
import cn.tron.baselibrary.utils.NetCacheUtils;
import cn.tron.beijingnews.R;
import cn.tron.beijingnews.activity.PicassoSampleActivity;
import cn.tron.beijingnews.bean.PhotosMenuBean;

/**
 * Created by ZZB27 on 2017.2.9.0009.
 */

public class PhotosMenuDetailPagerAdapter extends RecyclerView.Adapter<PhotosMenuDetailPagerAdapter.ViewHolder> {

    private final RecyclerView recyclerview;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCacheUtils.SUCCESS :
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;

                    if(recyclerview.isShown()) {
                        ImageView imageView = (ImageView) recyclerview.findViewWithTag(position);
                        if(imageView != null && bitmap != null) {
                            Log.e("TAG","网络缓存图片显示成功"+position);
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                    break;
                case NetCacheUtils.FAILURE:
                    position = msg.arg1;
                    Log.e("TAG","网络缓存失败=="+position);
                    break;
            }
        }
    };

    private List<PhotosMenuBean.DataBean.NewsBean> news;
    private Context mContext;

    // 三级缓存工具类
    private BitmapCacheUtils bitmapCacheUtils;

    public PhotosMenuDetailPagerAdapter(Context mContext, List<PhotosMenuBean.DataBean.NewsBean> news, RecyclerView recyclerview) {
        this.mContext = mContext;
        this.news = news;

        this.recyclerview = recyclerview;

        // 初始化三级缓存工具类
        bitmapCacheUtils = new BitmapCacheUtils(handler); // 在构造方法中传递handler
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

//        // 1.使用Glide请求图片
//        Glide.with(mContext).load(imageRUrl)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.news_pic_default) // 设置默认图片
//                .error(R.drawable.news_pic_default) // 请求失败的图片
//                .into(holder.ivIcon);

        // 2.自定义三级缓存请求图片
        // 设置标识
        holder.ivIcon.setTag(position);

        Bitmap bitmap = bitmapCacheUtils.getBitmapFromUrl(imageRUrl, position);

        if(bitmap != null) {
            // 当前bitmap不等于null,内容来自内存或者本地
            holder.ivIcon.setImageBitmap(bitmap);
        }

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
