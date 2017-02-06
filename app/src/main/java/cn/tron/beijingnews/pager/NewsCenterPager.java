package cn.tron.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.tron.beijingnews.activity.MainActivity;
import cn.tron.beijingnews.base.BasePager;
import cn.tron.beijingnews.base.MenuDetailBasePager;
import cn.tron.beijingnews.bean.NewsCenterBean;
import cn.tron.beijingnews.detailpager.InteractMenuDetailPager;
import cn.tron.beijingnews.detailpager.NewsMenuDetailPager;
import cn.tron.beijingnews.detailpager.PhotosMenuDetailPager;
import cn.tron.beijingnews.detailpager.TopicMenuDetailPager;
import cn.tron.beijingnews.fragment.LeftMenuFragment;
import cn.tron.beijingnews.utils.Constants;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 */

public class NewsCenterPager extends BasePager {

    // 左侧菜单对应的数据
    private List<NewsCenterBean.DataBean> dataBeanList;

    private ArrayList<MenuDetailBasePager> menuDetailBasePagers;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        Log.e("TAG", "新闻数据加载了");

        // 显示菜单按钮
        ib_menu.setVisibility(View.VISIBLE);

        // 设置标题
        tv_title.setText("新闻");

        // 实例视图
        TextView textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setText("新闻中心");
        textView.setTextColor(Color.RED);

        // 和父类的FrameLayout结合
        fl_main.addView(textView);

        // 联网请求数据
        getDataFromNet();
    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG","请求成功=="+result);

                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG","请求失败=="+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG","onCancelled=="+cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("TAG","onFinished==");
            }
        });
    }

    // 解析数据; 绑定数据
    private void processData(String json) {

        // 1.解析数据:手动解析（用系统的Api解析）和第三方解析json的框架（Gson,fastjson）
        NewsCenterBean centerBean = new Gson().fromJson(json, NewsCenterBean.class);
        dataBeanList = centerBean.getData();

        // 2.把新闻中心的数据传递给左侧菜单
        MainActivity mainActivity = (MainActivity) mContext;

        // 3.得到左侧菜单
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();

        // 绑定数据
        menuDetailBasePagers = new ArrayList<>();

        menuDetailBasePagers.add(new NewsMenuDetailPager(mainActivity)); //新闻详情页面
        menuDetailBasePagers.add(new TopicMenuDetailPager(mainActivity)); //专题详情页面
        menuDetailBasePagers.add(new PhotosMenuDetailPager(mainActivity)); //组图详情页面
        menuDetailBasePagers.add(new InteractMenuDetailPager(mainActivity)); //互动详情页面

        // 4.调用leftMenuFragment的setData
        leftMenuFragment.setData(dataBeanList);

    }

    // 根据位置切换到不同的详情页面
    public void switchPager(int preposition) {
        // 设置标题
        tv_title.setText(dataBeanList.get(preposition).getTitle());

        MenuDetailBasePager menuDetailBasePager = menuDetailBasePagers.get(preposition);
        menuDetailBasePager.initData();

        // 视图
        View rootView = menuDetailBasePager.rootView;

        // 移除之前的
        fl_main.removeAllViews();

        fl_main.addView(rootView);
    }
}
