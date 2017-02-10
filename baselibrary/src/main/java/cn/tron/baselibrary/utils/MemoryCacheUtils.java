package cn.tron.baselibrary.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by ZZB27 on 2017.2.10.0010.
 *
 * 内存缓存工具类
 */

public class MemoryCacheUtils {

    // LruCache是android提供的一个缓存工具类
    private LruCache<String, Bitmap> lruCache;

    public MemoryCacheUtils() {

        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);

        this.lruCache = new LruCache<String, Bitmap>(maxSize){

            // 计算每张图片的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    // 根据url存储图片到内存中
    public void putBitmap(String imageUrl, Bitmap bitmap) {
        // 缓存到内存中
        lruCache.put(imageUrl, bitmap);
    }

    // 根据url获取内存中的图片
    public Bitmap getBitmapFromMemory(String imageUrl){
        return  lruCache.get(imageUrl);
    }
}
