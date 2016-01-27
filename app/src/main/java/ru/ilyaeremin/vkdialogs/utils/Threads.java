package ru.ilyaeremin.vkdialogs.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Ilya Eremin on 1/26/16.
 */
public class Threads extends Thread {
    public volatile Handler handler           = null;
    private final       Object  handlerSyncObject = new Object();

    public final static Threads worker = new Threads("worker");

    public Threads(final String threadName) {
        setName(threadName);
        start();
    }

    public void postRunnable(Runnable runnable) {
        postRunnable(runnable, 0);
    }

    public void postRunnable(Runnable runnable, long delay) {
        if (handler == null) {
            synchronized (handlerSyncObject) {
                if (handler == null) {
                    try {
                        handlerSyncObject.wait();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }

        if (handler != null) {
            if (delay <= 0) {
                handler.post(runnable);
            } else {
                handler.postDelayed(runnable, delay);
            }
        }
    }

    public void run() {
        Looper.prepare();
        synchronized (handlerSyncObject) {
            handler = new Handler();
            handlerSyncObject.notify();
        }
        Looper.loop();
    }

}
