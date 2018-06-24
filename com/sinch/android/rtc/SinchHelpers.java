package com.sinch.android.rtc;

import android.content.Intent;
import com.sinch.android.rtc.internal.client.DefaultSinchClient;

public class SinchHelpers {
    public static boolean isSinchPushIntent(Intent intent) {
        return (intent == null || intent.getExtras().isEmpty() || intent.getStringExtra(DefaultSinchClient.GCM_PAYLOAD_TAG_SINCH) == null) ? false : true;
    }
}
