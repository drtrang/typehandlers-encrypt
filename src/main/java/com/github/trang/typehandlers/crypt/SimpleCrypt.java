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
     * 加密数据
     *
     * @param content 待加密数据
     * @return 加密后的 16 进制字符串
     */
    @Override
    public String encrypt(String content) {
        return encrypt(content, DEFAULT_CHARSET);
    }

    /**
     * 加密数据
     *
     * @param content 待加密数据
     * @param charset 字符集
     * @return 加密后的 16 进制字符串
     */
    public String encrypt(String content, Charset charset) {
        return encrypt(content, ConfigUtil.getPrivateKey(), charset);
    }

    /**
     * 加密数据
     *
     * @param content 待加密数据
     * @param key     密钥
     * @param charset 字符集
     * @return 加密后的 16 进制字符串
     */
    public String encrypt(String content, String key, Charset charset) {
        return byte2Hex(encrypt(content.getBytes(charset), key.getBytes(charset)));
    }

    /**
     * 执行加密的方法
     *
     * @param content 待加密数据
     * @param key     密钥
     */
    public byte[] encrypt(byte[] content, byte[] key) {
        return aesCrypt(Cipher.ENCRYPT_MODE, content, key);
    }

    /**
     * 解密 16 进制字符串
     *
     * @param content 加密后的 16 进制字符串
     * @return 解密后的明文字符串（UTF-8编码）
     */
    @Override
    public String decrypt(String content) {
        return decrypt(content, DEFAULT_CHARSET);
    }

    /**
     * 解密 16 进制字符串
     *
     * @param content 加密后的 16 进制字符串
     * @param charset 字符集
     * @return 解密后的明文字符串（UTF-8编码）
     */
    public String decrypt(String content, Charset charset) {
        return decrypt(content, ConfigUtil.getPrivateKey(), charset);
    }

    /**
     * 解密 16 进制字符串
     *
     * @param content 加密后的 16 进制字符串
     * @param key     密钥
     * @param charset 字符集
     * @return 解密后的明文字符串（UTF-8编码）
     */
    public String decrypt(String content, String key, Charset charset) {
        byte[] buffer = decrypt(hex2Byte(content), key.getBytes(charset));
        return new String(buffer, charset);
    }

    /**
     * 实际执行解密的方法
     *
     * @param content 待解密内容
     * @param key     密钥
     */
    public byte[] decrypt(byte[] content, byte[] key) {
        return aesCrypt(Cipher.DECRYPT_MODE, content, key);
    }

    /**
     * AES 加密/解密
     *
     * @param content 需要 加密/解密 的内容
     * @param key     加密/解密 密钥
     */
    private byte[] aesCrypt(int mode, byte[] content, byte[] key) {
        try {
            SecureRandom sRandom = SecureRandom.getInstance("SHA1PRNG");
            sRandom.setSeed(key);
            byte[] randomBytes = key.clone();
            sRandom.nextBytes(randomBytes);
            SecretKeySpec keySpec = new SecretKeySpec(randomBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, keySpec);

            return cipher.doFinal(content);
        } catch (GeneralSecurityException e) {
            throw new SecurityException(e);
        }
    }

    /**
     * 将 2 进制转换成 16 进制字符串
     */
    private String byte2Hex(byte[] buf) {
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
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

}
