package com.sinch.android.rtc.internal.natives.jni;

public class NativeServiceProviderImpl extends NativeProxy {
    private NativeServiceProviderImpl(long j) {
        super(j);
    }

    public static synchronized NativeServiceProviderImpl createInstance(long j) {
        NativeServiceProviderImpl nativeServiceProviderImpl;
        synchronized (NativeServiceProviderImpl.class) {
            nativeServiceProviderImpl = (NativeServiceProviderImpl) NativeProxy.get(j, NativeServiceProviderImpl.class);
            if (nativeServiceProviderImpl == null) {
                nativeServiceProviderImpl = new NativeServiceProviderImpl(j);
                NativeProxy.put(j, nativeServiceProviderImpl);
            }
        }
        return nativeServiceProviderImpl;
    }

    public native void dispose();
}
