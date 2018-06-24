package com.sinch.android.rtc.internal.natives;

import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import java.util.Date;

public class MessageInfo implements MessageDeliveryInfo {
    private static final long MILLIS_IN_SECOND = 1000;
    private String messageId;
    private String recipientId;
    private Date timestamp;

    public MessageInfo(String str, String str2, long j) {
        this.messageId = str;
        this.recipientId = str2;
        this.timestamp = new Date(MILLIS_IN_SECOND * j);
    }

    public String getMessageId() {
        return this.messageId;
    }

    public String getRecipientId() {
        return this.recipientId;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }
}
