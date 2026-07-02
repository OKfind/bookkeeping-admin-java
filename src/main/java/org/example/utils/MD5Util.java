package org.example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * MD5加密（32位小写）
     */
    public static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * MD5加盐加密
     */
    public static String md5WithSalt(String text, String salt) {
        return md5(text + salt);
    }

    /**
     * 验证明文与MD5是否匹配
     */
    public static boolean verify(String text, String md5Str) {
        return md5(text).equals(md5Str);
    }

    /**
     * 验证加盐MD5
     */
    public static boolean verifyWithSalt(String text, String salt, String md5Str) {
        return md5WithSalt(text, salt).equals(md5Str);
    }
}
