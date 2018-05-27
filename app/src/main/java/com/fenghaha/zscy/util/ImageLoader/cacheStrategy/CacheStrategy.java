package com.fenghaha.zscy.util.ImageLoader.cacheStrategy;

import android.graphics.Bitmap;

/**
 * Created by FengHaHa on2018/5/24 0024 21:04
 */
public interface CacheStrategy {
    void put(String url, Bitmap bitmap);
    Bitmap get(String url);
}
