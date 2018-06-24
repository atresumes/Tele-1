package com.sinch.android.rtc.internal.client;

public class CurrentThreadSleeper implements Sleepable {
    private static CurrentThreadSleeper instance = new CurrentThreadSleeper();

    private CurrentThreadSleeper() {
    }

    public static Sleepable getInstance() {
        return instance;
    }

    public void sleep(long j) {
        Thread.sleep(j);
    }
}
