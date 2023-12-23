package com.alz2019.utils;

import com.alz2019.exception.BeanInitializationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public final class BeanUtils {
    public static <T> T createInstance(Class<T> beanType) {
        try {
            Constructor<T> constructor = beanType.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Throwable e) {
            throw new BeanInitializationException(beanType, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Method method, Object... parameters) {
        try {
            method.setAccessible(true);
            Class<?> configurationClass = method.getDeclaringClass();
            Object instance = createInstance(configurationClass);
            return (T) method.invoke(instance, parameters);
        } catch (Throwable e) {
            throw new BeanInitializationException(method.getReturnType(), e);
        }
    }

    public static void validatePackage(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Incorrect package name");
        }
    }
}
