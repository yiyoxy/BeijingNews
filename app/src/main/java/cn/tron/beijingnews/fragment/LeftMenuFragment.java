package cn.tron.beijingnews.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.tron.beijingnews.R;
import cn.tron.beijingnews.activity.MainActivity;
import cn.tron.beijingnews.base.BaseFragment;
import cn.tron.beijingnews.bean.NewsCenterBean;
import cn.tron.beijingnews.pager.NewsCenterPager;
import cn.tron.beijingnews.utils.DensityUtil;

/**
 * Created by ZZB27 on 2017.2.5.0005.
 * <p>
 * 左侧菜单
 */

public class LeftMenuFragment extends BaseFragment {

    private ListView listView;

    // 左侧菜单对应的数据
    private List<NewsCenterBean.DataBean> datas;

    // 点击的位置
    private int preposition = 0;

    private LeftMenuFragmentAdapter adapter;

    @Override
    protected View initView() {

        listView = new ListView(mContext);
        listView.setPadding(0, DensityUtil.dip2px(mContext, 40), 0, 0);
        listView.setBackgroundColor(Color.BLACK);

        // 设置监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 1.记录位置和刷新适配器
                preposition = position;
                adapter.notifyDataSetChanged(); // getCount --> getView

                // 2.关闭侧滑菜单
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.getSlidingMenu().toggle(); // 开<->关

                // 3.切换到对应的详情页面
                switchPager(preposition);
            }
        });

        return listView;
    }

    // 根据位置切换到不同的详情页面
    private void switchPager(int preposition) {
        MainActivity mainActivity = (MainActivity) mContext;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        // 得到新闻中心
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        // 调用新闻中心的切换详情页面的方法
        newsCenterPager.switchPager(preposition);
    }

    @Override
    public void initData() {
        super.initData();
    }

    public void setData(List<NewsCenterBean.DataBean> dataBeanList) {
        this.datas = dataBeanList;

        // 设置适配器
        adapter = new LeftMenuFragmentAdapter();
        listView.setAdapter(adapter);

        // 默认选中新闻详情页面
        switchPager(preposition);
    }

    private class LeftMenuFragmentAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 每一个item就是一个textview
            TextView textView = (TextView) View.inflate(mContext, R.layout.item_leftmenu, null);

            // 设置内容
            textView.setText(datas.get(position).getTitle());

            if (preposition == position) {
                // 把颜色设置为高亮-红色
                textView.setEnabled(true);
            } else {
                // 把颜色设置为默认-白色
                textView.setEnabled(false);
            }

            return textView;
        }
    }
}
