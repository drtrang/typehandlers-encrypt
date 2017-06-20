package com.github.trang.typehandlers.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;

/**
 * 反射工具类
 *
 * @author trang
 */
class ReflectionUtil {

    private static final Logger log = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 使用反射新建一个对象，尽全力去新建，如果没有默认构造方法也支持
     *
     * @param clazz 类型
     * @param <T>   T
     * @return 对象
     */
    static <T> T newInstance(final Class<T> clazz) {
        Constructor<?>[] constructors = getAllConstructorsOfClass(clazz, true);
        if (constructors == null || constructors.length == 0) {
            return null;
        }

        Object[] initParameters = getInitParameters(constructors[0].getParameterTypes());

        try {
            @SuppressWarnings("unchecked")
            T instance = (T) constructors[0].newInstance(initParameters);
            return instance;
        } catch (Exception e) {
            log.error("newInstance", e);
            return null;
        }
    }

    /**
     * 获取某个类型的所有构造方法
     *
     * @param clazz      类型
     * @param accessible 是否可以访问
     * @return 构造方法数组
     */
    private static Constructor<?>[] getAllConstructorsOfClass(final Class<?> clazz, boolean accessible) {
        if (clazz == null) {
            return null;
        }

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors != null && constructors.length > 0) {
            AccessibleObject.setAccessible(constructors, accessible);
        }

        return constructors;
    }

    /**
     * 获取默认参数
     *
     * @param parameterTypes 参数类型数组
     * @return 参数值数组
     */
    private static Object[] getInitParameters(Class<?>[] parameterTypes) {
        int length = parameterTypes.length;

        Object[] result = new Object[length];
        for (int i = 0; i < length; i++) {
            if (parameterTypes[i].isPrimitive()) {
                Object init = getPrimitiveDefaultValue(parameterTypes[i]);
                result[i] = init;
                continue;
            }
            result[i] = null;
        }

        return result;
    }

    private static Object getPrimitiveDefaultValue(Class<?> primitiveType) {
        if (boolean.class.equals(primitiveType)) {
            return false;
        } else if (byte.class.equals(primitiveType)) {
            return (byte) 0;
        } else if (char.class.equals(primitiveType)) {
            return '\0';
        } else if (short.class.equals(primitiveType)) {
            return (short) 0;
        } else if (int.class.equals(primitiveType)) {
            return 0;
        } else if (long.class.equals(primitiveType)) {
            return 0L;
        } else if (float.class.equals(primitiveType)) {
            return 0F;
        } else if (double.class.equals(primitiveType)) {
            return 0D;
        } else {
            return null;
        }
    }
}
