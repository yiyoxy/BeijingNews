package cn.tron.beijingnews.utils;

import android.content.Context;
import android.content.SharedPreferences;

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
}
