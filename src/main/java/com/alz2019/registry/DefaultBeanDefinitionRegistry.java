package com.alz2019.registry;

import com.alz2019.exception.DuplicateNameException;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultBeanDefinitionRegistry implements BeanDefinitionRegistry {
    private final Map<String, BeanDefinition> registry = new ConcurrentHashMap<>();
    private final Map<String, ConfigurationBeanDefinition> configurationRegistry = new ConcurrentHashMap<>();

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        checkNameUniqueness(name);
        registry.put(name, beanDefinition);
    }

    private void checkNameUniqueness(String name) {
        if (registry.containsKey(name) || configurationRegistry.containsKey(name)) {
            throw new DuplicateNameException(name);
        }
    }

    @Override
    public void registerConfigBeanDefinition(String name, ConfigurationBeanDefinition configurationBeanDefinition) {
        checkNameUniqueness(name);
        configurationRegistry.put(name, configurationBeanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return registry.get(name);
    }

    @Override
    public ConfigurationBeanDefinition getConfigurationBeanDefinition(String name) {
        return configurationRegistry.get(name);
    }

    @Override
    public Map<String, BeanDefinition> getAllBeanDefinitions() {
        return Collections.unmodifiableMap(registry);
    }

    @Override
    public Map<String, ConfigurationBeanDefinition> getAllConfigurationBeanDefinitions() {
        return Collections.unmodifiableMap(configurationRegistry);
    }

    @Override
    public boolean contains(String beanName) {
        return registry.containsKey(beanName);
    }
}
