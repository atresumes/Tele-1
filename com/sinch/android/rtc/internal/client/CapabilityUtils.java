package com.sinch.android.rtc.internal.client;

import com.sinch.android.rtc.internal.Capability;

public class CapabilityUtils {
    public static Capability fromString(String str) {
        return "im".equals(str) ? Capability.MESSAGING : "voip".equals(str) ? Capability.CALLING : null;
    }
}
