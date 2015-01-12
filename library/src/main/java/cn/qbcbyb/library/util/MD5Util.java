package cn.qbcbyb.library.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 秋云 on 2014/8/26.
 */
public class MD5Util {
    public static String getMD5(String value) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (value == null) {
            return null;
        }
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(value.getBytes("UTF-8"));
        byte[] hash = md5.digest();// 加密
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toUpperCase();
    }
}
