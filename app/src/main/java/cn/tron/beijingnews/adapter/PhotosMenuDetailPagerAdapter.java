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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetCacheUtils.SUCCESS :
                    int position = msg.arg1;
                    Bitmap bitmap = (Bitmap) msg.obj;
                    Log.e("TAG","接收到成功的消息" + ",位置:" + position + "bitmap:" + bitmap);

                    if(recyclerview.isShown()) {
                        ImageView ivIcon = (ImageView) recyclerview.findViewWithTag(position);
                        Log.e("TAG", "position=======" + position + ", ivIcon==" + recyclerview.findViewWithTag(position));

                        if(ivIcon != null && bitmap != null) {
                            Log.e("TAG","网络缓存图片显示成功"+position);
                            ivIcon.setImageBitmap(bitmap);
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

    private final RecyclerView recyclerview;

    private List<PhotosMenuBean.DataBean.NewsBean> news;
    private Context mContext;

    // 三级缓存工具类
    private BitmapCacheUtils bitmapCacheUtils;

    private DisplayImageOptions options;

    public PhotosMenuDetailPagerAdapter(Context mContext, List<PhotosMenuBean.DataBean.NewsBean> news, RecyclerView recyclerview) {
        this.mContext = mContext;
        this.news = news;

        this.recyclerview = recyclerview;

        // 初始化三级缓存工具类
        bitmapCacheUtils = new BitmapCacheUtils(handler); // 在构造方法中传递handler

        // 初始化DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.home_scroll_default)
                .showImageForEmptyUri(R.drawable.home_scroll_default)
                .showImageOnFail(R.drawable.home_scroll_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(15)) //设置矩形圆角图片
                .build();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(mContext, R.layout.item_photos_menu_detail_pager, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // 根据位置得到数据
        PhotosMenuBean.DataBean.NewsBean newsBean = news.get(position);

        // 设置标题
        holder.tvTitle.setText(newsBean.getTitle());

        String imageRUrl = Constants.BASE_URL + newsBean.getLargeimage(); // 高清图

        // 1.使用Glide请求图片
        // diskCacheStrategy(DiskCacheStrategy strategy).设置缓存策略。
        // DiskCacheStrategy.SOURCE：缓存原始数据，
        // DiskCacheStrategy.RESULT：缓存变换(如缩放、裁剪等)后的资源数据，
        // DiskCacheStrategy.NONE：什么都不缓存，
        // DiskCacheStrategy.ALL：缓存SOURC和RESULT。
        // 默认采用DiskCacheStrategy.RESULT策略，对于download only操作要使用DiskCacheStrategy.SOURCE。
        Glide.with(mContext).load(imageRUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.news_pic_default) // 设置默认图片
                .error(R.drawable.news_pic_default) // 请求失败的图片
                .into(holder.ivIcon);
        Log.e("TAG", "Glide加载图片==自带三级缓存");

        /*// 2.自定义三级缓存请求图片
        holder.ivIcon.setTag(position);  // 设置标识

        Bitmap bitmap = bitmapCacheUtils.getBitmapFromUrl(imageRUrl, position);

        if(bitmap != null) {
            // 当前bitmap不等于null,内容来自内存或者本地
            holder.ivIcon.setImageBitmap(bitmap);
        }*/

        /*// 3.使用ImageLoader加载图片
        ImageLoader.getInstance().displayImage(imageRUrl, holder.ivIcon, options);*/

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
