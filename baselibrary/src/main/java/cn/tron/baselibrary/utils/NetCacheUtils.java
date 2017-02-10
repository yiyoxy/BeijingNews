package cn.tron.baselibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZZB27 on 2017.2.10.0010.
 * <p>
 * 网络缓存工具类
 */

public class NetCacheUtils {

    // 请求图片成功
    public static final int SUCCESS = 1;
    // 请求图片失败
    public static final int FAILURE = 2;

    // 本质上的实例在PhotosMenuDetailPager
    private final Handler handler;

    // 线程池类
    private ExecutorService executorService;

    public NetCacheUtils(Handler handler) {
        this.handler = handler;

        executorService = Executors.newFixedThreadPool(10);
    }

    // 使用子线程去请求网络，把图片抓取起来，在发给主线程显示
    public void getBitmapFromNet(String imageRUrl, int position) {
        // new Thread(new InternalRunnable(imageRUrl, position)).start();

        executorService.execute(new InternalRunnable(imageRUrl, position));
    }

    class InternalRunnable implements Runnable {

        // 图片请求地址
        private String imageUrl;
        private final int position;

        public InternalRunnable(String imageRUrl, int position) {
            this.imageUrl = imageRUrl;
            this.position = position;
        }

        // 子线程中进行联网操作(耗时操作), 防止阻塞主线程
        @Override
        public void run() {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(imageUrl).openConnection();
                connection.setRequestMethod("GET"); // 不能小写
                connection.setConnectTimeout(3000); //连接超时
                connection.setReadTimeout(3000); //读取超时
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    // 请求图片成功
                    InputStream inputStream = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    // 向本地存一份

                    // 向内存中存一份

                    // 发送到主线程显示在控件上
                    // Message msg = handler.obtainMessage();
                    Message msg = Message.obtain();
                    msg.what = SUCCESS;
                    msg.arg1 = position;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);

                }
            } catch (IOException e) {
                e.printStackTrace();
                // 请求失败,发送失败的消息
                Message msg = Message.obtain();
                msg.what = FAILURE;
                msg.arg1 = position;
                handler.sendMessage(msg);
            } finally {
                if(connection != null) {
                    // 断开连接
                    connection.disconnect();
                }
            }
        }
    }

}
