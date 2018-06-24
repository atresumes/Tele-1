package com.sinch.android.rtc.internal.client.model;

import com.sinch.android.rtc.calling.CallNotificationResult;

public class DefaultCallNotificationResult implements CallNotificationResult {
    private final String callId;
    private final boolean isValid;
    private final String remoteUserId;
    private final boolean timedOut;
    private final boolean videoOffered;

    public DefaultCallNotificationResult(boolean z, boolean z2, String str, String str2, boolean z3) {
        this.isValid = z;
        this.timedOut = z2;
        this.callId = str;
        this.remoteUserId = str2;
        this.videoOffered = z3;
    }

    public String getCallId() {
        return this.callId;
    }

    public String getRemoteUserId() {
        return this.remoteUserId;
    }

    public boolean isTimedOut() {
        return this.timedOut;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public boolean isVideoOffered() {
        return this.videoOffered;
    }
}
