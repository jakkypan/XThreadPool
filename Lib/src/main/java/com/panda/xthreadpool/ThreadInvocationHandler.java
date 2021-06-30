package com.panda.xthreadpool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 对线程的任何操作做拦截
 *
 * @author panda
 * created at 2021/3/9 6:04 PM
 */
class ThreadInvocationHandler implements InvocationHandler {
    private Object delegate;
    private ThreadLocal<Long> sThreadLocal = new ThreadLocal<>();

    public Object bind(Object delegate) {
        this.delegate = delegate;
        return Proxy.newProxyInstance(
                this.delegate.getClass().getClassLoader(), this.delegate
                        .getClass().getInterfaces(), this);
    }

    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object result = null;
        try {
            if (method.getName().equals("onStart")) {
                sThreadLocal.set(System.currentTimeMillis());
                ThreadRecorder.add(new ThreadRecorder.RecorderBean((String) args[0], (Thread) args[1]));
                result = method.invoke(this.delegate, args);
            } else if (method.getName().equals("onEnd")) {
                result = method.invoke(this.delegate, args);
                long timeElapsed = System.currentTimeMillis() - sThreadLocal.get();
                ThreadRecorder.remove(new ThreadRecorder.RecorderBean((String) args[0], (Thread) args[1]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
