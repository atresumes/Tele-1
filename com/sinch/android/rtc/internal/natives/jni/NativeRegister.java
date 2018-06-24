package com.sinch.android.rtc.internal.natives.jni;

import com.sinch.android.rtc.ClientRegistration;

public class NativeRegister extends NativeProxy implements ClientRegistration {
    public NativeRegister(long j) {
        super(j);
    }

    private static synchronized NativeRegister createInstance(long j) {
        NativeRegister nativeRegister;
        synchronized (NativeRegister.class) {
            nativeRegister = (NativeRegister) NativeProxy.get(j, NativeRegister.class);
            if (nativeRegister == null) {
                nativeRegister = new NativeRegister(j);
                NativeProxy.put(j, nativeRegister);
            }
        }
        return nativeRegister;
    }

    public void register(String str, long j) {
        registerInstance(j, str);
    }

    public void registerFailed() {
        registerInstanceFailed();
    }

    public native void registerInstance(long j, String str);

    public native void registerInstanceFailed();
}
