package com.sinch.android.rtc.internal.natives;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.internal.natives.jni.UserAgent;
import java.util.List;

public interface UserAgentEventListener {
    void log(int i, String str, String str2);

    void onAudioFeaturesChanged(int i, int i2, boolean z);

    void onConfigChanged(String str);

    void onDestinationCapabilities(String str, String str2, List<String> list);

    void onDestinationCapabilitiesFailed(String str, String str2, SinchError sinchError);

    void onNewPushProfileRequired(String str);

    void onNotification(String str, String str2);

    void onRegisterInstance(UserAgent userAgent, ClientRegistration clientRegistration);

    void onStartFailed(UserAgent userAgent, SinchError sinchError);

    void onStarted(UserAgent userAgent);
}
