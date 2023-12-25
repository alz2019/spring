package com.alz2019;

import com.alz2019.context.AnnotationConfigApplicationContext;
import com.alz2019.context.ApplicationContext;

import static com.alz2019.utils.BeanUtils.validatePackage;

public class SpringApplication {
    private final String packageName;
    private ApplicationContext applicationContext;

    public SpringApplication(String packageName) {
        validatePackage(packageName);
        this.packageName = packageName;
    }

    public ApplicationContext run() {
        this.applicationContext = new AnnotationConfigApplicationContext(packageName);
        return applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
