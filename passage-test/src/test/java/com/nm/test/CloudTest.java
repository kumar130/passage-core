package com.nm.test;

import org.springframework.test.annotation.IfProfileValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@IfProfileValue(name="execute.cloud.tests", values = {"true", "TRUE", "yes", "YES", "1"})
public @interface CloudTest {
}
