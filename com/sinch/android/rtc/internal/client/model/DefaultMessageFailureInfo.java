package com.sinch.android.rtc.internal.client.model;

import com.sinch.android.rtc.SinchError;

public class DefaultMessageFailureInfo {
    private SinchError error;
    private String messageId;
    private String recipientId;

    public DefaultMessageFailureInfo(String str, String str2, SinchError sinchError) {
        this.messageId = str;
        this.recipientId = str2;
        this.error = sinchError;
    }

    public SinchError getError() {
        return this.error;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public String getRecipientId() {
        return this.recipientId;
    }
}
