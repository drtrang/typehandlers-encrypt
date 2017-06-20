package com.github.trang.typehandlers.util;

/**
 * 加密工具类
 *
 * @author trang
 */
public final class EncryptUtil {

    private EncryptUtil() {
        throw new UnsupportedOperationException();
    }

    // 加密后的最短密文长度，用于兼容尚未刷完的数据
    private static final int LENGTH = 32;

    public static boolean isEncrypted(String content) {
        return !StringUtil.isNumeric(content) && content.length() >= LENGTH;
    }

    public static String encrypt(String content) {
        return ConfigUtil.getCrypt().encrypt(content);
    }

    public static String encrypt(String content, String key) {
        return ConfigUtil.getCrypt().encrypt(content, key);
    }

    public static String decrypt(String content) {
        return ConfigUtil.getCrypt().decrypt(content);
    }

    public static String decrypt(String content, String key) {
        return ConfigUtil.getCrypt().decrypt(content, key);
    }

}