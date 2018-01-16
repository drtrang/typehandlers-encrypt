package com.github.trang.typehandlers.util;

/**
 * 字符串工具类
 *
 * @author trang
 */
class StringUtil {

    static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    static boolean isNumeric(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
