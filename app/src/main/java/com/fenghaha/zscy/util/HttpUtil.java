package com.fenghaha.zscy.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by FengHaHa on2018/5/25 0025 12:11
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";

    private static Handler handler = new Handler();


    private static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return (info != null && info.isAvailable());
    }

    public static void loadImage(String address, ImageCallback callback) {
        String imageName;
        Log.d(TAG, "原address=" + address);
        //截取出文件名
        String[] names = address.split("com/");
        imageName = names[names.length - 1];
        if (imageName.contains("/"))
            imageName = imageName.replaceAll("/", "");


        Log.d(TAG, "文件名=" + imageName);
        File file = new File(MyApplication.getContext().getExternalCacheDir().getPath() + "/" + imageName + ".png");
        if (file.exists()) {
            //文件存在则从文件中读取
            try {
                Log.d(TAG, "文件存在!!    name=" + imageName + "\n" + "path=" + file.getPath() + "\n" + file.getAbsolutePath() +
                        "\n" + file.getCanonicalPath());
                callback.onResponse(BitmapFactory.decodeFile(file.getPath()), "success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "文件不存在  name=" + imageName);
            //文件不存在则从网络中读取
            //先检测网络
            if (isNetworkAvailable()) {
                String finalImageName = imageName;
                new Thread(() -> {
                    HttpURLConnection connection;
                    try {
                        URL mUrl = new URL(address);
                        connection = (HttpURLConnection) mUrl.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setReadTimeout(5000);
                        connection.setConnectTimeout(8000);
                        connection.setDoInput(true);
                        connection.setUseCaches(false);//不缓存
                        connection.connect();

                        Log.d(TAG, "ResponseCod " + connection.getResponseCode());

                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                            handler.post(() -> callback.onResponse(bitmap, "success"));
                            //缓存图片
                            File file1 = new File(MyApplication.getContext().getExternalCacheDir() + "/" + finalImageName + ".png");
                            FileOutputStream os = new FileOutputStream(file1);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 70, os);
                        } else {
                            handler.post(() -> callback.onResponse(null, "failed"));
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                handler.post(() -> {
                    callback.onResponse(null, "failed");
                    ToastUtil.makeToast("网络不可用,请检查网络连接后再试");
                });

            }
        }
    }


    public static void post(String url, String param, HttpCallBack callBack) {

        if (isNetworkAvailable()) {
            //把开启新线程的操作封装在网络请求里
            new Thread(() -> {
                HttpURLConnection connection = null;
                //Log.d(TAG, "网络请求");
                try {
                    URL mUrl = new URL(url);
                    connection = (HttpURLConnection) mUrl.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(8000);
                    connection.setDoOutput(true);
                    OutputStream os = connection.getOutputStream();
                    os.write(param.getBytes());
                    os.flush();
                    os.close();
                    int responseCode = connection.getResponseCode();
                    Log.d("ResponseCode", "ResponseCode = " + responseCode);
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        Response response = new Response(getByteArrayFromIS(connection.getInputStream()));
                        // Log.d(TAG, "info = " + response.getInfo() + "status = " + response.getStatus());
                        handler.post(() -> callBack.onResponse(response));
                    } else {
                        handler.post(() -> {
                            ToastUtil.makeToast("服务器连接错误 responseCode = " + responseCode);
                            callBack.onFail("网络");
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            callBack.onFail("网络");
            handler.post(() -> ToastUtil.makeToast("网络不可用,请检查网络连接后再试"));
        }
    }

    private static byte[] getByteArrayFromIS(InputStream is) throws IOException {
        byte buff[] = new byte[1024];
        int len;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while ((len = is.read(buff)) != -1) {
            os.write(buff, 0, len);
        }
        byte[] bytes = os.toByteArray();
        is.close();
        os.close();
        return bytes;
    }

    public static class Response {
        private int status;
        private String data;
        private byte[] bytes;
        private String info;
        public Response(byte[] content) {
            bytes = content;
            String contentString = new String(content);

            if (JsonParser.has(contentString, "status")) {
                status = Integer.parseInt(JsonParser.getElement(contentString, "status"));
            } else {
                status = 200;
            }
            if (JsonParser.has(contentString,"info")) info = JsonParser.getElement(contentString,"info");
            data = JsonParser.getElement(contentString, "data");
        }

        public boolean isOk() {
            return status == 200;
        }

        public String getInfo() {
            return info;
        }

        public String getData() {
            return data;
        }

        public byte[] getBytes() {
            return bytes;
        }
    }

    public interface HttpCallBack {
        void onResponse(Response response);

        void onFail(String reason);
    }

    public interface ImageCallback {
        void onResponse(Bitmap bitmap, String info);
    }
}