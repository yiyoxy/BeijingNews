package cn.tron.baselibrary.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

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

    //本地缓存工具类
    private LocalCacheUtils localCacheutils;

    //内存缓存工具类
    private MemoryCacheUtils memoryCacheUtils;

    // 网络缓存工具类
    private NetCacheUtils netCacheUtils;

    public BitmapCacheUtils(Handler handler) {
        memoryCacheUtils = new MemoryCacheUtils();

        localCacheutils = new LocalCacheUtils(memoryCacheUtils);

        // 初始化网络缓存工具类, 传递handler, 联网要用到handler, 其他地方用不到
        netCacheUtils = new NetCacheUtils(handler, localCacheutils, memoryCacheUtils);
    }

    public Bitmap getBitmapFromUrl(String imageRUrl, int position) {
        // 1.从内存中取得图片
        if (memoryCacheUtils != null) {
            Bitmap bitmap = memoryCacheUtils.getBitmapFromMemory(imageRUrl);
            if (bitmap != null) {
                Log.e("TAG", "内存缓存图片成功==" + position);
                return bitmap;
            }
        }

        // 2.从本地文件中取得图片
        if (localCacheutils != null) {
            Bitmap bitmap = localCacheutils.getBitmapFromLocal(imageRUrl);
            if (bitmap != null) {
                Log.e("TAG", "本地缓存图片成功==" + position);
                return bitmap;
            }
        }

        // 3.请求网络图片, 获取图片, 显示到控件上
        netCacheUtils.getBitmapFromNet(imageRUrl, position);

        return null;
    }
}
