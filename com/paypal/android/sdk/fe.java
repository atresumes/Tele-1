package com.paypal.android.sdk;

public abstract class fe extends cv {
    public fe(df dfVar, cx cxVar, C0439b c0439b, String str) {
        super(new dc(dfVar), cxVar, c0439b, "Bearer " + str);
        m191a("Content-Type", "application/json");
        m191a("Accept", "application/json");
    }
}
