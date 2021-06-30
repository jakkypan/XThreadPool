package com.panda.xthreadpool;

/**
 * 保证一定能做全局监控
 *
 * @author panda
 * created at 2021/3/10 5:39 PM
 */
class EmptyGlobalThreadInterceptor implements GlobalThreadInterceptor {

    @Override
    public void onStart(String threadTag, Thread thread) {

    }

    @Override
    public void onEnd(String threadTag, Thread thread) {

    }
}
