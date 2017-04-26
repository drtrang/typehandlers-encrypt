package com.github.drtrang.typehandlers.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author trang
 */
public final class PropertyUtil {

    private static final String DEFAULT_BUNDLE_NAMES = "default";
    private static final List<String> EXTRA_BUNDLE_NAMES = new ArrayList<>();
    private static ResourceBundle BUNDLE;

    static {
        init();
    }

    private PropertyUtil() {
        throw new UnsupportedOperationException();
    }

    public static String get(String name) {
        return BUNDLE.getString(name);
    }

    private static void init() {
        initPropertiesNames();
        initResourceBundle();
    }

    private static void initPropertiesNames() {
        EXTRA_BUNDLE_NAMES.add("encrypt");
        EXTRA_BUNDLE_NAMES.add("properties/config-common");
        EXTRA_BUNDLE_NAMES.add("config");
    }

    private static void initResourceBundle() {
        BUNDLE = find(0);
    }

    private static ResourceBundle find(int index) {
        int size = EXTRA_BUNDLE_NAMES.size();
        if (index >= size) {
            return ResourceBundle.getBundle(DEFAULT_BUNDLE_NAMES);
        }
        try {
            return ResourceBundle.getBundle(EXTRA_BUNDLE_NAMES.get(index));
        } catch (Exception e) {
            return find(++index);
        }
    }

    public static void main(String[] args) {
        System.out.println(find(0));
    }
}
