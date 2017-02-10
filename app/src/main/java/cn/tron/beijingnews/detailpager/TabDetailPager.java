package cn.tron.beijingnews.detailpager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tron.baselibrary.utils.CacheUtils;
import cn.tron.baselibrary.utils.Constants;
import cn.tron.baselibrary.utils.DensityUtil;
import cn.tron.beijingnews.R;
import cn.tron.beijingnews.activity.NewsDetailActivity;
import cn.tron.beijingnews.adapter.TabDetailPagerAdapter;
import cn.tron.beijingnews.base.MenuDetailBasePager;
import cn.tron.beijingnews.bean.NewsCenterBean;
import cn.tron.beijingnews.bean.TabDetailPagerBean;
import cn.tron.beijingnews.view.HorizontalScrollViewPager;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 */
public class TabDetailPager extends MenuDetailBasePager {

    public static final String ID_ARRAY = "id_array";

    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;

    @BindView(R.id.pull_refresh_list)
    PullToRefreshListView pullRefreshListView;

    ListView listview;

    HorizontalScrollViewPager viewpager;
    TextView tvTitle;
    LinearLayout llGroupPoint;

    private String url;

    private TabDetailPagerAdapter adapter;

    // 列表数据
    private List<TabDetailPagerBean.DataBean.NewsBean> news;

    // 更多(新闻)的路径
    private String moreUrl;

    // 是否加载更多
    private boolean isLoadMore = false;

    // 顶部轮播图的数据
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;

    private int preposition;

    private InternalHandler handler;

