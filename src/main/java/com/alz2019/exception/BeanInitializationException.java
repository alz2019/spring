package com.alz2019.exception;

public class BeanInitializationException extends RuntimeException {
    public <T> BeanInitializationException(Class<T> beanType, Throwable e) {
        super("Wasn't able to initialize bean with type %s. %s".formatted(beanType.getSimpleName(), e.getLocalizedMessage()));
    }
}
