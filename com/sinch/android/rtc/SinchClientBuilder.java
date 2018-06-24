package com.sinch.android.rtc;

import android.content.Context;

public interface SinchClientBuilder {
    SinchClientBuilder applicationKey(String str);

    SinchClientBuilder applicationSecret(String str);

    SinchClient build();

    SinchClientBuilder callerIdentifier(String str);

    SinchClientBuilder context(Context context);

    SinchClientBuilder environmentHost(String str);

    SinchClientBuilder userId(String str);
}
