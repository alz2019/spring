package com.alz2019.resolver;

import com.alz2019.annotation.Qualifier;
import com.alz2019.exception.NoSuchBeanException;
import com.alz2019.exception.NoUniqueBeanDefinitionException;
import com.alz2019.registry.BeanDefinition;
import com.alz2019.registry.BeanDefinitionRegistry;
import com.alz2019.registry.ConfigurationBeanDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DefaultDependencyResolver implements DependencyResolver {
    private final Map<String, BeanDefinition> beanDefinitionMap;
    private final Map<String, ConfigurationBeanDefinition> configurationBeanDefinitionMap;

    public DefaultDependencyResolver(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionMap = beanDefinitionRegistry.getAllBeanDefinitions();
        this.configurationBeanDefinitionMap = beanDefinitionRegistry.getAllConfigurationBeanDefinitions();
    }

    @Override
    public String getCandidateNameOfType(Class<?> candidateType, Annotation... metadata) {
        return getQualifierValue(metadata)
                .map(name -> resolveNameByType(name, candidateType))
                .orElseGet(() -> findCandidateByType(candidateType));
    }

    private Optional<String> getQualifierValue(Annotation... metadata) {
        return Arrays.stream(metadata)
                .filter(Qualifier.class::isInstance)
                .findFirst()
                .map(Qualifier.class::cast)
                .map(Qualifier::value);
    }

    private String resolveNameByType(String name, Class<?> candidateType) {
        Boolean isBean = Optional.ofNullable(beanDefinitionMap.get(name))
                .map(BeanDefinition::getBeanClass)
                .map(candidateType::isAssignableFrom)
                .orElse(false);
        Boolean isConfigurationBean = Optional.ofNullable(configurationBeanDefinitionMap.get(name))
                .map(ConfigurationBeanDefinition::method)
                .map(Method::getReturnType)
                .map(candidateType::isAssignableFrom)
                .orElse(false);
        if (isBean || isConfigurationBean) {
            return name;
        } else {
            throw new NoSuchBeanException(candidateType);
        }
    }

    private String findCandidateByType(Class<?> candidateType) {
        Predicate<Map.Entry<String, BeanDefinition>> beanPredicate =
                mapEntry -> candidateType.isAssignableFrom(mapEntry.getValue().getBeanClass());
        Predicate<Map.Entry<String, ConfigurationBeanDefinition>> configurationBeanPredicate =
                mapEntry -> candidateType.isAssignableFrom(mapEntry.getValue().method().getReturnType());
        List<String> names = Stream.concat(beanDefinitionMap.entrySet().stream().filter(beanPredicate),
                        configurationBeanDefinitionMap.entrySet().stream().filter(configurationBeanPredicate))
                .map(Map.Entry::getKey)
                .toList();

        if (names.isEmpty()) {
            throw new NoSuchBeanException(candidateType);
        }
        if (names.size() > 1) {
            throw new NoUniqueBeanDefinitionException(candidateType);
        }
        return names.get(0);
    }
}
