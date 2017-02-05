package cn.tron.beijingnews.fragment;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.tron.beijingnews.base.BaseFragment;

/**
 * Created by ZZB27 on 2017.2.5.0005.
 */

public class ContentFragment extends BaseFragment {

    private TextView textView;

    @Override
    protected View initView() {
        textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("主页: Fragment");
    }
}
