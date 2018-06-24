package com.sinch.android.rtc.internal.natives.jni;

import com.sinch.android.rtc.internal.natives.UserAgentEventListener;
import com.sinch.android.rtc.internal.natives.UserAgentState;

public interface UserAgent {
    void deletePushData();

    void disableCapability(String str);

    void dispose();

    void doHouseKeeping();

    void enableCapability(String str);

    AsyncAudioController getAudioController();

    CallClient getCallClient();

    void getDestinationCapabilities(String str, String str2);

    Messaging getMessaging();

    void getNativeVideoCapturer(Object obj);

    UserAgentState getState();

    boolean isCapabilityEnabled(String str);

    boolean isStarted();

    void refreshConfig();

    void resendFailedRequests();

    void setApplicationContext(long j);

    void setEventListener(UserAgentEventListener userAgentEventListener);

    void setManagedPushProfile(String str, String str2);

    void setMinimumLogLevel(int i);

    void setPushData(byte[] bArr);

    void setPushNotificationDisplayName(String str);

    void setUseManagedPush(boolean z);

    void start();

    void startBroadcastListener();

    void stop();

    void stopBroadcastListener();

    void triggerNewPushProfileRequest();
}
