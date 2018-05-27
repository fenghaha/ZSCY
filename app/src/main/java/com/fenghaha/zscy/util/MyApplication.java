package com.fenghaha.zscy.util;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.fenghaha.zscy.bean.User;

/**
 * Created by FengHaHa on2018/5/25 0025 12:13
 */
public class MyApplication extends Application {

    private static Context mContext;
    private static User user;
    private static final String TAG = "MyApplication";
    private static boolean isLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        user = new User();
        user.setScore(20);
        user.setStuNum(2017210051);
        user.setIdCardNum("080810");
        user.setNickName("冯哈哈哈");
        Log.d(TAG, "onCreate: " + mContext.toString());
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MyApplication.user = user;
        isLogin = true;
    }

    public static boolean isIsLogin() {
        return isLogin;
    }

    public static Context getContext() {
        return mContext;
    }
}
