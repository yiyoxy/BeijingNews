package cn.tron.beijingnews.detailpager;

import android.content.Context;
import android.view.View;

import cn.tron.beijingnews.R;
import cn.tron.beijingnews.base.MenuDetailBasePager;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 * <p>
 * 组图详情页面
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {

    public PhotosMenuDetailPager(Context mContext) {
        super(mContext);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.photos_menu_detail_pager, null);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
    }
}
