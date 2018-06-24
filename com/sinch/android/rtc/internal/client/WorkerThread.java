package com.sinch.android.rtc.internal.client;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Executor;

class WorkerThread extends Thread implements Executor {
    private Handler handler;
    private boolean isAlive = false;

    public WorkerThread() {
        super("WorkerThread-" + System.currentTimeMillis());
    }

    public static WorkerThread createWorkerThread() {
        WorkerThread workerThread = new WorkerThread();
        synchronized (workerThread) {
            workerThread.start();
            try {
                workerThread.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        return workerThread;
    }

    public void execute(Runnable runnable) {
        if (this.isAlive) {
            this.handler.post(runnable);
        }
    }

    public Handler getHandler() {
        return this.handler;
    }

    public void run() {
        Looper.prepare();
        this.isAlive = true;
        this.handler = new Handler();
        synchronized (this) {
            notifyAll();
        }
        Looper.loop();
    }

    public void stopThread() {
        this.isAlive = false;
        this.handler.getLooper().quit();
        this.handler.removeCallbacksAndMessages(null);
    }
}
