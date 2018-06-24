package com.sinch.android.rtc.internal.natives.jni;

import com.sinch.android.rtc.internal.natives.HttpRequestCallback;

public class NativeHttpRequestCallback extends NativeProxy implements HttpRequestCallback {
    private NativeHttpRequestCallback(long j) {
        super(j);
    }

    private static synchronized NativeHttpRequestCallback createInstance(long j) {
        NativeHttpRequestCallback nativeHttpRequestCallback;
        synchronized (NativeHttpRequestCallback.class) {
            nativeHttpRequestCallback = (NativeHttpRequestCallback) NativeProxy.get(j, NativeHttpRequestCallback.class);
            if (nativeHttpRequestCallback == null) {
                nativeHttpRequestCallback = new NativeHttpRequestCallback(j);
                NativeProxy.put(j, nativeHttpRequestCallback);
            }
        }
        return nativeHttpRequestCallback;
    }

    public native void completed(int i, String str, int i2, String str2, String str3);

    public native void exception(String str);
}
