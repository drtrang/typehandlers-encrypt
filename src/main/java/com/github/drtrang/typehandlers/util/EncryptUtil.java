package com.github.drtrang.typehandlers.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

/**
 * @author trang
 */
public final class EncryptUtil {

    private static final String PRIVATE_KEY_NAME = "encrypt.private.key";
    private static final String PRIVATE_KEY = PropertyUtil.get(PRIVATE_KEY_NAME);
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 重载类，使用默认key
     *
     * @param content 待加密内容，普通字符串
     * @return 加密后的内容，进行byte2hex后的内容
     */
    public static String encrypt(String content) {
        return encrypt(content, PRIVATE_KEY);
    }

    /**
     * 解密收到的十六进制字符串，重载类，使用默认key
     *
     * @param content：十六进制字符串，使用前需要先进行hex2byte操作
     * @return 解密后的明文字符串（UTF-8编码）
     */
    public static String decrypt(String content) {
        return decrypt(content, PRIVATE_KEY);
    }

    /**
     * @param content：待加密内容，普通字符串
     * @param password：十六进制key，使用前需要先进行hex2byte操作
     * @return 加密后的内容，进行byte2hex后的内容
     */
    private static String encrypt(String content, String password) {
        return Byte2Hex(encrypt(content.getBytes(DEFAULT_CHARSET), password.getBytes(DEFAULT_CHARSET)));
    }

    /**
     * 解密收到的十六进制字符串
     *
     * @param content：十六进制字符串，使用前需要先进行hex2byte操作
     * @param password：十六进制key，使用前需要先进行hex2byte操作
     * @return 解密后的明文字符串（UTF-8编码）
     */
    private static String decrypt(String content, String password) {
        byte[] buffer = decrypt(Hex2Byte(content), password.getBytes(DEFAULT_CHARSET));
        return new String(buffer, DEFAULT_CHARSET);
    }

    /**
     * 加密
     *
     * @param content  需要加密的内容
     * @param password 加密密钥
     */
    private static byte[] encrypt(byte[] content, byte[] password) {
        return aesCrypt(Cipher.ENCRYPT_MODE, content, password);
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     */
    private static byte[] decrypt(byte[] content, byte[] password) {
        return aesCrypt(Cipher.DECRYPT_MODE, content, password);
    }

    /**
     * AES加密/解密
     *
     * @param content  需要 加密/解密 的内容
     * @param password 加密/解密 密钥
     */
    private static byte[] aesCrypt(int mode, byte[] content, byte[] password) {
        try {
            SecureRandom sRandom = SecureRandom.getInstance("SHA1PRNG");
            sRandom.setSeed(password);
            byte[] randomBytes = password.clone();
            sRandom.nextBytes(randomBytes);
            SecretKeySpec key = new SecretKeySpec(randomBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(mode, key);

            return cipher.doFinal(content);// 加密/解密
        } catch (GeneralSecurityException e) {
            throw new SecurityException(e);
        }
    }

    /**
     * 将二进制转换成16进制
     */
    public static String Byte2Hex(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     */
    public static byte[] Hex2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static void printDecodeString(String decode) {
        System.out.println(EncryptUtil.decrypt(decode));
    }

    public static void printEncodeString(String encode) {
        System.out.println(EncryptUtil.encrypt(encode));
    }
}
