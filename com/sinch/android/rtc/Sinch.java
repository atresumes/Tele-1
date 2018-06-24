package com.sinch.android.rtc;

import com.sinch.android.rtc.internal.gen.SinchVersion;

public final class Sinch {
    public static SinchClientBuilder getSinchClientBuilder() {
        return new DefaultSinchClientBuilder();
    }

    public static String getVersion() {
        return SinchVersion.FULL_VERSION_WITH_REVISION;
    }
}
