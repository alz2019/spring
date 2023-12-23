package com.alz2019.exception;

public class NoSuchBeanException extends RuntimeException {
    public NoSuchBeanException(Class<?> candidateType) {
        super("Bean with type %s doesn't exist".formatted(candidateType.getSimpleName()));
    }

    public NoSuchBeanException(String name, Class<?> candidateType) {
        super("Bean with name %s and type %s doesn't exist".formatted(name, candidateType.getSimpleName()));
    }
}
