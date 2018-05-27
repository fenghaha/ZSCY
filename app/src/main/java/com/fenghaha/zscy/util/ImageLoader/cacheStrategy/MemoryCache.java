package com.fenghaha.zscy.util.ImageLoader.cacheStrategy;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.fenghaha.zscy.util.ImageLoader.util.MD5Utils;


/**
 * Created by FengHaHa on2018/5/24 0024 23:30
 */
public class MemoryCache implements CacheStrategy {

    private LruCache<String, Bitmap> mMemoryCache;

    public static MemoryCache getInstance() {
        return MemoryCacheHolder.instance;
    }

    private static class MemoryCacheHolder {
        private static final MemoryCache instance = new MemoryCache();
    }

    private MemoryCache() {
        init();
    }

    private void init() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };


    }

    @Override
    public void put(String url, Bitmap bitmap) {
        String key = MD5Utils.encrypt(url);
        if (get(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    @Override
    public Bitmap get(String url) {
        String key = MD5Utils.encrypt(url);
        return mMemoryCache.get(key);
    }
}
