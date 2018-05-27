package com.fenghaha.zscy.util.ImageLoader.cacheStrategy;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by FengHaHa on2018/5/24 0024 23:31
 */
public class DoubleCache implements CacheStrategy {
    private MemoryCache memoryCache;
    private DiskCache diskCache;

    public DoubleCache(Context context) {
        memoryCache = MemoryCache.getInstance();
        diskCache = DiskCache.getInstance(context);
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        memoryCache.put(url, bitmap);
        diskCache.put(url, bitmap);
    }

    @Override
    public Bitmap get(String url) {
        Bitmap bitmap = memoryCache.get(url);
        return bitmap == null ? diskCache.get(url) : bitmap;
    }
}
