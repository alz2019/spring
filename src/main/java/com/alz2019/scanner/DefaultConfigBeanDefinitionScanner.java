package com.alz2019.scanner;

import com.alz2019.annotation.Bean;
import com.alz2019.annotation.Configuration;
import com.alz2019.registry.BeanDefinitionRegistry;
import com.alz2019.registry.ConfigurationBeanDefinition;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.Set;

public class DefaultConfigBeanDefinitionScanner implements ConfigBeanDefinitionScanner {
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public DefaultConfigBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public void registerBeans(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Configuration.class);
        typesAnnotatedWith.forEach(this::registerBean);
    }

    private void registerBean(Class<?> aClass) {
        Arrays.stream(aClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .map(ConfigurationBeanDefinition::new)
                .forEach(configurationBeanDefinition -> beanDefinitionRegistry.registerConfigBeanDefinition(configurationBeanDefinition.method().getName(), configurationBeanDefinition));
    }
}
