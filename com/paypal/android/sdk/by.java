package com.paypal.android.sdk;

public abstract class by {
    private String f200a;
    private String f201b;

    private by(String str) {
    }

    protected by(String str, String str2) {
        this(str);
        this.f200a = str2;
        this.f201b = null;
    }

    protected by(String str, String str2, String str3, String str4) {
        this(str);
        this.f200a = str2;
        this.f201b = str3;
    }

    public final String m172a() {
        return this.f201b;
    }

    public final String m173b() {
        return this.f200a;
    }

    public String toString() {
        return "ErrorBase[mErrorCode=" + this.f200a + " mErrorMsgShort=" + this.f201b + "]";
    }
}
