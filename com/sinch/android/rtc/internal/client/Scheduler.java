package com.sinch.android.rtc.internal.client;

import java.util.Timer;
import java.util.TimerTask;

public abstract class Scheduler {
    private boolean isRunning;
    private Timer timer;

    class C05501 extends TimerTask {
        C05501() {
        }

        public void run() {
            Scheduler.this.onRun();
        }
    }

    public Scheduler() {
        this.timer = new Timer();
        this.isRunning = false;
    }

    public Scheduler(String str) {
        this.timer = new Timer(str);
        this.isRunning = false;
    }

    public void dispose() {
        if (this.isRunning) {
            stop();
        }
        this.timer = null;
        onDispose();
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    protected abstract void onDispose();

    protected abstract void onRun();

    public void start(int i) {
        this.isRunning = true;
        this.timer.schedule(new C05501(), (long) i, (long) i);
    }

    public void stop() {
        this.isRunning = false;
        this.timer.cancel();
        this.timer.purge();
    }
}
