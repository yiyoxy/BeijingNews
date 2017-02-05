package cn.tron.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import cn.tron.beijingnews.base.BasePager;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 */

public class SettingPager extends BasePager {

    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        // 设置标题
        tv_title.setText("设置");

        // 实例视图
        TextView textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setText("设置");
        textView.setTextColor(Color.RED);

        // 和父类的FrameLayout结合
        fl_main.addView(textView);
    }
}
