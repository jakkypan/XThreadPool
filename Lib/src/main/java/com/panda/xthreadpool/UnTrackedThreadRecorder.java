package com.panda.xthreadpool;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 记录离线池以外的线程
 *
 * @author panda
 * created at 2021/3/11 2:08 PM
 */
class UnTrackedThreadRecorder {
    private static final AtomicBoolean isNeedTracked = new AtomicBoolean(false);

    public static void openTrackedThreadRecorder() {
        isNeedTracked.compareAndSet(false, true);
    }

    public static void closeTrackedThreadRecorder() {
        isNeedTracked.compareAndSet(true, false);
    }

    // 存活中的线程
    private static final ConcurrentLinkedQueue<Thread> alivedThreads = new ConcurrentLinkedQueue<>();
    // 所有的线程
    private static final ConcurrentLinkedQueue<Thread> allThreads = new ConcurrentLinkedQueue<>();

    public static void add(Thread thread) {
        if (!isNeedTracked.get())return;
        alivedThreads.add(thread);
        allThreads.add(thread);
    }

    public static void remove(Thread t) {
        for (Thread thread : alivedThreads) {
            if (t.getId() == thread.getId()) {
                alivedThreads.remove(thread);
                break;
            }
        }
    }

    public static boolean isAlive(long threadId) {
        for (Thread thread : alivedThreads) {
            if (thread.getId() == threadId) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAlive(Thread t) {
        for (Thread thread : alivedThreads) {
            if (thread.getId() == t.getId()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFinished(long threadId) {
        if (isAlive(threadId)) return false;
        for (Thread thread : allThreads) {
            if (thread.getId() == threadId) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFinished(Thread t) {
        if (isAlive(t)) return false;
        for (Thread thread : allThreads) {
            if (thread.getId() == t.getId()) {
                return true;
            }
        }
        return false;
    }

    public static ConcurrentLinkedQueue<Thread> getAll() {
        return allThreads;
    }

    public static ConcurrentLinkedQueue<Thread> getAlived() {
        return alivedThreads;
    }
}
