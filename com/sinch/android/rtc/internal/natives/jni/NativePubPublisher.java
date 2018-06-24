package com.sinch.android.rtc.internal.natives.jni;

import com.sinch.android.rtc.internal.natives.PubPublisher;

public class NativePubPublisher extends NativeProxy implements PubPublisher {
    private NativePubPublisher(long j) {
        super(j);
    }

    public static synchronized NativePubPublisher createInstance(long j) {
        NativePubPublisher nativePubPublisher;
        synchronized (NativePubPublisher.class) {
            nativePubPublisher = (NativePubPublisher) NativeProxy.get(j, NativePubPublisher.class);
            if (nativePubPublisher == null) {
                nativePubPublisher = new NativePubPublisher(j);
                NativeProxy.put(j, nativePubPublisher);
            }
        }
        return nativePubPublisher;
    }

    public native void dispose();

    public native void onPublishFailed(int i, String str, String str2);

    public native void onPublishSuccess(String str);
}
