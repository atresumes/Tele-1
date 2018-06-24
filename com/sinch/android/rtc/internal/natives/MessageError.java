package com.sinch.android.rtc.internal.natives;

import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.messaging.MessageFailureInfo;

public class MessageError implements MessageFailureInfo {
    private final SinchError error;
    private final String messageId;
    private final String recipientId;

    public MessageError(String str, String str2, SinchError sinchError) {
        this.messageId = str;
        this.recipientId = str2;
        this.error = sinchError;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public String getRecipientId() {
        return this.recipientId;
    }

    public SinchError getSinchError() {
        return this.error;
    }
}
