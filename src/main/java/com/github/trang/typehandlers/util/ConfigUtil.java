package com.github.trang.typehandlers.util;

import com.github.trang.typehandlers.crypt.Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.github.trang.typehandlers.util.Constants.*;

/**
 * 配置文件工具类
 *
 * 说明：
 *   1. 默认加载以下文件：encrypt.properties、properties/config-common.properties、properties/config.properties、
 *   config.properties、application.properties，当文件存在且包含 encrypt.private.key 属性时认为是配置文件，会以该
 *   配置文件中的值为准，如果不存在以上文件则使用默认值，用户也可以通过 ConfigUtil.bundleNames() 方法来指定配置文件
 *   的名称，这种情况以指定的为准，若文件不存在则使用默认值
 *   2. 自定义私钥：在以上文件中新增属性 encrypt.private.key=私钥
 *      自定义算法：在以上文件中新增属性 encrypt.class.name=类的全路径
 *
 * @author trang
 */
public final class ConfigUtil {

    private static final Logger log = LoggerFactory.getLogger(ConfigUtil.class);

    private ConfigUtil() {
        throw new UnsupportedOperationException();
    }

    private static final List<String> INTERNAL_BUNDLE_NAMES = new ArrayList<>();
    private static final List<String> EXTRA_BUNDLE_NAMES = new ArrayList<>();
    private static ResourceBundle DEFAULT_BUNDLE = ResourceBundle.getBundle(DEFAULT_BUNDLE_NAME);
    private static ResourceBundle BUNDLE;
    private static Crypt CRYPT;

    static {
        init();
    }

    private static String findProperty(String name) {
        try {
            return BUNDLE.getString(name);
        } catch (MissingResourceException e) {
            return DEFAULT_BUNDLE.getString(name);
        }
    }

    public static String getPrivateKey() {
        return findProperty(PRIVATE_KEY_NAME);
    }

    public static Crypt getCrypt() {
        return ConfigUtil.CRYPT;
    }

    private static void init() {
        initBundleNames();
        initBundle();
        initCrypt();
    }

    private static void initBundleNames() {
        log.debug("init default bundle name");
        INTERNAL_BUNDLE_NAMES.add("encrypt");
        INTERNAL_BUNDLE_NAMES.add("properties/config-common");
        INTERNAL_BUNDLE_NAMES.add("properties/config");
        INTERNAL_BUNDLE_NAMES.add("config");
        INTERNAL_BUNDLE_NAMES.add("application");
        log.debug("init default bundle name completed: {}", INTERNAL_BUNDLE_NAMES);
    }

    private static void initBundle() {
        log.debug("init default bundle");
        ConfigUtil.BUNDLE = findBundle(0, INTERNAL_BUNDLE_NAMES);
        log.debug("init default bundle completed: {}", ConfigUtil.BUNDLE);
    }

    private static void initCrypt() {
        log.debug("init default crypt");
        ConfigUtil.CRYPT = findCrypt(findProperty(CRYPT_CLASS_NAME));
        log.debug("init default crypt completed: {}", ConfigUtil.CRYPT.getClass().getName());
    }

    public static void bundleNames(String... names) {
        if (names != null && names.length > 0) {
            Collections.addAll(EXTRA_BUNDLE_NAMES, names);
            refreshBundle();
            refreshCrypt();
        }
    }

    private static void refreshBundle() {
        log.debug("refresh bundle");
        ConfigUtil.BUNDLE = findBundle(0, EXTRA_BUNDLE_NAMES);
        log.debug("refresh bundle completed: {}", ConfigUtil.BUNDLE);
    }

    private static void refreshCrypt() {
        log.debug("refresh crypt");
        String className = findProperty(CRYPT_CLASS_NAME);
        if (!className.equalsIgnoreCase(CRYPT.getClass().getName())) {
            ConfigUtil.CRYPT = findCrypt(className);
        }
        log.debug("refresh crypt completed: {}", ConfigUtil.CRYPT.getClass().getName());
    }

    private static ResourceBundle findBundle(int index, List<String> names) {
        int size = names.size();
        if (index >= size) {
            return DEFAULT_BUNDLE;
        }
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(names.get(index));
            if (bundle.containsKey(PRIVATE_KEY_NAME)) {
                return bundle;
            }
            return findBundle(++index, names);
        } catch (MissingResourceException e) {
            return findBundle(++index, names);
        }
    }

    @SuppressWarnings("unchecked")
    private static Crypt findCrypt(String className) {
        try {
            if (StringUtil.isBlank(className)) {
                throw new IllegalArgumentException("Property '" + CRYPT_CLASS_NAME + "' can't be null");
            }
            Class<?> cryptClass = Class.forName(className);
            if (!Crypt.class.isAssignableFrom(cryptClass)) {
                throw new IllegalArgumentException("Class '" + cryptClass.getSimpleName() + "' must implements 'Crypt'");
            }
            Class<Crypt> clazz = (Class<Crypt>) cryptClass;
            if (clazz.isEnum()) {
                Crypt[] constants = clazz.getEnumConstants();
                if (constants == null || constants.length == 0) {
                    throw new IllegalArgumentException("Class '" + clazz.getSimpleName() + "' doesn't have any constants");
                }
                return constants[0];
            } else {
                return ReflectionUtil.newInstance(clazz);
            }
        } catch (Exception e) {
            log.error("init default crypt failed", e);
            throw new IllegalArgumentException(e);
        }
    }
}