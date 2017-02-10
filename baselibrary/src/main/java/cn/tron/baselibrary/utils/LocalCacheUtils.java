package cn.tron.baselibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by ZZB27 on 2017.2.10.0010.
 *
 * 本地缓存工具类
 */

public class LocalCacheUtils {

    private final MemoryCacheUtils memoryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        this.memoryCacheUtils = memoryCacheUtils;
    }

    public void putBitmap(String imageUrl, Bitmap bitmap) {
        try {
            // 文件名
            String fileName = MD5Encoder.encode(imageUrl);
            // 文件路径夹
            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/image/";
            // 拼接文件完整路径
            File file = new File(dir, fileName);

            // 获取父级目录
            File parent = file.getParentFile();
            if(!parent.exists()) {
                // 如果没有父级目录, 就创建多级目录
                parent.mkdirs();
            }

            // 创建文件
            if(!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 根据url获取图片
    public Bitmap getBitmapFromLocal(String imageUrl){
        try {
            // 文件名
            String fileName = MD5Encoder.encode(imageUrl);
            // 文件路径夹
            String dir = Environment.getExternalStorageDirectory() + "/beijingnews/image/";
            // 拼接文件完整路径
            File file = new File(dir, fileName);

            // 如果文件存在
            if(file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);

                // 当本地获取图片的时候,并且保存到内存中
                if(bitmap != null) {
                    // 同时缓存到内存中
                    memoryCacheUtils.putBitmap(imageUrl, bitmap);
                }
                return bitmap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
