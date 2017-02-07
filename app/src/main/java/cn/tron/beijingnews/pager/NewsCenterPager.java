package cn.tron.beijingnews.pager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import cn.tron.beijingnews.utils.CacheUtils;
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

        String saveJson = CacheUtils.getString(mContext, Constants.NEWSCENTER_PAGER_URL);

        if (!TextUtils.isEmpty(saveJson)) {
            processData(saveJson);
        }

        // 联网请求数据
        getDataFromNet();

        // 默认选中新闻详情页面
        // switchPager(0);

    }

    private void getDataFromNet() {
        RequestParams params = new RequestParams(Constants.NEWSCENTER_PAGER_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("TAG", "请求成功==" + result);

                // 缓存文本数据
                CacheUtils.putString(mContext, Constants.NEWSCENTER_PAGER_URL, result);

                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("TAG", "请求失败==" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.e("TAG", "onCancelled==" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                Log.e("TAG", "onFinished==");
            }
        });
    }

    // 解析数据; 绑定数据
    private void processData(String json) {

        // 1.解析数据:手动解析（用系统的Api解析）和第三方解析json的框架（Gson,fastjson）

        // 手动解析
        // NewsCenterBean centerBean = paraseJson(json);

        // Gson解析
        NewsCenterBean centerBean = new Gson().fromJson(json, NewsCenterBean.class);

        dataBeanList = centerBean.getData();

        // 2.把新闻中心的数据传递给左侧菜单
        MainActivity mainActivity = (MainActivity) mContext;

        // 3.得到左侧菜单
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();

        // 绑定数据
        menuDetailBasePagers = new ArrayList<>();

        menuDetailBasePagers.add(new NewsMenuDetailPager(mainActivity, dataBeanList.get(0))); //新闻详情页面
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

    /**
     * 使用系统的api手动解析json数据
     *
     * @param json
     * @return
     */
    private NewsCenterBean paraseJson(String json) {

        NewsCenterBean centerBean = new NewsCenterBean();
        try {
            JSONObject jsonObject = new JSONObject(json);
            int retcode = jsonObject.optInt("retcode");
            centerBean.setRetcode(retcode);
            JSONArray data = jsonObject.optJSONArray("data");

            //数据集合
            List<NewsCenterBean.DataBean> dataBeans = new ArrayList<>();
            centerBean.setData(dataBeans);

            for (int i = 0; i < data.length(); i++) {
                JSONObject itemObject = (JSONObject) data.get(i);
                if (itemObject != null) {


                    //集合装数据
                    NewsCenterBean.DataBean itemBean = new NewsCenterBean.DataBean();
                    dataBeans.add(itemBean);

                    int id = itemObject.optInt("id");
                    itemBean.setId(id);
                    String title = itemObject.optString("title");
                    itemBean.setTitle(title);
                    int type = itemObject.optInt("type");
                    itemBean.setType(type);
                    String url = itemObject.optString("url");
                    itemBean.setUrl(url);
                    String url1 = itemObject.optString("url1");
                    itemBean.setUrl1(url1);
                    String excurl = itemObject.optString("excurl");
                    itemBean.setExcurl(excurl);
                    String dayurl = itemObject.optString("dayurl");
                    itemBean.setDayurl(dayurl);
                    String weekurl = itemObject.optString("weekurl");
                    itemBean.setDayurl(weekurl);

                    JSONArray children = itemObject.optJSONArray("children");

                    if (children != null && children.length() > 0) {


                        //设置children的数据
                        List<NewsCenterBean.DataBean.ChildrenBean> childrenBeans = new ArrayList<>();
                        itemBean.setChildren(childrenBeans);
                        for (int j = 0; j < children.length(); j++) {

                            NewsCenterBean.DataBean.ChildrenBean childrenBean = new NewsCenterBean.DataBean.ChildrenBean();
                            //添加到集合中
                            childrenBeans.add(childrenBean);
                            JSONObject childenObje = (JSONObject) children.get(j);
                            int idc = childenObje.optInt("id");
                            childrenBean.setId(idc);
                            String titlec = childenObje.optString("title");
                            childrenBean.setTitle(titlec);
                            int typec = childenObje.optInt("type");
                            childrenBean.setType(typec);
                            String urlc = childenObje.optString("url");
                            childrenBean.setUrl(urlc);

                        }
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return centerBean;
    }
}
