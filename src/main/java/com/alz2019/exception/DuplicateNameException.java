package com.alz2019.exception;

public class DuplicateNameException extends RuntimeException {
    public DuplicateNameException(String beanName) {
        super("BeanDefinition with name %s already exists".formatted(beanName));
    }
}
