package com.sinch.android.rtc.internal.client.model;

import com.sinch.android.rtc.NotificationResult;
import com.sinch.android.rtc.calling.CallNotificationResult;
import com.sinch.android.rtc.internal.natives.jni.PushPayloadQuery.PushPayloadQueryResult;
import com.sinch.android.rtc.messaging.MessageNotificationResult;

public class DefaultNotificationResult implements NotificationResult {
    public static final int CALL = 1;
    public static final int MESSAGE = 2;
    public static final int UNKNOWN = 0;
    private final boolean isValid;
    private String mDisplayName;
    private final PushPayloadQueryResult queryResult;
    private final int type;

    private DefaultNotificationResult() {
        this.isValid = false;
        this.type = 0;
        this.queryResult = null;
    }

    public DefaultNotificationResult(PushPayloadQueryResult pushPayloadQueryResult) {
        this.queryResult = pushPayloadQueryResult;
        this.type = pushPayloadQueryResult.getType();
        if (this.type == 1 || this.type == 2) {
            this.isValid = pushPayloadQueryResult.isValid();
        } else {
            this.isValid = false;
        }
    }

    public static DefaultNotificationResult getNonValidResult() {
        return new DefaultNotificationResult();
    }

    public CallNotificationResult getCallResult() {
        if (!isValid()) {
            throw new IllegalStateException("Cannot return CallNotificationResult for non-valid push notification.");
        } else if (isCall()) {
            return new DefaultCallNotificationResult(this.isValid, this.queryResult.isTimedOut(), this.queryResult.getSessionId(), this.queryResult.getUserId(), this.queryResult.getVideoOffered());
        } else {
            throw new IllegalStateException("Cannot return CallNotificationResult for non-call push notification.");
        }
    }

    public String getDisplayName() {
        return this.mDisplayName;
    }

    public MessageNotificationResult getMessageResult() {
        if (!isValid()) {
            throw new IllegalStateException("Cannot return MessageNotificationResult for non-valid push notification.");
        } else if (isMessage()) {
            return new DefaultMessageNotificationResult(this.isValid, this.queryResult.getSessionId(), this.queryResult.getUserId());
        } else {
            throw new IllegalStateException("Cannot return MessageNotificationResult for non-message push notification.");
        }
    }

    public boolean isCall() {
        return this.type == 1;
    }

    public boolean isMessage() {
        return this.type == 2;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setDisplayName(String str) {
        this.mDisplayName = str;
    }
}
