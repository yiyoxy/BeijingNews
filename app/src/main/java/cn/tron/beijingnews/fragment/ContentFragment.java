package cn.tron.beijingnews.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.tron.beijingnews.R;
import cn.tron.beijingnews.base.BaseFragment;
import cn.tron.beijingnews.base.BasePager;
import cn.tron.beijingnews.pager.HomePager;
import cn.tron.beijingnews.pager.NewsCenterPager;
import cn.tron.beijingnews.pager.SettingPager;
import cn.tron.beijingnews.view.NoScrollViewPager;

/**
 * Created by ZZB27 on 2017.2.5.0005.
 */

public class ContentFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;

    private ArrayList<BasePager> basePagers;

    private Unbinder mUnbinder;

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.fragment_content, null);
        // 把view注入到ButterKnife
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        // 初始化三个页面
        initPager();

        // 设置viewpager的适配器
        setAdapter();

        // 设置监听RadioGroup状态选中的监听
        initListener();

    }

    private void initListener() {
        rgMain.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home :
                        viewpager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_news :
                        viewpager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_setting :
                        viewpager.setCurrentItem(2, false);
                        break;
                }
            }
        });

        // 默认选中新闻页
        rgMain.check(R.id.rb_news);

        // 监听页面的选中, 选中的页面再加载数据
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        // 孩子的视图和父类的FrameLayout结合
        basePagers.get(1).initData();
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // 调用initData
            basePagers.get(position).initData(); // 孩子的视图和父类的FrameLayout结合
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void setAdapter() {
        viewpager.setAdapter(new MyPagerAdapter());
    }

    class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return basePagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = basePagers.get(position);

            // 代表不同页面的实例
            View rootView = basePager.rootView;

            // 调用initData; 孩子的视图和父类的FrameLayout结合
            // basePager.initData();

            container.addView(rootView);

            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private void initPager() {
        basePagers = new ArrayList<>();
        // 主页
        basePagers.add(new HomePager(mContext));
        // 新闻
        basePagers.add(new NewsCenterPager(mContext));
        // 设置
        basePagers.add(new SettingPager(mContext));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
