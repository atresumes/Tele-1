package com.sinch.android.rtc.internal;

public interface CallbackHandler {
    boolean post(Runnable runnable);

    void postDelayed(Runnable runnable, int i);

    void removeCallbacksAndMessages(Object obj);
}
