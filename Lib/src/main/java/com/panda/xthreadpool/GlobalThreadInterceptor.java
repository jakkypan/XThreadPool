package com.panda.xthreadpool;

/**
 * @author panda
 * created at 2021/3/9 6:18 PM
 */
public interface GlobalThreadInterceptor {
    /**
     * 运行开始
     *
     * @param threadTag 线程tag，从而能监控到是哪个线程
     * @param thread 运行线程
     */
    void onStart(String threadTag, Thread thread);

    /**
     * 运行结束
     *
     * @param threadTag 线程tag，从而能监控到是哪个线程
     * @param thread 运行线程
     */
    void onEnd(String threadTag, Thread thread);
}
