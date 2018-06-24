package com.sinch.android.rtc.internal.client;

import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;

public interface InternalSinchClientListener extends SinchClientListener {
    void onNotification(SinchClient sinchClient, String str, String str2);
}
