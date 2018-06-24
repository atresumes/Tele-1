package com.sinch.android.rtc.internal.natives.jni;

import com.sinch.android.rtc.internal.natives.MessagingEventListener;

public class Messaging extends NativeProxy {
    private Messaging(long j) {
        super(j);
    }

    private static synchronized Messaging createInstance(long j) {
        Messaging messaging;
        synchronized (Messaging.class) {
            messaging = (Messaging) NativeProxy.get(j, Messaging.class);
            if (messaging == null) {
                messaging = new Messaging(j);
                NativeProxy.put(j, messaging);
            }
        }
        return messaging;
    }

    public native void dispose();

    public native void sendMessage(String str, String[] strArr, String[] strArr2, String[] strArr3, String str2, long j);

    public native void setEventListener(MessagingEventListener messagingEventListener);

    public native void tryScheduleRetryFailedMessages();
}
