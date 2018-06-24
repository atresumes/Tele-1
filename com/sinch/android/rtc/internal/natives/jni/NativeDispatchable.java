package com.sinch.android.rtc.internal.natives.jni;

import com.sinch.android.rtc.internal.natives.Dispatchable;

public class NativeDispatchable extends NativeProxy implements Dispatchable {
    private NativeDispatchable(long j) {
        super(j);
    }

    private static synchronized NativeDispatchable createInstance(long j) {
        NativeDispatchable nativeDispatchable;
        synchronized (NativeDispatchable.class) {
            nativeDispatchable = (NativeDispatchable) NativeProxy.get(j, NativeDispatchable.class);
            if (nativeDispatchable == null) {
                nativeDispatchable = new NativeDispatchable(j);
                NativeProxy.put(j, nativeDispatchable);
            }
        }
        return nativeDispatchable;
    }

    public native void dispose();

    public native void run();
}
