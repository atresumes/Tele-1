package com.sinch.android.rtc;

public interface SinchClientListener {
    void onClientFailed(SinchClient sinchClient, SinchError sinchError);

    void onClientStarted(SinchClient sinchClient);

    @Deprecated
    void onClientStopped(SinchClient sinchClient);

    void onLogMessage(int i, String str, String str2);

    void onRegistrationCredentialsRequired(SinchClient sinchClient, ClientRegistration clientRegistration);
}
