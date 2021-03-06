package cn.tron.beijingnews.detailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tron.beijingnews.R;
import cn.tron.beijingnews.activity.MainActivity;
import cn.tron.beijingnews.base.MenuDetailBasePager;
import cn.tron.beijingnews.bean.NewsCenterBean;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 * <p>
 * 新闻详情页面
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {

    // 新闻详情页面的数据
    private final List<NewsCenterBean.DataBean.ChildrenBean> childrenData;

    // 新闻标签页面的集合
    private ArrayList<TabDetailPager> tabDetailPagers;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.indicator)
    TabPageIndicator indicator;

    @BindView(R.id.ib_news_menu_detail_next_tab)
    ImageButton ibNewsMenuDetailNextTab;

    // tab页面的位置
    private int prePosition;

    public NewsMenuDetailPager(Context mContext, NewsCenterBean.DataBean dataBean) {
        super(mContext);
        this.childrenData = dataBean.getChildren();  // 12条
    }

    @Override
    public View initView() {

        // 新闻详情页面的视图
        View view = View.inflate(mContext, R.layout.news_menu_detail_pager, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();

        // 准备数据-页面
        tabDetailPagers = new ArrayList<>();
        // 根据有多少数据创建多少个TabDetailPager，并且把数据传入到对象中
        for (int i = 0; i < childrenData.size(); i++) {
            tabDetailPagers.add(new TabDetailPager(mContext, childrenData.get(i)));
        }

        // 设置适配器
        viewpager.setAdapter(new MyPagerAdapter());

        // 要在设置适配器之后
        indicator.setViewPager(viewpager);

        //监听页面的变化用TabPageIndicator
        indicator.setOnPageChangeListener(new MyOnPageChangeListener());

        // 切换到记录的位置
        indicator.setCurrentItem(prePosition);
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            // 修改页面不能切换的BUG,记录当前的位置
            prePosition = viewpager.getCurrentItem();

            MainActivity mainActivity = (MainActivity) mContext;
            if (position == 0) {
                // 可以把左侧菜单拖拽出来
                mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            } else {
                // 不能把左侧菜单拖拽出来
                mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    // 点击切换到下一个页签
    @OnClick(R.id.ib_news_menu_detail_next_tab)
    public void onClick() {

        // 防止最后一个下划线溢出
        // 下一个页签的位置
        int nextPosition = viewpager.getCurrentItem() + 1;
        // 先判断再切换
        if (nextPosition <= childrenData.size() - 1) {
            //切换到下一个页面
            indicator.setCurrentItem(nextPosition);
        }

    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public CharSequence getPageTitle(int position) {
            return childrenData.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            tabDetailPager.initData();
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}