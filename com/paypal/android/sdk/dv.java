package com.paypal.android.sdk;

public enum dv {
    AMEX,
    DINERSCLUB,
    DISCOVER,
    JCB,
    MASTERCARD,
    VISA,
    MAESTRO,
    UNKNOWN,
    INSUFFICIENT_DIGITS;

    public static dv m300a(String str) {
        if (str == null) {
            return UNKNOWN;
        }
        for (dv dvVar : values()) {
            if (dvVar != UNKNOWN && dvVar != INSUFFICIENT_DIGITS && str.equalsIgnoreCase(dvVar.toString())) {
                return dvVar;
            }
        }
        return UNKNOWN;
    }
}
