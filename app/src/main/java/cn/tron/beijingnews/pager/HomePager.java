package cn.tron.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import cn.tron.beijingnews.base.BasePager;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 */

public class HomePager extends BasePager {

    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        Log.e("TAG", "主页数据加载了");

        // 设置标题
        tv_title.setText("主页");

        // 实例视图
        TextView textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setText("主页面");
        textView.setTextColor(Color.RED);

        // 和父类的FrameLayout结合
        fl_main.addView(textView);
    }
}
