package cn.tron.beijingnews.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.tron.beijingnews.R;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 * <p>
 * 视图的基类: HomePager、NewsCenterPager、SettingPager都继承该类
 * 在子类中重写initData方法，实现子类的视图，并且视图在该方法中和基类的Framelayout布局结合在一起
 */

public class BasePager {

    // 上下文
    public final Context mContext;

    public ImageButton ib_menu;
    public TextView tv_title;
    public FrameLayout fl_main;

    // 代表各个页面的实例
    public View rootView;

    public BasePager(Context context) {
        this.mContext = context;

        rootView = initView();
    }

    private View initView() {
        View view = View.inflate(mContext, R.layout.basepager, null);
        ib_menu = (ImageButton) view.findViewById(R.id.ib_menu);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        fl_main = (FrameLayout) view.findViewById(R.id.fl_main);
        return view;
    }

    /**
     * 1.在子类重新initData方法，实现子类的视图，并且视图在该方法中和基类的Fragmelayout布局结合在一起
     * 2.绑定数据或者请求数据再绑定数据
     */
    public void initData() {

    }
}
