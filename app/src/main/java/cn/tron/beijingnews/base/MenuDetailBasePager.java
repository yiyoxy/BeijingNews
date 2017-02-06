package cn.tron.beijingnews.base;

import android.content.Context;
import android.view.View;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 *
 * NewsMenuDetailPager、TopicMenuDetailPager、PhotosMenuDetailPager、InteractMenuDetailPager的基类
 */

public abstract class MenuDetailBasePager {

    // 上下文
    public final Context mContext;

    // 代表各个菜单详情页面的实例(视图)
    public View rootView;

    public MenuDetailBasePager(Context mContext) {
        this.mContext = mContext;
        rootView = initView();
    }

    // 由子类实现该方法, 初始化子类的视图
    public abstract View initView();

    // 绑定数据或者请求数据再绑定数据
    public void initData(){

    }
}
