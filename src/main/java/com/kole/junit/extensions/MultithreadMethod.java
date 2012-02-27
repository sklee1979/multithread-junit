package com.kole.junit.extensions;

import java.lang.reflect.Method;

import org.junit.runners.model.FrameworkMethod;

/**
 * Extending the FrameworkMethod to set the number of threads required to spin up
 * 
 * @author Edward Lee
 *
 */
public class MultithreadMethod extends FrameworkMethod {
	private long numberOfThreads = 1;	

	public MultithreadMethod(Method method) {
		super(method);
	}
	
	public MultithreadMethod(Method method, long threads) {
		super(method);
		this.numberOfThreads = threads;
	}
	
	public long getNumberOfThreads() {
		return numberOfThreads;
	}
	
	public void setNumberOfThreads(long threads) {
		this.numberOfThreads = threads;
	}
}
