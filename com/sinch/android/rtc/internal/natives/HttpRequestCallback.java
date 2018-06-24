package com.sinch.android.rtc.internal.natives;

public interface HttpRequestCallback {
    void completed(int i, String str, int i2, String str2, String str3);

    void exception(String str);
}
