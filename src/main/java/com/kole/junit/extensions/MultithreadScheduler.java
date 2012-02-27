package com.kole.junit.extensions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.runners.model.RunnerScheduler;

/**
 * This is a multithread scheduler that is setup to take all the test cases and 
 * start all of them at the same time (almost the same) at a later stage
 * 
 * @author Edward Lee
 *
 */
public class MultithreadScheduler implements RunnerScheduler {
	List<Future<?>> results = new ArrayList<Future<?>>();
	List<Runnable> tests = new ArrayList<Runnable>();
	ExecutorService pool = Executors.newCachedThreadPool();

	@Override
	public void finished() {
		for (Future<?> result:results) {
			try {
				result.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} 
		}
	}

	@Override
	public void schedule(Runnable arg0) {
		tests.add(arg0);
	}
	
	public void start() {
		for (Runnable test : tests) {
			Future<?> result = pool.submit(test) ;
			results.add(result);
		}
	}

}
