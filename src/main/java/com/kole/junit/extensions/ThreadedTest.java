package com.kole.junit.extensions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation to specify which method should be created in multithreaded mode. 
 * 
 * The number of threads variable by default is set to 1, but can be set to anything to 
 * indicate the number of parallel tests the framework should create. 
 * 
 * @author Edward Lee
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ThreadedTest {
	
	long numberOfThreads() default 1;
}
