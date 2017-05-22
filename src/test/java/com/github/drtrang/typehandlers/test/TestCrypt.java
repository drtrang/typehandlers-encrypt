package com.github.drtrang.typehandlers.test;

import com.github.drtrang.typehandlers.crypt.Crypt;

/**
 * @author trang
 */
public class TestCrypt implements Crypt {

    @Override
    public String encrypt(String content) {
        return "1";
    }

    @Override
    public String encrypt(String content, String password) {
        return "2";
    }

    @Override
    public String decrypt(String content) {
        return "3";
    }

    @Override
    public String decrypt(String content, String password) {
        return "4";
    }

}
