package com.panda.xthreadpool;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import androidx.annotation.IntDef;

/**
 * @author panda
 * created at 2021/3/7 12:00 PM
 */
public class ThreadPoolExecutors {
    public static final int THREAD_CACHE = 0;
    public static final int THREAD_SINGLE = 1;
    public static final int THREAD_FIXED = 2;
    public static final int THREAD_SCHEDULED = 3;

    @IntDef({THREAD_CACHE, THREAD_SINGLE, THREAD_FIXED, THREAD_SCHEDULED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ThreadType {
    }

    private ExecutorService pool;
    private ThreadCallback threadCallback;
    private int priority = Thread.NORM_PRIORITY;
    private final ThreadInvocationHandler threadInvocationHandler;
    private final GlobalThreadInterceptor globalThreadInterceptor;
    private String threadTag;

    private ThreadPoolExecutors(@ThreadType int type, int size, GlobalThreadInterceptor interceptor) {
        pool = createPool(type, size);
        threadInvocationHandler = new ThreadInvocationHandler();
        globalThreadInterceptor = (GlobalThreadInterceptor) threadInvocationHandler.bind(interceptor);
    }

    public ThreadPoolExecutors setThreadCallback(ThreadCallback threadCallback) {
        this.threadCallback = threadCallback;
        return this;
    }

    public ThreadPoolExecutors setThreadTag(String threadTag) {
        this.threadTag = threadTag;
        return this;
    }

    public ThreadPoolExecutors setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public void execute(Runnable command) {
        pool.execute(new RunnableWrapper(command, threadTag, globalThreadInterceptor, threadCallback));
    }

    public <T> Future<T> submit(Callable<T> command) {
        return pool.submit(new CallableWrapper<T>(command, threadTag, globalThreadInterceptor, threadCallback));
    }

    public void executeSchedule(Runnable command, long delay, TimeUnit unit) {
        if (pool instanceof ScheduledExecutorService) {
            ScheduledExecutorService scheduledPool = (ScheduledExecutorService) pool;
            scheduledPool.schedule(new RunnableWrapper(command, threadTag, globalThreadInterceptor, threadCallback), delay, unit);
        }
    }

    public <V> ScheduledFuture<V> executeSchedule(Callable<V> callable, long delay, TimeUnit unit) {
        if (pool instanceof ScheduledExecutorService) {
            ScheduledExecutorService scheduledPool = (ScheduledExecutorService) pool;
            return scheduledPool.schedule(new CallableWrapper<V>(callable, threadTag, globalThreadInterceptor, threadCallback), delay, unit);
        }
        return null;
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit) {
        if (pool instanceof ScheduledExecutorService) {
            ScheduledExecutorService scheduledPool = (ScheduledExecutorService) pool;
            return scheduledPool.scheduleAtFixedRate(new RunnableWrapper(command, threadTag, globalThreadInterceptor, threadCallback), initialDelay, period, unit);
        }
        return null;
    }

    /**
     * ???????????????????????????SHUTDOWN?????????????????????????????????????????????????????????
     */
    public void stop() {
        pool.shutdown();
        pool = null;
    }

    /**
     * ??????????????????????????????STOP???????????????????????????
     */
    public void stopNow() {
        pool.shutdownNow();
        pool = null;
    }

    private synchronized ExecutorService createPool(@ThreadType int type, int size) {
        switch (type) {
            case THREAD_CACHE:
                // ??????????????????????????????????????????????????????????????????????????????????????????????????????
                return Executors.newCachedThreadPool();
            case THREAD_FIXED:
                // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
                return Executors.newFixedThreadPool(size);
            case THREAD_SCHEDULED:
                // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                return Executors.newScheduledThreadPool(size);
            case THREAD_SINGLE:
            default:
                // ??????????????????????????????????????????????????????????????????????????????
                return Executors.newSingleThreadExecutor();
        }
    }

    public static class PoolBuilder {
        /**
         * ???????????????
         */
        private @ThreadType
        int type;
        /**
         * ???????????????
         */
        private int size;

        /**
         * ?????????????????????
         */
        private GlobalThreadInterceptor interceptor;

        private PoolBuilder(@ThreadType int type, int size) {
            this.type = type;
            this.size = size;
        }

        public static PoolBuilder createSingle() {
            return new PoolBuilder(THREAD_SINGLE, 0);
        }

        public static PoolBuilder createFixed(int size) {
            return new PoolBuilder(THREAD_FIXED, size);
        }

        public static PoolBuilder createCache() {
            return new PoolBuilder(THREAD_CACHE, 0);
        }

        public static PoolBuilder createScheduled(int size) {
            return new PoolBuilder(THREAD_SCHEDULED, size);
        }

        public PoolBuilder setInterceptor(GlobalThreadInterceptor interceptor) {
            this.interceptor = interceptor;
            return this;
        }

        public ThreadPoolExecutors build() {
            if (interceptor == null) {
                interceptor = new EmptyGlobalThreadInterceptor();
            }
            return new ThreadPoolExecutors(type, size, interceptor);
        }
    }
}
