package com.sinch.android.rtc.internal.natives.jni;

import com.sinch.android.rtc.internal.natives.UserAgentEventListener;
import com.sinch.android.rtc.internal.natives.UserAgentState;

public class DefaultUserAgent extends NativeProxy implements UserAgent {
    private DefaultUserAgent(long j) {
        super(j);
    }

    private static synchronized DefaultUserAgent createInstance(long j) {
        DefaultUserAgent defaultUserAgent;
        synchronized (DefaultUserAgent.class) {
            defaultUserAgent = (DefaultUserAgent) NativeProxy.get(j, DefaultUserAgent.class);
            if (defaultUserAgent == null) {
                defaultUserAgent = new DefaultUserAgent(j);
                NativeProxy.put(j, defaultUserAgent);
            }
        }
        return defaultUserAgent;
    }

    private native int getStateAsInt();

    public native void deletePushData();

    public native void disableCapability(String str);

    public native void dispose();

    public native void doHouseKeeping();

    public native void enableCapability(String str);

    public native AsyncAudioController getAudioController();

    public native CallClient getCallClient();

    public native void getDestinationCapabilities(String str, String str2);

    public native Messaging getMessaging();

    public native void getNativeVideoCapturer(Object obj);

    public UserAgentState getState() {
        int stateAsInt = getStateAsInt();
        return (stateAsInt < 0 || stateAsInt >= UserAgentState.values().length) ? UserAgentState.UNKNOWN : UserAgentState.values()[stateAsInt];
    }

    public native boolean isCapabilityEnabled(String str);

    public native boolean isStarted();

    public native void refreshConfig();

    public native void resendFailedRequests();

    public native void setApplicationContext(long j);

    public native void setEventListener(UserAgentEventListener userAgentEventListener);

    public native void setManagedPushProfile(String str, String str2);

    public native void setMinimumLogLevel(int i);

    public native void setPushData(byte[] bArr);

    public native void setPushNotificationDisplayName(String str);

    public native void setUseManagedPush(boolean z);

    public native void start();

    public native void startBroadcastListener();

    public native void stop();

    public native void stopBroadcastListener();

    public native void triggerNewPushProfileRequest();
}
