package com.panda.xthreadpool;

import androidx.annotation.NonNull;

/**
 * @author panda
 * created at 2021/3/9 5:02 PM
 */
class RunnableWrapper implements Runnable {
    private ThreadCallback threadCallback;
    private Runnable realRunnable;
    private GlobalThreadInterceptor interceptor;
    private String threadTag;

    public RunnableWrapper(Runnable realRunnable, String threadTag,
                           @NonNull GlobalThreadInterceptor interceptor, ThreadCallback threadCallback) {
        this.realRunnable = realRunnable;
        this.threadCallback = threadCallback;
        this.interceptor = interceptor;
        this.threadTag = threadTag;
    }

    @Override
    public void run() {
        if (realRunnable == null) return;
        interceptor.onStart(threadTag, Thread.currentThread());
        if (threadCallback != null) {
            threadCallback.onStart(Thread.currentThread());
        }

        try {
            realRunnable.run();
            if (threadCallback != null) {
                threadCallback.onCompleted(Thread.currentThread());
            }
        } catch (Throwable t) {
            if (threadCallback != null) {
                threadCallback.onError(Thread.currentThread(), "", t);
            }
        }
        interceptor.onEnd(threadTag, Thread.currentThread());
    }
}
