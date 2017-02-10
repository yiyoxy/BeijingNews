package cn.tron.beijingnews.app;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by ZZB27 on 2017.2.6.0006.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        // 初始化ShareSDK
        ShareSDK.initSDK(this);
    }
}
