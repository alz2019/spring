package com.alz2019.factory;

import com.alz2019.registry.BeanDefinitionRegistry;
import com.alz2019.resolver.DefaultDependencyResolver;
import com.alz2019.resolver.DependencyResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactoryImpl implements BeanFactory {
    private final Map<String, Object> beansMap = new ConcurrentHashMap<>();
    private final List<BeanProcessor> beanProcessorList = new ArrayList<>();
    private final List<BeanPostProcessor> beanPostProcessorList = new ArrayList<>();

    public BeanFactoryImpl(BeanDefinitionRegistry beanDefinitionRegistry) {
        DependencyResolver dependencyResolver = new DefaultDependencyResolver(beanDefinitionRegistry);
        this.beanProcessorList.add(new ConfigurationBeanProcessor(beanDefinitionRegistry, beansMap, dependencyResolver));
        this.beanProcessorList.add(new RawBeanProcessor(beansMap, beanDefinitionRegistry));
        this.beanPostProcessorList.add(new AutowiredBeanPostProcessor(beanDefinitionRegistry, beansMap, dependencyResolver));
    }

    public Map<String, Object> createBeans() {
        beanProcessorList.forEach(BeanProcessor::process);
        beanPostProcessorList.forEach(BeanPostProcessor::process);
        return beansMap;
    }
}
