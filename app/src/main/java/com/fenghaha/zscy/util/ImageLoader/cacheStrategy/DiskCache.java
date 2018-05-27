package com.fenghaha.zscy.util.ImageLoader.cacheStrategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


import com.fenghaha.zscy.util.ImageLoader.util.MD5Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by FengHaHa on2018/5/24 0024 23:31
 */
public class DiskCache implements CacheStrategy {

    private final int DISK_CACHE_SIZE = 1024 * 1024 * 300;

    private static volatile DiskCache instance = null;

    private String mCacheDir;

    public static DiskCache getInstance(Context context) {
        if (instance == null) {//避免不必要的同步
            synchronized (MemoryCache.class) {
                if (instance == null) {
                    instance = new DiskCache(context);
                }
            }
        }
        return instance;
    }

    private DiskCache(Context context) {
        init(context);
    }

    private void init(Context context) {
        mCacheDir = getDiskCacheDir(context);
        File dir = new File(mCacheDir + File.separator + "imageCache");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        mCacheDir = dir.getPath();
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        if (mCacheDir == null) return;
        String key = MD5Utils.encrypt(url);
        FileOutputStream os = null;

        try {
            File file = new File(mCacheDir, key+".png");
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Bitmap get(String url) {
        if (mCacheDir == null) return null;
        String key = MD5Utils.encrypt(url);
        return BitmapFactory.decodeFile(mCacheDir + File.separator + key+".png");
    }

    private String getDiskCacheDir(Context context) {
        boolean externalStorageAvailable = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

}