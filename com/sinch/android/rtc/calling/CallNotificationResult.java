package com.sinch.android.rtc.calling;

public interface CallNotificationResult {
    String getCallId();

    String getRemoteUserId();

    boolean isTimedOut();

    boolean isVideoOffered();
}
