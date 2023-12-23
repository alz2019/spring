package com.alz2019.scanner;

import com.alz2019.annotation.Autowired;
import com.alz2019.annotation.Component;
import com.alz2019.registry.BeanDefinition;
import com.alz2019.registry.BeanDefinitionRegistry;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static java.util.Objects.nonNull;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

public class DefaultBeanDefinitionScanner implements BeanDefinitionScanner {
    private final BeanDefinitionRegistry beanDefinitionRegistry;

    public DefaultBeanDefinitionScanner(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public void registerBeans(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Component.class);
        typesAnnotatedWith.forEach(this::registerBean);
    }

    private void registerBean(Class<?> aClass) {
        String name = aClass.getAnnotation(Component.class).value();
        String uncapitalizedName = isBlank(name) ? uncapitalize(aClass.getSimpleName()) : name;
        BeanDefinition beanDefinition = createBeanDefinition(aClass);
        beanDefinitionRegistry.registerBeanDefinition(uncapitalizedName, beanDefinition);
    }

    private BeanDefinition createBeanDefinition(Class<?> aClass) {
        Field[] fields = aClass.getDeclaredFields();
        Map<String, Field> stringFieldMap = Arrays.stream(fields)
                .filter(field -> nonNull(field.getAnnotation(Autowired.class)))
                .collect(toMap(Field::getName, identity()));
        return BeanDefinition
                .builder()
                .aClass(aClass)
                .fieldMetadata(stringFieldMap)
                .build();
    }
}
