package cn.tron.beijingnews.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import cn.tron.beijingnews.R;
import cn.tron.beijingnews.adapter.TabDetailPagerAdapter;
import cn.tron.beijingnews.base.MenuDetailBasePager;
import cn.tron.beijingnews.bean.NewsCenterBean;
import cn.tron.beijingnews.bean.TabDetailPagerBean;
import cn.tron.beijingnews.utils.Constants;
import cn.tron.beijingnews.utils.DensityUtil;
import cn.tron.beijingnews.view.HorizontalScrollViewPager;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 */
public class TabDetailPager extends MenuDetailBasePager {

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

        // 设置数据
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "请求数据成功==TabDetailPager==" + childrenBean.getTitle());

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

            adapter.notifyDataSetChanged();
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
            Glide.with(mContext).load(Constants.BASE_URL + topnews.get(position).getTopimage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.news_pic_default) // 设置默认图片
                    .error(R.drawable.news_pic_default) // 请求失败的图片
                    .into(imageView);

            // 添加到viewpager
            container.addView(imageView);

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

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
