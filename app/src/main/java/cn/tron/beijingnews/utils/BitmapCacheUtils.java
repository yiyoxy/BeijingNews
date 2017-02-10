package cn.tron.beijingnews.utils;

import android.graphics.Bitmap;
import android.os.Handler;

/**
 * Created by ZZB27 on 2017.2.10.0010.
 * <p>
 * 三级图片缓存工具类
 * <p>
 * 根据Url取图片
 * 1.先根据Url去内存中取，如果取到直接返回
 * 2.再根据Url去本地中取，如果取到直接返回
 * 3.最后根据Url网络中取，取到后发送到主线程显示
 *     3.1 根据Url向内存中存一份
 *     3.2 根据Url向本地中存一份
 */

public class BitmapCacheUtils {

    // 网络缓存工具类
    private NetCacheUtils netCacheUtils;

    public BitmapCacheUtils(Handler handler) {
        // 初始化网络缓存工具类, 传递handler, 联网要用到handler, 其他地方用不到
        netCacheUtils = new NetCacheUtils(handler);
    }

    public Bitmap getBitmapFromUrl(String imageRUrl, int position) {

        // 从内存中取得图片

        // 从本地文件中取得图片

        // 请求网络图片, 获取图片, 显示到控件上
        netCacheUtils.getBitmapFromNet(imageRUrl, position);

        return null;
    }
}
