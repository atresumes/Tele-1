package com.sinch.android.rtc.internal.client;

public interface SinchClientStatusProvider {
    boolean isDisposed();

    boolean isStarted();

    boolean isSupportCalling();

    boolean isSupportMessaging();
}
