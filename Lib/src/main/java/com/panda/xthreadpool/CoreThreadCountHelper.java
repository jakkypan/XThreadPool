package com.panda.xthreadpool;

/**
 * 得到合理的核心线程数
 *
 * @author panda
 * created at 2021/3/9 10:29 PM
 */
class CoreThreadCountHelper {
    private static int CPUS = Runtime.getRuntime().availableProcessors();

    /**
     * 对于 CPU 密集型的计算场景，理论上线程的数量 = CPU 核数就是最合适的，
     * 不过通常把线程的数量设置为CPU 核数 +1，会实现最优的利用率。
     *
     * @return
     */
    public static int cpuCoreThreads() {
        return Math.max(CPUS + 1, 4);
    }

    /**
     * 线程数 = CPU 核心数 / (1 - 阻塞系数)
     *
     * 其中计算密集型阻塞系数为 0，IO 密集型阻塞系数接近 1，一般认为在 0.8 ~ 0.9 之间。
     *
     * @return
     */
    public static int ioCoreThreads() {
        return (int) (CPUS / (1 - 0.9));
    }

}
