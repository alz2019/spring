package com.alz2019.factory;

import com.alz2019.registry.BeanDefinitionRegistry;
import com.alz2019.registry.ConfigurationBeanDefinition;
import com.alz2019.resolver.DependencyResolver;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

import static com.alz2019.utils.BeanUtils.createInstance;

public class ConfigurationBeanProcessor implements BeanProcessor {
    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private final Map<String, Object> beanMap;
    private final DependencyResolver dependencyResolver;

    public ConfigurationBeanProcessor(BeanDefinitionRegistry beanDefinitionRegistry, Map<String, Object> beanMap, DependencyResolver dependencyResolver) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.beanMap = beanMap;
        this.dependencyResolver = dependencyResolver;
    }

    @Override
    public void process() {
        Map<String, ConfigurationBeanDefinition> configurationBeanDefinitionMap = beanDefinitionRegistry.getAllConfigurationBeanDefinitions();
        configurationBeanDefinitionMap.forEach(this::initializeBean);
    }

    private Object initializeBean(String name, ConfigurationBeanDefinition configurationBeanDefinition) {
        Method method = configurationBeanDefinition.method();
        Parameter[] parameters = method.getParameters();
        Object[] objects = Arrays.stream(parameters)
                .map(parameter -> dependencyResolver.getCandidateNameOfType(parameter.getType(), parameter.getAnnotations()))
                .map(this::getOrInit)
                .toArray();
        Object instance = createInstance(method, objects);
        beanMap.put(name, instance);
        return instance;
    }

    private Object getOrInit(String name) {
        if (beanMap.containsKey(name)) {
            return beanMap.get(name);
        } else {
            return initializeBean(name, beanDefinitionRegistry.getConfigurationBeanDefinition(name));
        }
    }
}
