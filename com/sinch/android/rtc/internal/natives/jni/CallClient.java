package com.sinch.android.rtc.internal.natives.jni;

public interface CallClient {
    Session createIncomingCall(String str, String str2, int i);

    Session createOutgoingCall(String str, String str2, String str3, String[] strArr, String[] strArr2, int i);

    void setListener(CallClientListener callClientListener);
}
