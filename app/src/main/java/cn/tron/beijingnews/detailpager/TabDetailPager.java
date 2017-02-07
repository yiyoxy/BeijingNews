package cn.tron.beijingnews.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

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

/**
 * Created by ZZB27 on 2017.2.6.0006.
 */
public class TabDetailPager extends MenuDetailBasePager {

    private final NewsCenterBean.DataBean.ChildrenBean childrenBean;

    @BindView(R.id.listview)
    ListView listview;

    ViewPager viewpager;
    TextView tvTitle;
    LinearLayout llGroupPoint;

    private String url;

    private TabDetailPagerAdapter adapter;

    // 列表数据
    private List<TabDetailPagerBean.DataBean.NewsBean> news;

    // 顶部轮播图的数据
    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;

    public TabDetailPager(Context mContext, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(mContext);
        this.childrenBean = childrenBean;
    }


    @Override
    public View initView() {
        // 视图
        View view = View.inflate(mContext, R.layout.tab_detail_pager, null);
        ButterKnife.bind(this, view);

        View headerView = View.inflate(mContext, R.layout.header_view, null);
        viewpager = (ViewPager) headerView.findViewById(R.id.viewpager);
        tvTitle = (TextView) headerView.findViewById(R.id.tv_title);
        llGroupPoint = (LinearLayout) headerView.findViewById(R.id.ll_group_point);

        // 注意方法是addHeaderView
        listview.addHeaderView(headerView);

        return view;
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
        Log.e("TAG", "数据解析成功==TabDetailPager==" + pagerBean.getData().getNews().get(0).getTitle());

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
        // 设置第一个为默认
        tvTitle.setText(topnews.get(0).getTitle());
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
