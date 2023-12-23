package com.alz2019.resolver;

import java.lang.annotation.Annotation;

public interface DependencyResolver {
    String getCandidateNameOfType(Class<?> candidateType, Annotation... metadata);
}
