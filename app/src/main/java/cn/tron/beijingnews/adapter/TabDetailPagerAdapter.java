package cn.tron.beijingnews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tron.beijingnews.R;
import cn.tron.beijingnews.bean.TabDetailPagerBean;
import cn.tron.beijingnews.utils.Constants;

/**
 * Created by ZZB27 on 2017.2.7.0007.
 */

public class TabDetailPagerAdapter extends BaseAdapter {

    private final Context mContext;

    private final List<TabDetailPagerBean.DataBean.NewsBean> datas;

    public TabDetailPagerAdapter(Context mContext, List<TabDetailPagerBean.DataBean.NewsBean> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

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

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_tab_detailpager, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 根据位置得到对应的数据
        TabDetailPagerBean.DataBean.NewsBean newsBean = datas.get(position);
        viewHolder.tvTitle.setText(newsBean.getTitle());
        viewHolder.tvTime.setText(newsBean.getPubdate());

        // Glide加载图片
        Glide.with(mContext).load(Constants.BASE_URL + newsBean.getListimage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.news_pic_default)
                .error(R.drawable.news_pic_default)
                .into(viewHolder.ivIcon);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
