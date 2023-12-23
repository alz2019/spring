package com.alz2019.registry;

import java.util.Map;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String name, BeanDefinition beanDefinition);

    void registerConfigBeanDefinition(String name, ConfigurationBeanDefinition configurationBeanDefinition);

    BeanDefinition getBeanDefinition(String name);

    ConfigurationBeanDefinition getConfigurationBeanDefinition(String name);

    Map<String, BeanDefinition> getAllBeanDefinitions();

    Map<String, ConfigurationBeanDefinition> getAllConfigurationBeanDefinitions();

    boolean contains(String beanName);
}
