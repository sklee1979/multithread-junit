package com.kole.junit.extensions;

import java.util.ArrayList;
import java.util.List;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * Extending the BlockJUnit4ClassRunner to reuse all the methods of looking for children 
 * test cases. 
 * 
 * @author Edward Lee
 *
 */
public class MultithreadedRunner extends BlockJUnit4ClassRunner {
	MultithreadScheduler mscheduler = new MultithreadScheduler();

	public MultithreadedRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	/**
	 * Overwriting the childrenInvoker method on the BlockJUnit4ClassRunner.
	 * This will allow us to also invoke the multithreaded children test cases 
	 */
	@Override
	protected Statement childrenInvoker(final RunNotifier notifier) {
		return new Statement() {
			@Override
			public void evaluate() {
				runMultithreadedChildren(notifier);
				runChildren(notifier);
			}
		};
	}

	/**
	 * This is method to create the multiple parallel run test cases.
	 * Based on the annotation @ThreadedTest where on the annotation you can 
	 * Specify the number of threads to kick start, it uses the MultithreadScheduler
	 * to manage the schedule the Future tasks 
	 * 
	 * @param notifier Notify the progress and result to the parent class
	 */
	private void runMultithreadedChildren(final RunNotifier notifier) {
		for (final MultithreadMethod each : getMultiThreadedChildren()) {
			for (int i = 0; i < each.getNumberOfThreads(); i++) {
				mscheduler.schedule(new Runnable() {
					public void run() {
						runChild(each, notifier);
					}
				});
			}
		}
		
		// start the scheduler, which means running all the test cases at the same time
		mscheduler.start();
		mscheduler.finished();
	}

	
	/*
	 * This is a copy of the runChildren function, because I am unable to overwrite 
	 * this method
	 */
	private void runChildren(final RunNotifier notifier) {
		for (final FrameworkMethod each : getChildren()) {
			runChild(each, notifier);
		}
	}

	/**
	 * Get all the methods annotated with @ThreadedTest 
	 * 
	 * @return Methods with multithread configuration
	 */
	private List<MultithreadMethod> getMultiThreadedChildren() {
		List<MultithreadMethod> methods = new ArrayList<MultithreadMethod>();
		for (FrameworkMethod method : getTestClass().getAnnotatedMethods(
				ThreadedTest.class)) {
			ThreadedTest annotation = method.getAnnotation(ThreadedTest.class);
			methods.add(new MultithreadMethod(method.getMethod(), annotation
					.numberOfThreads()));
		}

		return methods;
	}
}
