package cn.tron.beijingnews.detailpager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tron.baselibrary.utils.Constants;
import cn.tron.beijingnews.R;
import cn.tron.beijingnews.adapter.PhotosMenuDetailPagerAdapter;
import cn.tron.beijingnews.base.MenuDetailBasePager;
import cn.tron.beijingnews.bean.NewsCenterBean;
import cn.tron.beijingnews.bean.PhotosMenuBean;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 * <p>
 * 组图详情页面
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {

   /* @BindView(R.id.listview)
    ListView listview;
    @BindView(R.id.gridview)
    GridView gridview;*/

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private final NewsCenterBean.DataBean dataBean;
    private String url;
    private List<PhotosMenuBean.DataBean.NewsBean> news;

    private PhotosMenuDetailPagerAdapter adapter;

    public PhotosMenuDetailPager(Context mContext, NewsCenterBean.DataBean dataBean) {
        super(mContext);
        this.dataBean = dataBean;
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.photos_menu_detail_pager, null);

        // 注入view中,别忘记
        ButterKnife.bind(this, view);

        // 设置下拉多少距离起作用
        swipeRefreshLayout.setDistanceToTriggerSync(100);

        // 设置不同颜色, 可以设置多个颜色
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN, Color.BLACK, Color.WHITE, Color.YELLOW);

        // 设置背景颜色
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.holo_blue_bright);

        // 设置下拉刷新的监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(url);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        // 组图数据的链接
        url = Constants.BASE_URL + dataBean.getUrl();
        Log.e("TAG", "组图url==" + url);

        getDataFromNet(url);
    }

    private void getDataFromNet(String url) {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "请求成功==" + result);

                processData(result);

                //隐藏刷新按钮 : Whether or not the view should show refresh progress
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("TAG", "onFinished==");
            }
        });
    }

    private void processData(String json) {
        PhotosMenuBean photosBean = new Gson().fromJson(json, PhotosMenuBean.class);
        news = photosBean.getData().getNews();
        Log.e("TAG", "组图解析成功news, 0, title==" + news.get(0).getTitle());

        if (news != null && news.size() > 0) {

           /* // 设置listview的适配器
            adapter = new PhotosMenuDetailPagerAdapter();
            listview.setAdapter(adapter);*/

            // 设置recyclerview的适配器
            adapter = new PhotosMenuDetailPagerAdapter(mContext, photosBean.getData().getNews());
            recyclerview.setAdapter(adapter);

            // 设置布局管理器
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        } else {
            Toast.makeText(mContext, "没有请求到数据", Toast.LENGTH_SHORT).show();
        }
    }

    // false->不显示; true->显示
    private boolean isShowListView = true;

    // 切换listview和gridview的方法
    public void switchListGrid(ImageButton ib_switch) {
        if (isShowListView) {
            // 则显示GridView
            recyclerview.setLayoutManager(new GridLayoutManager(mContext, 2, LinearLayoutManager.VERTICAL, false));

            //  按钮->ListView , 注意是setImageResource, 不是setBackgroundResource
            ib_switch.setImageResource(R.drawable.icon_pic_list_type);

            isShowListView = false;
        } else {
            // 则显示ListView
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

            // 按钮->GridView
            ib_switch.setImageResource(R.drawable.icon_pic_grid_type);

            isShowListView = true;
        }
    }

   /* // false->不显示; true->显示
    private boolean isShowListView = true;

    // 切换listview和gridview的方法
    public void switchListGrid(ImageButton ib_switch) {
        if (isShowListView) {
            // 则显示GridView
            gridview.setAdapter(adapter);
            listview.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);

            //  按钮->ListView , 注意是setImageResource, 不是setBackgroundResource
            ib_switch.setImageResource(R.drawable.icon_pic_list_type);

            isShowListView = false;
        } else {
            // 则显示ListView
            listview.setAdapter(adapter);
            listview.setVisibility(View.VISIBLE);
            gridview.setVisibility(View.GONE);

            // 按钮->GridView
            ib_switch.setImageResource(R.drawable.icon_pic_grid_type);

            isShowListView = true;
        }
    }

    class PhotosMenuDetailPagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_photos_menu_detail_pager, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            // 根据位置得到对应的数据
            PhotosMenuBean.DataBean.NewsBean newsBean = news.get(position);
            // 用Glide加载并设置设置图片
            Glide.with(mContext).load(Constants.BASE_URL + newsBean.getLargeimage()).into(viewHolder.ivIcon);
            // 设置标题
            viewHolder.tvTitle.setText(newsBean.getTitle());

            return convertView;
        }
    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }*/
}
