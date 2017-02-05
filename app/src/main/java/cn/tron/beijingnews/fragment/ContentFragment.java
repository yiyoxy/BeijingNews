package cn.tron.beijingnews.fragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.tron.beijingnews.R;
import cn.tron.beijingnews.base.BaseFragment;

/**
 * Created by ZZB27 on 2017.2.5.0005.
 */

public class ContentFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.rg_main)
    RadioGroup rgMain;

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
        rgMain.check(R.id.rb_news);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
