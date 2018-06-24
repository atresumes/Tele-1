package com.sinch.android.rtc.internal.service.pubnub;

public class PubNubMessage {
    private String message;
    private long timeToken;

    public PubNubMessage(String str, Long l) {
        this.message = str;
        this.timeToken = l.longValue();
    }

    public String getMessage() {
        return this.message;
    }

    public long getTimeToken() {
        return this.timeToken;
    }
}
