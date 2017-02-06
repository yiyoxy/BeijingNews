package cn.tron.beijingnews.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.tron.beijingnews.base.BaseFragment;
import cn.tron.beijingnews.bean.NewsCenterBean;

/**
 * Created by ZZB27 on 2017.2.5.0005.
 */

public class LeftMenuFragment extends BaseFragment {

    private TextView textView;

    // 左侧菜单对应的数据
    private List<NewsCenterBean.DataBean> datas;

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
        textView.setText("左侧菜单: Fragment");
    }

    public void setData(List<NewsCenterBean.DataBean> dataBeanList) {
        this.datas = dataBeanList;

        for(int i = 0; i < datas.size(); i++) {
            Log.e("TAG", datas.get(i).getTitle());
        }
    }
}
