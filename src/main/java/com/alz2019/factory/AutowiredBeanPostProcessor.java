package com.alz2019.factory;

import com.alz2019.registry.BeanDefinition;
import com.alz2019.registry.BeanDefinitionRegistry;
import com.alz2019.resolver.DependencyResolver;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

@RequiredArgsConstructor
public class AutowiredBeanPostProcessor implements BeanPostProcessor {
    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private final Map<String, Object> beansMap;
    private final DependencyResolver dependencyResolver;

    @Override
    public void process() {
        beansMap.forEach(this::processBean);
    }

    private void processBean(String name, Object bean) {
        if (beanDefinitionRegistry.contains(name)) {
            BeanDefinition beanDefinition = beanDefinitionRegistry.getBeanDefinition(name);
            beanDefinition.getFieldMetadata().values().forEach(field -> fillField(field, bean));
        }
    }

    private void fillField(Field field, Object bean) {
        Class<?> type = field.getType();
        Annotation[] annotations = field.getAnnotations();
        Object object = beansMap.get(dependencyResolver.getCandidateNameOfType(type, annotations));
        try {
            field.setAccessible(true);
            field.set(bean, object);
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("Impossible to inject field %s. %s".formatted(field.getName(), e.getLocalizedMessage()));
        }
    }
}
