package com.payUMoney.sdk.entity;

public enum SdkIssuer {
    VISA,
    MASTERCARD,
    MAESTRO,
    DISCOVER,
    AMEX,
    DINER,
    UNKNOWN,
    JCB,
    LASER,
    RUPAY;

    public static SdkIssuer getIssuer(String issuer) {
        for (SdkIssuer i : values()) {
            if (i.name().equals(issuer)) {
                return i;
            }
        }
        return UNKNOWN;
    }
}
