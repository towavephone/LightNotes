package com.example.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	private ExecutorService executorService;
	private static ThreadPool tp = null;

	private ThreadPool() {
		executorService = Executors.newFixedThreadPool(5);
	}

	public static ThreadPool getInstance() {
		if (tp == null)
			tp = new ThreadPool();
		return tp;
	}

	public void AddThread(Thread t) {
		executorService.execute(t);
	}

	public void AddThread(Runnable r) {
		executorService.execute(r);
	}

	public void shutsown() {
		executorService.shutdown();
		tp = null;
	}
}
