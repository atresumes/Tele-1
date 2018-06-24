package com.sinch.android.rtc;

public class MissingPermissionException extends RuntimeException {
    private static final long serialVersionUID = 5510657903884365439L;
    private String mRequiredPermission = "";

    public MissingPermissionException(String str) {
        super("Requires permission: " + str);
        this.mRequiredPermission = str;
    }

    public String getRequiredPermission() {
        return this.mRequiredPermission;
    }
}
