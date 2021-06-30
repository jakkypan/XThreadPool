package com.panda.xthreadpool;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 运行中线程记录器
 *
 * @author panda
 * created at 2021/3/9 7:23 PM
 */
public class ThreadRecorder {
    // 存活中的线程
    private static final ConcurrentLinkedQueue<RecorderBean> alivedThreads = new ConcurrentLinkedQueue<>();
    // 所有的线程
    private static final ConcurrentLinkedQueue<RecorderBean> allThreads = new ConcurrentLinkedQueue<>();

    public static void add(RecorderBean thread) {
        alivedThreads.add(thread);
        allThreads.add(thread);
    }

    public static boolean isAlive(String threadTag) {
        for (RecorderBean rb : alivedThreads) {
            if (rb.threadTag.equals(threadTag)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAlive(Thread thread) {
        for (RecorderBean rb : alivedThreads) {
            if (rb.curThread.getId() == thread.getId()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFinished(String threadTag) {
        if (isAlive(threadTag)) return false;
        for (RecorderBean rb : allThreads) {
            if (rb.threadTag.equals(threadTag)) {
                return true;
            }
        }
        // 表示这个tag的线程还没启动
        return false;
    }

    public static boolean isFinished(Thread thread) {
        if (isAlive(thread)) return false;
        for (RecorderBean rb : allThreads) {
            if (rb.curThread.getId() == thread.getId()) {
                return true;
            }
        }
        // 表示这个tag的线程还没启动
        return false;
    }

    public static void remove(RecorderBean thread) {
        for (RecorderBean rb : alivedThreads) {
            if (rb.equals(thread)) {
                alivedThreads.remove(rb);
                break;
            }
        }
    }

    public static ConcurrentLinkedQueue<RecorderBean> getAll() {
        return allThreads;
    }

    public static ConcurrentLinkedQueue<RecorderBean> getAlived() {
        return alivedThreads;
    }

    public static class RecorderBean {
        private final String threadTag;
        private final Thread curThread;

        public RecorderBean(String threadTag, Thread curThread) {
            this.threadTag = threadTag;
            this.curThread = curThread;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RecorderBean that = (RecorderBean) o;
            return Objects.equals(threadTag, that.threadTag) &&
                    Objects.equals(curThread.getId(), that.curThread.getId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(threadTag, curThread);
        }
    }
}
