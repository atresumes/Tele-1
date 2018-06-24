package com.paypal.android.sdk;

public class ca extends by {
    public ca(bz bzVar, Exception exception) {
        this(bzVar.toString(), (Throwable) exception);
    }

    public ca(String str) {
        super("RequestError", str);
    }

    public ca(String str, String str2, String str3) {
        super("RequestError", str, str2, str3);
    }

    public ca(String str, Throwable th) {
        super(th.getClass().toString(), str, th.toString(), th.getMessage());
    }
}
