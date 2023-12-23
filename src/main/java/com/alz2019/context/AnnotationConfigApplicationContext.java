package com.alz2019.context;

import com.alz2019.exception.NoSuchBeanException;
import com.alz2019.exception.NoUniqueBeanDefinitionException;
import com.alz2019.factory.BeanFactory;
import com.alz2019.factory.BeanFactoryImpl;
import com.alz2019.registry.BeanDefinitionRegistry;
import com.alz2019.registry.DefaultBeanDefinitionRegistry;
import com.alz2019.scanner.DefaultBeanDefinitionScanner;
import com.alz2019.scanner.DefaultConfigBeanDefinitionScanner;

import java.util.Map;

import static com.alz2019.utils.BeanUtils.validatePackage;
import static java.util.stream.Collectors.toMap;

public class AnnotationConfigApplicationContext implements ApplicationContext {
    private final Map<String, Object> beans;

    public AnnotationConfigApplicationContext(String packageName) {
        validatePackage(packageName);
        BeanDefinitionRegistry beanDefinitionRegistry = new DefaultBeanDefinitionRegistry();
        new DefaultBeanDefinitionScanner(beanDefinitionRegistry).registerBeans(packageName);
        new DefaultConfigBeanDefinitionScanner(beanDefinitionRegistry).registerBeans(packageName);
        BeanFactory beanFactory = new BeanFactoryImpl(beanDefinitionRegistry);
        this.beans = beanFactory.createBeans();
    }

    public <T> T getBean(Class<T> beanType) {
        Map<String, T> allBeans = getAllBeans(beanType);
        if (allBeans.size() > 1) {
            throw new NoUniqueBeanDefinitionException(beanType);
        }
        return allBeans.values().stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchBeanException(beanType));
    }

    public <T> T getBean(String name, Class<T> beanType) {
        Map<String, T> allBeans = getAllBeans(beanType);
        T bean = allBeans.get(name);
        if (bean == null) {
            throw new NoSuchBeanException(name, beanType);
        }
        return bean;
    }

    public <T> Map<String, T> getAllBeans(Class<T> beanType) {
        return beans.entrySet()
                .stream()
                .filter(mapEntry -> beanType.isAssignableFrom(mapEntry.getValue().getClass()))
                .collect(toMap(Map.Entry::getKey, mapEntry -> beanType.cast(mapEntry.getValue())));
    }
}
