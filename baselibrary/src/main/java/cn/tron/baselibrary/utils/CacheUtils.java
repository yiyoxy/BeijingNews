package cn.tron.baselibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by ZZB27 on 2017.2.5.0005.
 *
 * 缓存工具类
 */

public class CacheUtils {

    // 保存参数
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("tron", Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    // 得到保存的参数
    public static boolean getBoolean(Context context, String key){
        SharedPreferences sp = context.getSharedPreferences("tron", Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    // 缓存文本信息, SD卡->sp存储
    public static void putString(Context context, String key, String value) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // sd卡可用, 则缓存文本到sd卡
            try {
                // 文件名
                String fileName = MD5Encoder.encode(key);
                // 文件路径夹
                String dir = Environment.getExternalStorageDirectory() + "/beijingnews/text/";
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

                // 保存到sdcard上
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(value.getBytes());
                fos.flush();
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // sp存储
            SharedPreferences sp = context.getSharedPreferences("tron", Context.MODE_PRIVATE);

            sp.edit().putString(key, value).commit();
        }
    }

    // 得到缓存的文本信息, sd卡->sp存储
    public static String getString(Context context, String key) {
        String result = "";
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // sd卡可用
            try {
                // 文件名
                String fileName = MD5Encoder.encode(key);
                // 文件路径夹
                String dir = Environment.getExternalStorageDirectory() + "/beijingnews/text/";
                // 拼接文件完整路径
                File file = new File(dir, fileName);

                if(file.exists()) {
                    int length;
                    byte[] buffer = new byte[1024];

                    // 文件输入流
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    while ((length = fis.read(buffer)) != -1){
                        byteArrayOutputStream.write(buffer, 0, length);
                    }

                    // 转换成字符串
                    result = byteArrayOutputStream.toString();

                    // 关闭流 , finally中关闭流?
                    fis.close();
                    byteArrayOutputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            SharedPreferences sp = context.getSharedPreferences("tron", Context.MODE_PRIVATE);

            result = sp.getString(key, "");
        }

        return result;
    }
}
