package com.fenghaha.zscy.util;

import android.widget.Toast;

/**
 * Created by FengHaHa on2018/5/25 0025 12:12
 */
public class ToastUtil {
    public static void makeToast(String content) {
        Toast.makeText(MyApplication.getContext(), content, Toast.LENGTH_SHORT).show();
    }
}
