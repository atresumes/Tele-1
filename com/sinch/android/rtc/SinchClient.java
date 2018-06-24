package com.sinch.android.rtc;

import android.content.Intent;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.video.VideoController;

public interface SinchClient {
    void addSinchClientListener(SinchClientListener sinchClientListener);

    void checkManifest();

    AudioController getAudioController();

    CallClient getCallClient();

    String getLocalUserId();

    MessageClient getMessageClient();

    VideoController getVideoController();

    boolean isStarted();

    void registerPushNotificationData(byte[] bArr);

    NotificationResult relayRemotePushNotificationPayload(Intent intent);

    NotificationResult relayRemotePushNotificationPayload(String str);

    void removeSinchClientListener(SinchClientListener sinchClientListener);

    void setPushNotificationDisplayName(String str);

    void setSupportActiveConnectionInBackground(boolean z);

    void setSupportCalling(boolean z);

    void setSupportManagedPush(boolean z);

    void setSupportMessaging(boolean z);

    void setSupportPushNotifications(boolean z);

    void start();

    void startListeningOnActiveConnection();

    @Deprecated
    void stop();

    void stopListeningOnActiveConnection();

    void terminate();

    void terminateGracefully();

    void unregisterManagedPush();

    void unregisterPushNotificationData();
}
