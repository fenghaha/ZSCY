package com.fenghaha.zscy.util.ImageLoader.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by FengHaHa on2018/5/24 0024 23:28
 */
public class MD5Utils {
    public static String encrypt(String url) {
        String code;
        final MessageDigest mDigest;
        try {
            mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            code = byte2HexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            code = String.valueOf(url.hashCode());
            e.printStackTrace();
        }
        return code;
    }

    private static String byte2HexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() == 1) sb.append("0");
            sb.append(hex);
        }
        return sb.toString();
    }
}