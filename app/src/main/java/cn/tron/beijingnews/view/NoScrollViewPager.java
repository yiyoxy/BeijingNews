package cn.tron.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 */

public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 屏蔽viewpager的滑动功能
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false; // 不拦截
    }

}
