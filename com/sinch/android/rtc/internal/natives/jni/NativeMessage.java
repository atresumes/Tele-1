package com.sinch.android.rtc.internal.natives.jni;

import com.payUMoney.sdk.SdkConstants;
import java.util.List;
import java.util.Map;

public class NativeMessage extends NativeProxy {
    private NativeMessage(long j) {
        super(j);
    }

    private static synchronized NativeMessage createInstance(long j) {
        NativeMessage nativeMessage;
        synchronized (NativeMessage.class) {
            nativeMessage = (NativeMessage) NativeProxy.get(j, NativeMessage.class);
            if (nativeMessage == null) {
                nativeMessage = new NativeMessage(j);
                NativeProxy.put(j, nativeMessage);
            }
        }
        return nativeMessage;
    }

    public native List<String> getDestinations();

    public native String getFrom();

    public native Map<String, String> getHeaders();

    public native String getId();

    public native String getText();

    public native long getTimestamp();

    public String toString() {
        String id = getId();
        if (id == null) {
            id = SdkConstants.NULL_STRING;
        }
        return "NativeMessage [id=" + id + ", nativeAddress=" + String.valueOf(getNativeAddress()) + "]";
    }
}
