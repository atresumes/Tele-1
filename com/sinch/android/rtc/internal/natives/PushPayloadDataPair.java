package com.sinch.android.rtc.internal.natives;

import com.sinch.android.rtc.PushPair;

public class PushPayloadDataPair implements PushPair {
    private String payload;
    private byte[] pushData;

    public PushPayloadDataPair(String str, byte[] bArr) {
        this.payload = str;
        this.pushData = bArr;
    }

    public byte[] getPushData() {
        return this.pushData;
    }

    public String getPushPayload() {
        return this.payload;
    }
}
