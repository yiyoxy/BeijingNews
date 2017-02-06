package cn.tron.beijingnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tron.beijingnews.R;
import cn.tron.beijingnews.utils.CacheUtils;
import cn.tron.beijingnews.utils.DensityUtil;

public class GuideActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.btn_start_main)
    Button btnStartMain;
    @BindView(R.id.ll_group_point)
    LinearLayout llGroupPoint;
    @BindView(R.id.iv_red_point)
    ImageView ivRedPoint;

    @OnClick(R.id.btn_start_main)
    public void onClick() {
        //1.保存参数，记录已经进入过引导页面，下次就不再进入引导界面
        CacheUtils.putBoolean(this, "isOpenMain", true);

        // 进入主界面
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private int[] ids = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

    // 间距
    private int leftMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        ButterKnife.bind(this); // 这里为何不要解绑?

        initData();
    }

    private void initData() {
        // 设置适配器
        viewpager.setAdapter(new MyPagerAdapter());

        // 监听页面的改变
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        // 根据多少个页面添加多少个灰色的点
        for(int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
            if(i != 0) {
                // 设置灰色点的间距
                params.leftMargin = DensityUtil.dip2px(this, 10);
            }
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.point_normal);

            // 添加到线性布局
            llGroupPoint.addView(imageView);
        }

        // View生命周期:测量-布局-绘制
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new MyOnGlobalLayoutListener());
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return ids.length;
        }

        // Create the page for the given position.
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(GuideActivity.this);
            imageView.setBackgroundResource(ids[position]);
            // 添加到viewpager
            container.addView(imageView);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当滑动了页面的时候回调
         *
         * @param position             当前页面滑动到的位置
         * @param positionOffset       滑动的百分比
         * @param positionOffsetPixels 滑动的单位(像素)
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            /**
             * 红点移动距离 = 间距 * 屏幕滑动的百分比
             * 红点移动的坐标 = 起始坐标 + 红点移动距离
             */
            //int marginLeft = (int) (leftMargin * positionOffset);
            //marginLeft = position * leftMargin + (int) (leftMargin * positionOffset);
            int marginLeft = (int) (leftMargin * (position   +  positionOffset));

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
            params.leftMargin = marginLeft;
            ivRedPoint.setLayoutParams(params);
        }

        // 当某个页面被选中的时候回调
        @Override
        public void onPageSelected(int position) {
            if(position == ids.length - 1) {
                // 显示按钮
                btnStartMain.setVisibility(View.VISIBLE);
            } else {
                // 隐藏按钮
                btnStartMain.setVisibility(View.GONE);
            }
        }

        // 当状态变化的时候回调
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            ivRedPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            // 间距 = 第1个点距离左边距离- 第0个点距离左边的距离
            leftMargin = llGroupPoint.getChildAt(1).getLeft() - llGroupPoint.getChildAt(0).getLeft();
        }
    }
}
