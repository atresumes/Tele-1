package com.paypal.android.sdk;

import java.util.Collection;

public enum dj {
    GET_FUNDING_OPTIONS("https://uri.paypal.com/services/payments/funding-options"),
    PAYMENT_CODE("https://uri.paypal.com/services/pos/payments"),
    MIS_CUSTOMER("https://uri.paypal.com/services/mis/customer"),
    FINANCIAL_INSTRUMENTS("https://uri.paypal.com/services/wallet/financial-instruments/view"),
    SEND_MONEY("https://uri.paypal.com/services/wallet/sendmoney"),
    REQUEST_MONEY("https://uri.paypal.com/services/wallet/money-request/requests"),
    LOYALTY("https://uri.paypal.com/services/loyalty/memberships/update"),
    EXPRESS_CHECKOUT("https://uri.paypal.com/services/expresscheckout");
    
    public static final Collection f314i = null;
    private String f316j;

    static {
        f314i = new dk();
    }

    private dj(String str) {
        this.f316j = str;
    }

    public final String m285a() {
        return this.f316j;
    }
}
