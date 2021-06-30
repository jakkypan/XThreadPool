package com.panda.xthreadpool;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

import androidx.annotation.Nullable;

/**
 * @author panda
 * created at 2021/3/9 5:16 PM
 */
public final class AndroidHandler implements Executor {

    private static final AndroidHandler instance = new AndroidHandler();
    private static final Handler main = new Handler(Looper.getMainLooper());

    public static AndroidHandler getInstance() {
        return instance;
    }

    @Override
    public void execute(@Nullable final Runnable runnable) {
        if (runnable == null) return;
        Looper mainLooper = Looper.getMainLooper();
        if (Looper.myLooper() == mainLooper) {
            runnable.run();
            return;
        }
        main.post(() -> {
            runnable.run();
        });
    }
}
