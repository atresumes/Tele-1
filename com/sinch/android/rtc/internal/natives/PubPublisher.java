package com.sinch.android.rtc.internal.natives;

public interface PubPublisher {
    void onPublishFailed(int i, String str, String str2);

    void onPublishSuccess(String str);
}
