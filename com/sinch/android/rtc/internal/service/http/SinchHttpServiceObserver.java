package com.sinch.android.rtc.internal.service.http;

public interface SinchHttpServiceObserver {
    void onHttpRequestSent(String str, String str2, byte[] bArr);
}
