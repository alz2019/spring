package com.alz2019;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SpringTest {
    @Test
    void constructorWontCreateContext() {
        SpringApplication springApplication = new SpringApplication("test");
        assertNull(springApplication.getApplicationContext());
    }

    @Test
    void runReturnsCreatedContext() {
        SpringApplication springApplication = new SpringApplication("test");
        assertNotNull(springApplication.run());
    }
}
