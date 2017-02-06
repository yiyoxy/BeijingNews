package cn.tron.beijingnews.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.tron.beijingnews.base.BasePager;
import cn.tron.beijingnews.utils.Constants;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 */

public class NewsCenterPager extends BasePager {

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        Log.e("TAG", "新闻数据加载了");

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
}
