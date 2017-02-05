package cn.tron.beijingnews.activity;

import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import cn.tron.beijingnews.R;
import cn.tron.beijingnews.utils.DensityUtil;

public class MainActivity extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1.设置主页面
        setContentView(R.layout.activity_main);

        // 2.设置左侧菜单
        setBehindContentView(R.layout.leftmenu);

        // 3.设置右侧菜单
        SlidingMenu slidingMenu = getSlidingMenu();
        //slidingMenu.setSecondaryMenu(R.layout.leftmenu);

        // 4.设置模式: 左侧 + 主页; 左侧 + 主页 + 右侧; 主页 + 右侧
        slidingMenu.setMode(SlidingMenu.LEFT); // 左侧 + 主页

        // 5.设置滑动的模式: 全屏, 边缘, 不可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // 6.设置主页面占的宽度dip(dp)
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this, 200));
    }
}
