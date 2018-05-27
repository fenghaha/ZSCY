package com.fenghaha.zscy.util.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;


import com.fenghaha.zscy.util.ImageLoader.cacheStrategy.CacheStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by FengHaHa on2018/5/24 0024 23:31
 */
public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private CacheStrategy imageCache;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    public static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    public static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    public static final long KEEP_ALIVE = 10L;


    private ExecutorService threadPool = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<Runnable>()
    );
    private Handler handler = new Handler(Looper.getMainLooper());


    /**
     * 设置具体的Cache方案(这是一种策略模式);也体现了面向接口编程(面向抽象编程的优点)。
     * 这是面向对象几大原则核心体现地方，稍后再进行解释
     *
     * @param imageCache
     */
    public void setImageCache(CacheStrategy imageCache) {
        this.imageCache = imageCache;
    }

    /**
     * 对网上拉取还是本地拉取Bitmap进行一个判断
     *
     * @param url
     * @param imageView
     */
    public void displayImage(String url, ImageView imageView) {
        Bitmap bitmap = imageCache.get(url);
        imageView.setTag(url);
        if (bitmap != null) {
            if (imageView.getTag().equals(url)) {
                imageView.setImageBitmap(bitmap);
            }
            return;
        }
        //如果图片没有缓存就进行网上下载操作
        submitLoadRequest(url, imageView);
    }

    /**
     * 在线程池中进行网络请求，防止应用无响应(Application Not Response，ANR)
     *
     * @param url
     * @param imageView
     */
    private void submitLoadRequest(final String url, final ImageView imageView) {
        imageView.setTag(url);
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    handler.post(() -> imageView.setImageBitmap(bitmap));
                }
                imageCache.put(url, bitmap);
            }
        });
    }

    /**
     * 网络请求的具体实现方法
     *
     * @param url
     * @return
     */
    private Bitmap downloadImage(String url) {

        Bitmap bitmap = null;
        HttpURLConnection connection;
        InputStream is = null;
        try {
            URL mUrl = new URL(url  .replaceAll("http", "https"));
            connection = (HttpURLConnection) mUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(8000);
            connection.setDoInput(true);
            connection.setUseCaches(false);//不缓存
            connection.connect();
            is = connection.getInputStream();
            Log.d(TAG, "url = "+url+"  ResponseCode " + connection.getResponseCode());

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
//        Bitmap bitmap = null;
//        Log.d(TAG, "downloadImage: "+url);
//        InputStream inputStream = null;
//        BufferedInputStream bufferedInputStream = null;
//        try {
//            URL imageUrl = new URL(url);
//            final HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
//            inputStream = conn.getInputStream();
//            bufferedInputStream = new BufferedInputStream(inputStream);
//
//            bitmap = BitmapFactory.decodeStream(bufferedInputStream);
//            //bitmap = ImageResizer.decodeSampledBitmapFromInputStream(bufferedInputStream, 150, 150);
//            conn.disconnect();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//                if (bufferedInputStream != null) {
//                    bufferedInputStream.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return bitmap;
}