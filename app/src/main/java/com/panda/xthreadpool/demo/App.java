package com.panda.xthreadpool.demo;

import android.app.Application;

import com.panda.xthreadpool.GlobalThreadInterceptor;
import com.panda.xthreadpool.ThreadPoolExecutors;

/**
 * @author panda
 * created at 2021/6/30 4:05 PM
 */
public class App extends Application {
	private static ThreadPoolExecutors executors;

	@Override
	public void onCreate() {
		super.onCreate();
		executors = ThreadPoolExecutors
				.PoolBuilder
				.createFixed(5)
				.setInterceptor(new GlobalThreadInterceptor() {
					@Override
					public void onStart(String threadTag, Thread thread) {

					}

					@Override
					public void onEnd(String threadTag, Thread thread) {

					}
				})
				.build();
	}

	public static ThreadPoolExecutors getExecutors() {
		return executors;
	}
}
