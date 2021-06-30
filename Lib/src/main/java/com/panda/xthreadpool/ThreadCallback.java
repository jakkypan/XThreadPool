package com.panda.xthreadpool;

/**
 * @author panda
 * created at 2021/3/9 4:36 PM
 */
public interface ThreadCallback {
    /**
     * 运行异常
     *
     * @param thread 运行线程
     * @param msg 补充消息
     * @param t 异常原因
     */
    void onError(Thread thread, String msg, Throwable t);

    /**
     * 运行结束
     *
     * @param thread 运行线程
     */
    void onCompleted(Thread thread);

    /**
     * 运行开始
     *
     * @param thread 运行线程
     */
    void onStart(Thread thread);
}
