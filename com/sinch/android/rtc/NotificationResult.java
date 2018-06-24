package com.sinch.android.rtc;

import com.sinch.android.rtc.calling.CallNotificationResult;
import com.sinch.android.rtc.messaging.MessageNotificationResult;

public interface NotificationResult {
    CallNotificationResult getCallResult();

    String getDisplayName();

    MessageNotificationResult getMessageResult();

    boolean isCall();

    boolean isMessage();

    boolean isValid();
}
