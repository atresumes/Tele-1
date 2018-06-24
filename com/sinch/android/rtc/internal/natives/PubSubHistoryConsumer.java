package com.sinch.android.rtc.internal.natives;

public interface PubSubHistoryConsumer {
    void endHistoryGet(String[] strArr, String[] strArr2, String str, String str2);

    void endHistoryGetWithoutTimestamps(String[] strArr);

    void failedHistoryGet();

    String getChannel();

    String getSubscribeKey();
}
