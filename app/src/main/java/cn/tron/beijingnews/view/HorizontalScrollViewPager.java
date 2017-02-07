package cn.tron.beijingnews.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ZZB27 on 2017.2.7.0007.
 *
 * 自定义水平方向滑动ViewPager
 */

public class HorizontalScrollViewPager extends ViewPager {

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float startX;
    private float startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN :
                /**
                 * true:要求父元素，不拦截当前控件的事件
                 * false:要求父元素，拦截当前控件的事件
                 */
                getParent().requestDisallowInterceptTouchEvent(true);

                // 1.记录起始坐标
                startX = ev.getX();
                startY = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                // 2.来到结束坐标
                float endX = ev.getX();
                float endY = ev.getY();

                // 计算水平方向和竖直方向滑动的距离
                float distanceX = Math.abs(endX - startX);
                float distanceY = Math.abs(endY - startY);

                if(distanceX > distanceY) {
                    // 水平方向
                    // -->当页面是第0个, 并且滑动方向是左-->右
                    if(getCurrentItem() == 0 && endX - startX > 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    // -->当页面是最后一个页面，并且滑动方向是右-->左
                    else if(getCurrentItem() == getAdapter().getCount() - 1 && endX - startX < 0) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        // 其他页面
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                } else{
                    // 竖直方向
                    getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
