package com.sinch.android.rtc.internal.client.model;

import com.sinch.android.rtc.messaging.MessageNotificationResult;

public class DefaultMessageNotificationResult implements MessageNotificationResult {
    private final boolean isValid;
    private final String messageId;
    private final String senderId;

    public DefaultMessageNotificationResult(boolean z, String str, String str2) {
        this.isValid = z;
        this.messageId = str;
        this.senderId = str2;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public boolean isValid() {
        return this.isValid;
    }
}
