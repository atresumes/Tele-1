package com.sinch.android.rtc.internal.natives;

public class AccessNumber {
    private String globalNumber;
    private String localNumber;

    public AccessNumber(String str, String str2) {
        this.globalNumber = str;
        this.localNumber = str2;
    }

    public String getGlobalNumber() {
        return this.globalNumber;
    }

    public String getLocalNumber() {
        return this.localNumber;
    }
}
