package com.sinch.android.rtc.internal.natives.jni;

import com.sinch.android.rtc.internal.natives.PubSubHistoryConsumer;

public class NativePubSubHistoryConsumer extends NativeProxy implements PubSubHistoryConsumer {
    private NativePubSubHistoryConsumer(long j) {
        super(j);
    }

    public static synchronized NativePubSubHistoryConsumer createInstance(long j) {
        NativePubSubHistoryConsumer nativePubSubHistoryConsumer;
        synchronized (NativePubSubHistoryConsumer.class) {
            nativePubSubHistoryConsumer = (NativePubSubHistoryConsumer) NativeProxy.get(j, NativePubSubHistoryConsumer.class);
            if (nativePubSubHistoryConsumer == null) {
                nativePubSubHistoryConsumer = new NativePubSubHistoryConsumer(j);
                NativeProxy.put(j, nativePubSubHistoryConsumer);
            }
        }
        return nativePubSubHistoryConsumer;
    }

    public native void endHistoryGet(String[] strArr, String[] strArr2, String str, String str2);

    public native void endHistoryGetWithoutTimestamps(String[] strArr);

    public native void failedHistoryGet();

    public native String getChannel();

    public native String getSubscribeKey();
}
