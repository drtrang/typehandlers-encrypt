package com.github.drtrang.typehandlers.crypt;

/**
 * 加密接口，自定义算法必须实现此接口
 *
 * @author trang
 */
public interface Crypt {

    String encrypt(String content);

    String encrypt(String content, String password);

    String decrypt(String content);

    String decrypt(String content, String password);

}