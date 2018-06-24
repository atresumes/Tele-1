package com.sinch.android.rtc.internal.natives.jni;

public class NativeCallClient extends NativeProxy implements CallClient {
    private NativeCallClient(long j) {
        super(j);
    }

    private static synchronized NativeCallClient createInstance(long j) {
        NativeCallClient nativeCallClient;
        synchronized (NativeCallClient.class) {
            nativeCallClient = (NativeCallClient) NativeProxy.get(j, NativeCallClient.class);
            if (nativeCallClient == null) {
                nativeCallClient = new NativeCallClient(j);
                NativeProxy.put(j, nativeCallClient);
            }
        }
        return nativeCallClient;
    }

    public native Session createIncomingCall(String str, String str2, int i);

    public native Session createOutgoingCall(String str, String str2, String str3, String[] strArr, String[] strArr2, int i);

    public native void setListener(CallClientListener callClientListener);
}
