package com.panda.xthreadpool;

import java.util.concurrent.Callable;

import androidx.annotation.NonNull;

/**
 * @author panda
 * created at 2021/3/9 5:16 PM
 */
class CallableWrapper<T> implements Callable<T> {
    private ThreadCallback threadCallback;
    private Callable<T> callable;
    private GlobalThreadInterceptor interceptor;
    private String threadTag;

    public CallableWrapper(Callable<T> callable, String threadTag,
                           @NonNull GlobalThreadInterceptor interceptor, ThreadCallback threadCallback) {
        this.threadCallback = threadCallback;
        this.callable = callable;
        this.interceptor = interceptor;
        this.threadTag = threadTag;
    }

    @Override
    public T call() throws Exception {
        if (callable == null) return null;
        interceptor.onStart(threadTag, Thread.currentThread());
        if (threadCallback != null) {
            threadCallback.onStart(Thread.currentThread());
        }

        T result = null;
        try {
            result = callable.call();
            if (threadCallback != null) {
                threadCallback.onCompleted(Thread.currentThread());
            }
        } catch (Throwable t) {
            if (threadCallback != null) {
                threadCallback.onError(Thread.currentThread(), "", t);
            }
        }
        interceptor.onEnd(threadTag, Thread.currentThread());
        return result;
    }
}
