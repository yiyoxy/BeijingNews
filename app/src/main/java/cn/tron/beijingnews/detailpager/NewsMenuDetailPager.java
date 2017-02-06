package cn.tron.beijingnews.detailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.tron.beijingnews.base.MenuDetailBasePager;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 *
 * 新闻详情页面
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {

    private TextView textView;

    public NewsMenuDetailPager(Context mContext) {
        super(mContext);
    }

    @Override
    public View initView() {

        // 新闻详情页面的视图
        textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);

        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("新闻详情页面内容");
    }
}
