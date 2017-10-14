package com.github.trang.typehandlers.crypt;

import com.github.trang.typehandlers.util.ConfigUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

/**
 * 默认的 AES 加密算法
 *
 * @author trang
 */
public enum SimpleCrypt implements Crypt {
    INSTANCE;

    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 加密字符串，使用默认key
     *
     * @param content 待加密内容，普通字符串
     * @return 加密后的 16 进制字符串
     */
    public String encrypt(String content) {
        return encrypt(content, ConfigUtil.getPrivateKey());
    }

    /**
     * 解密 16 进制字符串，使用默认key
     *
     * @param content 加密后的 16 进制字符串
     * @return 解密后的明文字符串（UTF-8编码）
     */
    public String decrypt(String content) {
        return decrypt(content, ConfigUtil.getPrivateKey());
    }

    /**
     * 加密字符串，使用自定义key
     *
     * @param content 待加密内容，普通字符串
     * @param password 密钥
     * @return 加密后的 16 进制字符串
     */
    public String encrypt(String content, String password) {
        return byte2Hex(encrypt(content.getBytes(DEFAULT_CHARSET), password.getBytes(DEFAULT_CHARSET)));
    }

    /**
     * 解密 16 进制字符串，使用自定义key
     *
     * @param content 加密后的 16 进制字符串
     * @param password 密钥
     * @return 解密后的明文字符串（UTF-8编码）
     */
    public String decrypt(String content, String password) {
        byte[] buffer = decrypt(hex2Byte(content), password.getBytes(DEFAULT_CHARSET));
        return new String(buffer, DEFAULT_CHARSET);
    }

    /**
     * 加密
     *
     * @param content  待加密的内容
     * @param password 密钥
     */
    public byte[] encrypt(byte[] content, byte[] password) {
        return aesCrypt(Cipher.ENCRYPT_MODE, content, password);
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 密钥
     */
    public byte[] decrypt(byte[] content, byte[] password) {
        return aesCrypt(Cipher.DECRYPT_MODE, content, password);
    }

    /**
     * AES 加密/解密
     *
     * @param content  需要 加密/解密 的内容
     * @param password 加密/解密 密钥
     */
    private byte[] aesCrypt(int mode, byte[] content, byte[] password) {
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
     * 将 2 进制转换成 16 进制字符串
     */
    private String byte2Hex(byte buf[]) {
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
     * 将 16 进制字符串转换为 2 进制
     */
    private byte[] hex2Byte(String hexStr) {
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

}
