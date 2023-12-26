package com.alz2019.factory;

import com.alz2019.registry.BeanDefinition;
import com.alz2019.registry.BeanDefinitionRegistry;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import static com.alz2019.utils.BeanUtils.createInstance;

@RequiredArgsConstructor
public class RawBeanProcessor implements BeanProcessor {
    private final Map<String, Object> beansMap;
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    @Override
    public void process() {
        Map<String, BeanDefinition> allBeanDefinitions = this.beanDefinitionRegistry.getAllBeanDefinitions();
        allBeanDefinitions.forEach(this::initializeBean);
    }

    private void initializeBean(String name, BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Object instance = createInstance(beanClass);
        beansMap.put(name, instance);
    }
}
