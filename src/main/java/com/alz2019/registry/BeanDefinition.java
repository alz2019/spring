package com.alz2019.registry;

import lombok.Builder;

import java.lang.reflect.Field;
import java.util.Map;

@Builder
public class BeanDefinition {
    private Class<?> aClass;
    private Map<String, Field> fieldMetadata;

    public Class<?> getBeanClass() {
        return aClass;
    }

    public Map<String, Field> getFieldMetadata() {
        return fieldMetadata;
    }
}
