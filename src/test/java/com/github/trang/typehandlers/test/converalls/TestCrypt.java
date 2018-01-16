package com.github.trang.typehandlers.test.converalls;

import com.github.trang.typehandlers.crypt.Crypt;

/**
 * @author trang
 */
public class TestCrypt implements Crypt {

    @Override
    public String encrypt(String content) {
        return "1";
    }

    @Override
    public String decrypt(String content) {
        return "3";
    }

}