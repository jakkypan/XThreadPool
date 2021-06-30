package com.panda.xthreadpool;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author panda
 * created at 2021/3/11 12:56 AM
 */
@Aspect
class RunnableAspect {
    // 把线程池的过滤掉
    @Pointcut("!within(com.panda.college.libbase.thread.RunnableWrapper)")
    public void baseCondition() {
    }

    // https://www.programmersought.com/article/4096362968/
    @Pointcut("execution(* java.lang.Runnable+.*())")
    public void onRunnable() {
    }

    @Before("onRunnable() && baseCondition()")
    public void beforeRun() {
        UnTrackedThreadRecorder.add(Thread.currentThread());
    }

    @After("onRunnable() && baseCondition()")
    public void afterRun() {
        UnTrackedThreadRecorder.remove(Thread.currentThread());
    }
}