    public TabDetailPager(Context mContext, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(mContext);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        // 视图
        View view = View.inflate(mContext, R.layout.tab_detail_pager, null);
        ButterKnife.bind(this, view);

        listview = pullRefreshListView.getRefreshableView();

        View headerView = View.inflate(mContext, R.layout.header_view, null);

        viewpager = (HorizontalScrollViewPager) headerView.findViewById(R.id.viewpager);
        tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
        llGroupPoint = (LinearLayout) headerView.findViewById(R.id.ll_group_point);

        // 注意方法是addHeaderView
        listview.addHeaderView(headerView);

        // 添加刷新的声音
        SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(mContext);
        soundListener.addSoundEvent(PullToRefreshBase.State.PULL_TO_REFRESH, R.raw.pull_event);
        soundListener.addSoundEvent(PullToRefreshBase.State.RESET, R.raw.reset_sound);
        soundListener.addSoundEvent(PullToRefreshBase.State.REFRESHING, R.raw.refreshing_sound);
        pullRefreshListView.setOnPullEventListener(soundListener);

        // 设置下拉和上拉刷新
        pullRefreshListView.setOnRefreshListener(new MyOnRefreshListener2());

        // 设置listview的item点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 得到Bean对象
                TabDetailPagerBean.DataBean.NewsBean newsBean = news.get(position - 2);
                String title = newsBean.getTitle();
                int ids = newsBean.getId();

                // 获取是否已经存在, 如果不存在才保存
                String idArray = CacheUtils.getString(mContext, ID_ARRAY); //

                // 如果不包含才保存
                if (!idArray.contains(ids + "")) {
                    // 保存点击过的item的对应的id
                    CacheUtils.putString(mContext, ID_ARRAY, idArray + ids + ",");

                    // 刷新适配器 --> getCount-->getView
                    adapter.notifyDataSetChanged();
                }

                // 跳转到新闻浏览界面
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("url", Constants.BASE_URL + newsBean.getUrl());
                mContext.startActivity(intent);

                // 测试
                // MainActivity m = (MainActivity) mContext;
                // m.startActivity(intent);
                // m.finish();

            }
        });

        return view;
    }

    class MyOnRefreshListener2 implements com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView> {

        // 下拉刷新
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            isLoadMore = false;
            getDataFromNet();
        }

        // 上拉加载更多
        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            if (!TextUtils.isEmpty(moreUrl)) {
                isLoadMore = true;
                getMoreDataFromNet();
            } else {
                Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
                // 刷新完成
                pullRefreshListView.onRefreshComplete();
            }
        }
    }

    // 请求更多数据--> 加载更多新闻
    private void getMoreDataFromNet() {
        RequestParams params = new RequestParams(moreUrl);
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                processData(result);

                // 把下拉刷新和上拉刷新隐藏
                pullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();

        // eg. 新闻->children[北京, 中国, 国际, ...]
        url = Constants.BASE_URL + childrenBean.getUrl();
        Log.e("TAG", "TabDetailPager--url==" + url);

        // 软件数据缓存-得到缓存文本
        String savaJson = CacheUtils.getString(mContext, url);
        if(!TextUtils.isEmpty(savaJson)) {
            processData(savaJson);
        }
        // 设置数据
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "请求数据成功==TabDetailPager==" + childrenBean.getTitle());

                // 软件数据缓存-缓存文本
                CacheUtils.putString(mContext, url, result);

                processData(result);

                // 把下拉刷新和上拉刷新隐藏
                pullRefreshListView.onRefreshComplete();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求数据失败==TabDetailPager==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void processData(String json) {
        TabDetailPagerBean pagerBean = new Gson().fromJson(json, TabDetailPagerBean.class);

        // Log.e("TAG", "数据解析成功==TabDetailPager==" + pagerBean.getData().getNews().get(0).getTitle());

        String more = pagerBean.getData().getMore();
        if (TextUtils.isEmpty(more)) {
            moreUrl = "";
        } else {
            moreUrl = Constants.BASE_URL + more;
        }

        if (!isLoadMore) {
            // 原来的代码
            news = pagerBean.getData().getNews();

            // 设置适配器
            adapter = new TabDetailPagerAdapter(mContext, news);
            listview.setAdapter(adapter);

            // 设置顶部新闻(轮播图)

            // 设置viewpager的适配器
            topnews = pagerBean.getData().getTopnews();
            viewpager.setAdapter(new MyPagerAdapter());

            // 监听viewpager页面的变化
            viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

            // 设置topnews第一个新闻视图的标题
            tvTitle.setText(topnews.get(0).getTitle());

            // 把之前的指示点(视图)移除
            llGroupPoint.removeAllViews();

            // 添加红点
            for (int i = 0; i < topnews.size(); i++) {
                // 添加到线性布局
                ImageView point = new ImageView(mContext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, ViewGroup.LayoutParams.WRAP_CONTENT); // WRAP_CONTENT = -2

                if (i != 0) {
                    // 设置距离左边的距离(指示点间距)
                    params.leftMargin = DensityUtil.dip2px(mContext, 8);
                    point.setEnabled(false);
                } else {
                    point.setEnabled(true);
                }

                point.setLayoutParams(params);

                // 设置图片背景选择器
                point.setBackgroundResource(R.drawable.point_selector);

                llGroupPoint.addView(point);
            }
        } else {
            isLoadMore = false;
            // 更多
            List<TabDetailPagerBean.DataBean.NewsBean> moreNews = pagerBean.getData().getNews();
            news.addAll(moreNews);

            // 简写: news.addAll(pagerBean.getData().getNews());

            // 刷新适配器
            adapter.notifyDataSetChanged();
        }

        // 添加Handler
        if (handler == null) {
            handler = new InternalHandler();
        }

        // 移除消息
        handler.removeCallbacksAndMessages(null);

        // 发送延时消息
        handler.postDelayed(new MyRunnable(), 2000);

    }

    class InternalHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 接收消息执行这里, viewpager切换到下一个页面
            int item = (viewpager.getCurrentItem() + 1) % topnews.size();  // getCurrentItem -> Index of currently displayed page.当前页面的下标
            viewpager.setCurrentItem(item);
            handler.postDelayed(new MyRunnable(), 4000);
        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            // 设置默认的图片和联网请求的图片
            final TabDetailPagerBean.DataBean.TopnewsBean topnewsBean = topnews.get(position);

            Glide.with(mContext).load(Constants.BASE_URL + topnewsBean.getTopimage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_pic_default) // 设置默认图片
                    .error(R.drawable.news_pic_default) // 请求失败的图片
                    .into(imageView);

            // 添加到viewpager
            container.addView(imageView);

            // imageview触摸事件的监听: 滑动图片时消息的移除与发送
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // 按下的时候移除所有消息
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_UP:
                            // 抬起的时候移除所有消息并发送新的消息
                            handler.removeCallbacksAndMessages(null);
                            handler.postDelayed(new MyRunnable(), 4000);
                            break;
                    }

                    // 设为true, 则轮播图片的点击事件被消费, 点击图片就会没反应, 因此要返回false, 继续传递
                    return false;
                }
            });

            // 轮播图片的点击事件监听
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //跳转到新闻的浏览页面
                    Intent intent = new Intent(mContext, NewsDetailActivity.class);
                    intent.putExtra("url", Constants.BASE_URL + topnewsBean.getUrl());
                    mContext.startActivity(intent);

                }
            });

            return imageView;
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            //动态改变指示点
            llGroupPoint.getChildAt(preposition).setEnabled(false); // 上一个点变灰色
            llGroupPoint.getChildAt(position).setEnabled(true); // 当前的点变红色

            preposition = position;
        }

        @Override
        public void onPageSelected(int position) {
            tvTitle.setText(topnews.get(position).getTitle());
        }

        // viewpager状态变化时回调
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) { // 拖拽
                handler.removeCallbacksAndMessages(null);
            } else if (state == ViewPager.SCROLL_STATE_IDLE) { // 停顿, 闲置
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new MyRunnable(), 4000);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) { // 惯性
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new MyRunnable(), 4000);
            }

        }
    }
}
