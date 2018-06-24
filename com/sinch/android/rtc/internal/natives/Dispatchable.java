package com.sinch.android.rtc.internal.natives;

public interface Dispatchable {
    void dispose();

    void invalidate();

    void run();
}
