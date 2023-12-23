package com.alz2019.exception;

public class NoUniqueBeanDefinitionException extends RuntimeException {
    public NoUniqueBeanDefinitionException(Class<?> candidateType) {
        super("There are multiple beans with type %s".formatted(candidateType.getSimpleName()));
    }
}
