package com.github.drtrang.typehandlers.util;

import java.util.*;

import static com.github.drtrang.typehandlers.util.Constants.DEFAULT_BUNDLE_NAME;
import static com.github.drtrang.typehandlers.util.Constants.PRIVATE_KEY_NAME;

/**
 * Properties 文件工具类
 *
 * @author trang
 */
public final class BundleUtil {

    private BundleUtil() {
        throw new UnsupportedOperationException();
    }

    private static final List<String> INTERNAL_BUNDLE_NAMES = new ArrayList<>();
    private static final List<String> EXTRA_BUNDLE_NAMES = new ArrayList<>();
    private static ResourceBundle BUNDLE;

    static {
        init();
    }

    static String get(String name) {
        return BUNDLE.getString(name);
    }

    private static void init() {
        initBundleNames();
        initBundle();
    }

    private static void initBundleNames() {
        INTERNAL_BUNDLE_NAMES.add("encrypt");
        INTERNAL_BUNDLE_NAMES.add("properties/config-common");
        INTERNAL_BUNDLE_NAMES.add("properties/config");
        INTERNAL_BUNDLE_NAMES.add("config");
        INTERNAL_BUNDLE_NAMES.add("application");
    }

    private static void initBundle() {
        BUNDLE = find(0, INTERNAL_BUNDLE_NAMES);
    }

    private static ResourceBundle find(int index, List<String> names) {
        int size = names.size();
        if (index >= size) return ResourceBundle.getBundle(DEFAULT_BUNDLE_NAME);
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(names.get(index));
            if (bundle.containsKey(PRIVATE_KEY_NAME)) {
                return bundle;
            }
            return find(++index, names);
        } catch (Exception e) {
            return find(++index, names);
        }
    }

    private static void refreshBundle() {
        BUNDLE = find(0, EXTRA_BUNDLE_NAMES);
    }

    public static void bundleNames(String... names) {
        if (names != null && names.length > 0) {
            Collections.addAll(EXTRA_BUNDLE_NAMES, names);
            refreshBundle();
        }
    }
}