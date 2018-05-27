package com.fenghaha.zscy.util.ImageLoader.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by FengHaHa on2018/5/24 0024 23:29
 */
public class ImageResizer {

    private static final String TAG = "ImageResizer";

    //从res中加载图片
    public static Bitmap decodeSampledBitmapFromResource(
            Resources res, int resID, int reqWidth, int reqHeight) {

        // 首先通过options.inJustDecodeBounds = true检查图片尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resID, options);
        //(只解析图片的原始宽高,而不是真正加载图片)

        //calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        //Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resID, options);
    }

    //从文件描述符读取图片
    public static Bitmap decodeSampledBitmapFromFileDescription(
            FileDescriptor fd, int reqWidth, int reqHeight ){
        // First decode with inJustDecodeBounds = true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        //calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        //Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return  BitmapFactory.decodeFileDescriptor(fd, null, options);
    }
    public static Bitmap decodeSampledBitmapFromInputStream(InputStream inputStream, int reqWidth, int reqHeight){
        inputStream.mark(Integer.MAX_VALUE);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream,null,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        try {
            inputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(inputStream,null,options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        //计算采样率
        if (reqHeight == 0 || reqWidth == 0) return 1;
        //图片的真实长宽
        final int height = options.outHeight;
        final int width = options.outWidth;
        Log.d(TAG, "origin, Width = " + width + " height= " + height);
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            //算出 使得宽高都大于需求宽高 且为2的n次幂 的最大的inSampleSize
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize <<= 1;//乘2
            }
            Log.d(TAG, "sampleSize: " + inSampleSize);
        }
        return inSampleSize;
    }
}