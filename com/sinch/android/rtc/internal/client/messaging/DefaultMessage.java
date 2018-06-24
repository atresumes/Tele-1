package com.sinch.android.rtc.internal.client.messaging;

import com.sinch.android.rtc.internal.natives.jni.NativeMessage;
import com.sinch.android.rtc.messaging.Message;
import java.util.Date;
import java.util.List;
import java.util.Map;

class DefaultMessage implements Message {
    static final /* synthetic */ boolean $assertionsDisabled = (!DefaultMessage.class.desiredAssertionStatus());
    private static final long MILLIS_IN_SECOND = 1000;
    private final Date mTimestamp;
    private NativeMessage nativeMessage;

    public DefaultMessage(NativeMessage nativeMessage) {
        this.nativeMessage = nativeMessage;
        this.mTimestamp = new Date(nativeMessage.getTimestamp() * MILLIS_IN_SECOND);
    }

    public DefaultMessage(NativeMessage nativeMessage, Date date) {
        if ($assertionsDisabled || date != null) {
            this.nativeMessage = nativeMessage;
            this.mTimestamp = date;
            return;
        }
        throw new AssertionError();
    }

    public Map<String, String> getHeaders() {
        return this.nativeMessage.getHeaders();
    }

    public String getMessageId() {
        return this.nativeMessage.getId();
    }

    public List<String> getRecipientIds() {
        return this.nativeMessage.getDestinations();
    }

    public String getSenderId() {
        return this.nativeMessage.getFrom();
    }

    public String getTextBody() {
        return this.nativeMessage.getText();
    }

    public Date getTimestamp() {
        return this.mTimestamp;
    }
}
