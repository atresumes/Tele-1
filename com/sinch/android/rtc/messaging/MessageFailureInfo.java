package com.sinch.android.rtc.messaging;

import com.sinch.android.rtc.SinchError;

public interface MessageFailureInfo {
    String getMessageId();

    String getRecipientId();

    SinchError getSinchError();
}
